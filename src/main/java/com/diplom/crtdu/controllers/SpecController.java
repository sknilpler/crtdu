package com.diplom.crtdu.controllers;

import com.diplom.crtdu.exception.NotFoundException;
import com.diplom.crtdu.models.*;
import com.diplom.crtdu.repo.*;
import com.diplom.crtdu.services.UserService;
import com.diplom.crtdu.utils.KidsOnMeropModel;
import com.diplom.crtdu.utils.StatKids;
import com.diplom.crtdu.utils.StatTeacher;
import com.diplom.crtdu.utils.WeekTimesModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Controller
public class SpecController {

    private long kidEditId;
    private Kid editedKid;
    private long teachEditId;
    private Teacher editedTeach;
    private Long meropEditId;
    private Meropriyatie editedMerop;
    private Long meropIDForDost;
    private Long meropIDForKids;
    private Long krujok_id;

    private Long portfolioKidId;

    @Autowired
    private KidRepository kidRepository;
    @Autowired
    private ParentsRepository parentsRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private KrujokRepository krujokRepository;
    @Autowired
    private MeropriyatieRepository meropriyatieRepository;
    @Autowired
    private TypeMeropriyatiyaRepository typeMeropriyatiyaRepository;
    @Autowired
    private LevelMeropriyatiyaRepository levelMeropriyatiyaRepository;
    @Autowired
    private DostijenieRepository dostijenieRepository;
    @Autowired
    private TypeKrujokRepository typeKrujokRepository;
    @Autowired
    private CreativeAssociationRepository caRepository;
    @Autowired
    private ZanyatieRepository zanyatieRepository;
    @Autowired
    private KidZanyatieRepository kidZanyatieRepository;
    @Autowired
    private UchastnikRepository uchastnikRepository;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private OcenkaRepository ocenkaRepository;
    @Autowired
    private TeacherDocRepository teacherDocRepository;
    @Autowired
    private NormaRepository normaRepository;
    @Autowired
    private WorkTimeRepository workTimeRepository;
    @Autowired
    private RaspisanieRepository raspisanieRepository;

    public static String convertDate(String inputDate) {
        try {
            // Создание объекта для парсинга входной даты
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (zzz)", Locale.ENGLISH);

            // Парсинг входной даты
            Date date = inputFormat.parse(inputDate);

            // Создание объекта для форматирования выходной даты
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            outputFormat.setTimeZone(TimeZone.getTimeZone("GMT+0500"));

            // Форматирование выходной даты
            String outputDate = outputFormat.format(date);

            // Возвращение выходной даты
            return outputDate;
        } catch (Exception e) {
            // Обработка ошибок парсинга даты
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/spec/list-kids")
    public String openKidsListPage(Model model) {
        model.addAttribute("kids", kidRepository.findAll());
        return "spec/list-kids";
    }

    @GetMapping("/spec/kid-add")
    public String openKidAddPage(Model model) {
        //model.addAttribute("formTarget", "_blank");
        return "spec/kids-add";
    }

    @PostMapping("/spec/kid-add/{surname}/{name}/{patronymic}/{birthday}/{sex}/{grazhdanstvo}/{adres}/{phone}/{school}/{klas}/{username}/{password}")
    public String addKid(@PathVariable("surname") String surname,
                         @PathVariable("name") String name,
                         @PathVariable("patronymic") String patronymic,
                         @PathVariable("birthday") String birthday,
                         @PathVariable("sex") String sex,
                         @PathVariable("grazhdanstvo") String grazhdanstvo,
                         @PathVariable("adres") String adres,
                         @PathVariable("phone") String phone,
                         @PathVariable("school") String school,
                         @PathVariable("klas") String klas,
                         @PathVariable("username") String username,
                         @PathVariable("password") String password,
                         Authentication authentication) {

        Role role = roleRepository.findByName("ROLE_KID");
        User user = new User(username, password, "kid", surname, name, patronymic, password);
        user.setRoles(Collections.singleton(role));
        userService.saveUser(user);
        log.warn("ADD new User: {}", user);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dateBirth = new Date();
        try {
            dateBirth = format.parse(birthday);
        } catch (ParseException e) {
            // e.printStackTrace();
            System.out.println("Не удалось преобразовать дату рождения ребенка. Входные данные: birthday=" + birthday);
        }
        boolean gender = sex.equals("male");
        Kid kid = new Kid(surname, name, patronymic, dateBirth, gender, grazhdanstvo, adres, phone, school, klas);
        kid.setUsername(username);
        kidRepository.save(kid);
        log.warn("ADD new Kid: {}", kid);

        return "redirect:/spec/list-kids";
    }

    @GetMapping("/spec/list-kids/info/{id}")
    public String openInfoKid(Model model, @PathVariable("id") Long id) {
        Kid kid = kidRepository.findById(id).orElseThrow(() -> new NotFoundException("Kid with id = " + id + " not found on server!"));
        model.addAttribute("kid", kid);
        model.addAttribute("parents", parentsRepository.findByKidsId(kid.getId()));
        return "spec/list-kids :: info-kids";
    }


    @GetMapping("/spec/list-kids/edit/{id}")
    public String openEditKid(Model model, @PathVariable("id") Long id) {
        Kid kid = kidRepository.findById(id).orElseThrow(() -> new NotFoundException("Kid with id = " + id + " not found on server!"));
        kidEditId = id;
        editedKid = kid;
        List<Parents> parents = parentsRepository.findByKidsId(kid.getId());
        model.addAttribute("kid", kid);
        model.addAttribute("parents", parents);
        model.addAttribute("parent", parents.size() > 0 ? parents.get(0) : new Parents("", "", "", "", "", ""));
        return "spec/kids-edit";
    }

    @PostMapping("/spec/list-kids/edit/{id}")
    public String saveEditKid(Model model, @PathVariable("id") Long id,
                              @RequestParam("surname") String surname,
                              @RequestParam("name") String name,
                              @RequestParam("patronymic") String patronymic,
                              @RequestParam("birthday") String birthday,
                              @RequestParam("sex") String sex,
                              @RequestParam("grazhdanstvo") String grazhdanstvo,
                              @RequestParam("adres") String adres,
                              @RequestParam("phone") String phone,
                              @RequestParam("school") String school,
                              @RequestParam("klas") String klas,
                              @RequestParam("scans") MultipartFile file) throws IOException {
        Kid kid = kidRepository.findById(id).orElseThrow(() -> new NotFoundException("Kid with id = " + id + " not found on server!"));
        kid.setSurname(surname);
        kid.setName(name);
        kid.setPatronymic(patronymic);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dateBirth = new Date();
        try {
            dateBirth = format.parse(birthday);
        } catch (ParseException e) {
            // e.printStackTrace();
            System.out.println("Не удалось преобразовать дату рождения ребенка. Входные данные: birthday=" + birthday);
        }
        kid.setBirthday(dateBirth);
        kid.setSex(sex.equals("male"));
        kid.setGrazhdanstvo(grazhdanstvo);
        kid.setAdres(adres);
        kid.setPhone(phone);
        kid.setSchool(school);
        kid.setKlas(klas);
        kidRepository.save(kid);
        log.warn("Edit Kid: {}", kid);
        // сохраняем сканы документов
        //for (MultipartFile file : scans) {
        // здесь можно сохранять файлы в БД или в файловой системе
        // например, можно использовать класс java.nio.file.Path
        // для сохранения файлов на жесткий диск в определенной директории
        // или можно сохранять файлы в BLOB-полях таблицы ребенка в БД
        // ...
        Document document = new Document();
        document.setKid(kid);
        document.setName(file.getOriginalFilename());
        document.setDate(new Date());
        document.setType(file.getContentType());
        document.setContent(file.getBytes());

        documentRepository.save(document);
        // }
        return "redirect:/spec/list-kids";
    }

    @GetMapping("/spec/list-kids/kid/edit-parent/{id}")
    public String openEditParent(Model model, @PathVariable("id") Long id) {
        Parents parents = parentsRepository.findById(id).orElseThrow(() -> new NotFoundException("Parent with id = " + id + " not found on server!"));
        model.addAttribute("parent", parents);
        return "spec/kids-edit :: edit-parent";
    }

    @GetMapping("/spec/list-kids/kid/new-parent/{surname}/{name}/{patronymic}/{adres}/{phone}/{email}")
    public String saveNewParent(Model model, @PathVariable("surname") String surname,
                                @PathVariable("name") String name,
                                @PathVariable("patronymic") String patronymic,
                                @PathVariable("adres") String adres,
                                @PathVariable("email") String email,
                                @PathVariable("phone") String phone) {
        Parents parents = new Parents(surname, name, patronymic, adres, phone, email);
        List<Kid> kids = new ArrayList<>();
        kids.add(editedKid);
        parents.setKids(kids);
        parentsRepository.save(parents);
        List<Parents> p = parentsRepository.findByKidsId(kidEditId);
        model.addAttribute("parents", p);
        return "spec/kids-edit :: parents-table";
    }

    @GetMapping("/spec/list-kids/kid/edit-parent/{id}/{surname}/{name}/{patronymic}/{adres}/{phone}/{email}")
    public String saveEditParent(Model model, @PathVariable("surname") String surname,
                                 @PathVariable("name") String name,
                                 @PathVariable("patronymic") String patronymic,
                                 @PathVariable("adres") String adres,
                                 @PathVariable("phone") String phone,
                                 @PathVariable("email") String email,
                                 @PathVariable("id") Long id) {
        Parents parents = parentsRepository.findById(id).orElseThrow(() -> new NotFoundException("Parent with id = " + id + " not found on server!"));
        parents.setSurname(surname);
        parents.setName(name);
        parents.setPatronymic(patronymic);
        parents.setAdres(adres);
        parents.setPhone(phone);
        parents.setEmail(email);
        parentsRepository.save(parents);
        List<Parents> p = parentsRepository.findByKidsId(kidEditId);
        model.addAttribute("parents", p);
        return "spec/kids-edit :: parents-table";
    }

    @PostMapping("/spec/list-kids/edit/del-parent/{id}")
    public String delParent(Model model, @PathVariable("id") Long id) {
        parentsRepository.deleteById(id);
        List<Parents> p = parentsRepository.findByKidsId(kidEditId);
        model.addAttribute("parents", p);
        return "spec/kids-edit :: parents-table";
    }

    @PostMapping("/spec/list-kids/del/{id}")
    public String delKid(Model model, @PathVariable("id") Long id) {
        Kid kid = kidRepository.findById(id).orElseThrow(() -> new NotFoundException("Kid with id = " + id + " not found on server!"));
        log.warn("Deleting Kid: {}", kid);
        List<Parents> p = parentsRepository.findByKidsId(id);
        p.forEach(parents -> {
            if ((parents.getKids().size() == 1) && (Objects.equals(parents.getKids().get(0).getId(), id))) {
                parentsRepository.deleteById(parents.getId());
                System.out.println("\tDeleted parents: ID - " + parents.getId() + " FIO - " + parents.getFullFIO());
            }
        });
        User user = userService.getUserByUsername(kid.getUsername());
        if (userService.deleteUser(user.getId())) log.warn("\tDeleted user: {}", user);
        kidRepository.deleteById(id);
        return "redirect:/spec/list-kids";
    }

    @PostMapping("/spec/list-kids/edit/upload-file")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes) throws IOException {
        Kid kid = kidRepository.findById(kidEditId).orElseThrow(() -> new NotFoundException("Kid with id = " + kidEditId + " not found on server!"));
        Document document = new Document();
        document.setKid(kid);
        document.setName(file.getOriginalFilename());
        document.setDate(new Date());
        document.setType(file.getContentType());
        document.setContent(file.getBytes());
        documentRepository.save(document);
        return "redirect:/spec/list-kids/edit/" + kidEditId;
    }


    //---------------- преподаватели ------------------------

    @GetMapping("/spec/list-teachers")
    public String openTeacherListPage(Model model) {
        boolean[][] wt = new boolean[13][7];
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 7; j++) {
                wt[i][j] = false;
            }
        }
        model.addAttribute("teachers", teacherRepository.findAll());
        model.addAttribute("arrTime", wt);
        return "spec/teacher-list";
    }

    @GetMapping("/spec/teacher-add")
    public String openTeacherAddPage(Model model) {
        return "spec/teacher-add";
    }

    @PostMapping("/spec/teacher-add/{surname}/{name}/{patronymic}/{doljnost}/{username}/{password}/{napravlenie}/{kvalif}/{staj}/{stajSpec}")
    public String addTeacher(@PathVariable("surname") String surname,
                             @PathVariable("name") String name,
                             @PathVariable("patronymic") String patronymic,
                             @PathVariable("doljnost") String doljnost,
                             @PathVariable("username") String username,
                             @PathVariable("password") String password,
                             @PathVariable("napravlenie") String napravlenie,
                             @PathVariable("kvalif") String kvalif,
                             @PathVariable("staj") String staj,
                             @PathVariable("stajSpec") String stajSpec,
                             Authentication authentication) {

        Role role = roleRepository.findByName("ROLE_TEACHER");
        User user = new User(username, password, "teacher", surname, name, patronymic, password);
        user.setRoles(Collections.singleton(role));
        userService.saveUser(user);
        log.warn("ADD new User: {}", user);
        Teacher teach = new Teacher(surname, name, patronymic, doljnost, napravlenie, kvalif, staj, stajSpec);
        teach.setUsername(username);
        teacherRepository.save(teach);
        log.warn("ADD new Teach: {}", teach);

        return "redirect:/spec/list-teachers";
    }


    @GetMapping("/spec/list-teachers/edit/{id}")
    public String openEditTeacher(Model model, @PathVariable("id") Long id) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new NotFoundException("Teacher with id = " + id + " not found on server!"));
        teachEditId = id;
        editedTeach = teacher;
        model.addAttribute("teacher", teacher);
        return "spec/teacher-edit";
    }

    @PostMapping("/spec/list-teachers/del/{id}")
    public String delTeacher(Model model, @PathVariable("id") Long id) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new NotFoundException("Teacher with id = " + id + " not found on server!"));
        log.warn("Deleting Teacher: {}", teacher);
        User user = userService.getUserByUsername(teacher.getUsername());
        if (userService.deleteUser(user.getId())) log.warn("\tDeleted user: {}", user);
        teacherRepository.deleteById(id);
        return "redirect:/spec/list-teachers";
    }

    @GetMapping("/spec/list-teachers/info/{id}")
    public String openInfoTeacher(Model model, @PathVariable("id") Long id) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new NotFoundException("Teacher with id = " + id + " not found on server!"));
        model.addAttribute("teacher", teacher);
        model.addAttribute("krujki", krujokRepository.findByTeachersId(id));
        return "spec/teacher-list :: info-teacher";
    }

    @PostMapping("/spec/list-teachers/edit/{id}/{surname}/{name}/{patronymic}/{doljnost}/{napravlenie}/{kvalif}/{staj}/{stajSpec}")
    public String saveEditTeacher(Model model, @PathVariable("id") Long id,
                                  @PathVariable("surname") String surname,
                                  @PathVariable("name") String name,
                                  @PathVariable("patronymic") String patronymic,
                                  @PathVariable("napravlenie") String napravlenie,
                                  @PathVariable("kvalif") String kvalif,
                                  @PathVariable("staj") String staj,
                                  @PathVariable("stajSpec") String stajSpec,
                                  @PathVariable("doljnost") String doljnost) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new NotFoundException("Teacher with id = " + id + " not found on server!"));
        teacher.setSurname(surname);
        teacher.setName(name);
        teacher.setPatronymic(patronymic);
        teacher.setDoljnost(doljnost);
        teacher.setNapravlenie(napravlenie);
        teacher.setKvalif(kvalif);
        teacher.setStaj(staj);
        teacher.setStajSpec(stajSpec);
        teacherRepository.save(teacher);
        log.warn("Edit Teacher: {}", teacher);
        return "redirect:/spec/list-teachers";
    }

    @GetMapping("/spec/teacher-documents/{id}")
    public ResponseEntity<byte[]> downloadDocumentForTeacher(@PathVariable("id") Long docId) {
        TeacherDoc document = teacherDocRepository.findById(docId).orElseThrow(() -> new NotFoundException("Document with id = " + docId + " not found on server!"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(document.getName()).build());
        return new ResponseEntity<>(document.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping("/spec/list-teachers/edit/upload-file")
    public String uploadFileFromTeacher(@RequestParam("file") MultipartFile file, RedirectAttributes attributes) throws IOException {
        Teacher teacher = teacherRepository.findById(teachEditId).orElseThrow(() -> new NotFoundException("Teacher with id = " + teachEditId + " not found on server!"));
        TeacherDoc document = new TeacherDoc();
        document.setTeacher(teacher);
        document.setName(file.getOriginalFilename());
        document.setDate(new Date());
        document.setType(file.getContentType());
        document.setContent(file.getBytes());
        teacherDocRepository.save(document);
        return "redirect:/spec/list-teachers/edit/" + teachEditId;
    }

    @GetMapping("/spec/list-teachers/rasp/{id}")
    public String openRaspTeacher(Model model, @PathVariable("id") Long id) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new NotFoundException("Teacher with id = " + id + " not found on server!"));
        List<WorkTime> workTimes = teacher.getWorkTimes();
        boolean[][] wt = new boolean[13][7];
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 7; j++) {
                wt[i][j] = false;
            }
        }
        for (WorkTime w : workTimes) {
            int i = 0;
            int j = 0;
            if (w.getDayOfWeek().equals("Понедельник")) j = 0;
            if (w.getDayOfWeek().equals("Вторник")) j = 1;
            if (w.getDayOfWeek().equals("Среда")) j = 2;
            if (w.getDayOfWeek().equals("Четверг")) j = 3;
            if (w.getDayOfWeek().equals("Пятница")) j = 4;
            if (w.getDayOfWeek().equals("Суббота")) j = 5;
            if (w.getDayOfWeek().equals("Воскресенье")) j = 6;

            if (w.getHour().equals("08:00")) i = 0;
            if (w.getHour().equals("09:00")) i = 1;
            if (w.getHour().equals("10:00")) i = 2;
            if (w.getHour().equals("11:00")) i = 3;
            if (w.getHour().equals("12:00")) i = 4;
            if (w.getHour().equals("13:00")) i = 5;
            if (w.getHour().equals("14:00")) i = 6;
            if (w.getHour().equals("15:00")) i = 7;
            if (w.getHour().equals("16:00")) i = 8;
            if (w.getHour().equals("17:00")) i = 9;
            if (w.getHour().equals("18:00")) i = 10;
            if (w.getHour().equals("19:00")) i = 11;
            if (w.getHour().equals("20:00")) i = 12;

            wt[i][j] = true;
        }
        model.addAttribute("teacher", teacher);
        model.addAttribute("arrTime", wt);
        return "blocks/teacher-raspisanie :: rasp-info";
    }

    @PostMapping("/spec/list-teachers/rasp/save/{id}")
    public String saveRaspTeacher(Model model, @PathVariable("id") Long id, @RequestBody String arr) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new NotFoundException("Teacher with id = " + id + " not found on server!"));
        workTimeRepository.deleteAll(teacher.getWorkTimes());
        List<WorkTime> wt = new ArrayList<>();
        arr = removeLastCharOptional(arr);
        String[] mass1 = arr.split(" ");
        for (String s : mass1) {
            String day = s.split(":")[0].substring(0, s.split(":")[0].length() / 2);
            String time = s.split(":")[0].substring(s.split(":")[0].length() / 2);
            boolean isChecked = Boolean.parseBoolean(s.split(":")[1]);
            if (isChecked) {
                if (day.equals("pn")) day = "Понедельник";
                if (day.equals("vt")) day = "Вторник";
                if (day.equals("sr")) day = "Среда";
                if (day.equals("ch")) day = "Четверг";
                if (day.equals("pt")) day = "Пятница";
                if (day.equals("sb")) day = "Суббота";
                if (day.equals("vs")) day = "Воскресенье";

                if (time.equals("08")) time = "08:00";
                if (time.equals("09")) time = "09:00";
                if (time.equals("10")) time = "10:00";
                if (time.equals("11")) time = "11:00";
                if (time.equals("12")) time = "12:00";
                if (time.equals("13")) time = "13:00";
                if (time.equals("14")) time = "14:00";
                if (time.equals("15")) time = "15:00";
                if (time.equals("16")) time = "16:00";
                if (time.equals("17")) time = "17:00";
                if (time.equals("18")) time = "18:00";
                if (time.equals("19")) time = "19:00";
                if (time.equals("20")) time = "20:00";

                wt.add(new WorkTime(day, time, teacher));
                System.out.println("Created new work time object: " + day + " " + time);
            }

        }
        workTimeRepository.saveAll(wt);
        System.out.println("Successfully save all work time objects to " + teacher.getFullFIO());
        return "redirect:/spec/list-teachers";
    }

    public static String removeLastCharOptional(String s) {
        return Optional.ofNullable(s)
                .filter(str -> str.length() != 0)
                .map(str -> str.substring(0, str.length() - 1))
                .orElse(s);
    }

    //------------------------------- мероприятия---------------

    @GetMapping("/spec/list-meropriyatiya")
    public String openMeropriyatiyaPage(Model model) {
        int year = LocalDate.now().getYear(); // текущий год
        // дата 1 января текущего года
        //String d1 = LocalDate.of(year, 1, 1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        //текущая дата
        String d1 = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // дата 31 декабря текущего года
        String d2 = LocalDate.of(year, 12, 31).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        model.addAttribute("d1", d1);
        model.addAttribute("d2", d2);
        model.addAttribute("levels", levelMeropriyatiyaRepository.findAll());
        model.addAttribute("types", typeMeropriyatiyaRepository.findAllByOrderByName());
        model.addAttribute("merop", meropriyatieRepository.findByData(d1, d2));
        return "spec/meropriyatiya";
    }

    @GetMapping("/spec/meropriyatiya-add")
    public String openMeropriyatiyaAddPage(Model model) {
        model.addAttribute("levels", levelMeropriyatiyaRepository.findAll());
        model.addAttribute("types", typeMeropriyatiyaRepository.findAllByOrderByName());
        return "spec/meropriyatiya-add";
    }

    @PostMapping("/spec/meropriyatiya-add")
    public String saveMeropriyatie(@RequestParam String name,
                                   @RequestParam String data,
                                   @RequestParam String place,
                                   @RequestParam Long type,
                                   @RequestParam Long level,
                                   @RequestParam String otvetstvenniy,
                                   Authentication authentication) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date d = dateFormat.parse(data);
        TypeMeropriyatiya t = typeMeropriyatiyaRepository.findById(type).orElseThrow(() -> new NotFoundException("Type Meropriyatiya with id = " + type + " not found on server!"));
        LevelMeropriyatiya l = levelMeropriyatiyaRepository.findById(level).orElseThrow(() -> new NotFoundException("Level Meropriyatiya with id = " + level + " not found on server!"));
        Meropriyatie m = new Meropriyatie(name, d, t, place, l, otvetstvenniy);
        log.warn("Add meropriyatie: {}", m);
        meropriyatieRepository.save(m);
        return "redirect:/spec/list-meropriyatiya";
    }

    @PostMapping("/spec/meropriyatiya/del/{id}")
    public String delMerop(Model model, @PathVariable("id") Long id) {
        Meropriyatie m = meropriyatieRepository.findById(id).orElseThrow(() -> new NotFoundException("Meropriyatie with id = " + id + " not found on server!"));
        log.warn("Deleting Meropriyatie: {}", m);
        meropriyatieRepository.deleteById(id);
        return "redirect:/spec/list-meropriyatiya";
    }


    @GetMapping("/spec/meropriyatiya/filter/{type}/{level}/{startDate}/{endDate}")
    public String filterMeropriyatiya(Model model,
                                      @PathVariable("type") String type,
                                      @PathVariable("level") String level,
                                      @PathVariable("startDate") String startDate,
                                      @PathVariable("endDate") String endDate) throws ParseException {

        List<Meropriyatie> meropriyatieList = new ArrayList<>();
        if (type.equals("0")) {
            if (level.equals("0")) {
                meropriyatieList = meropriyatieRepository.findByData(startDate, endDate);
            } else {
                meropriyatieList = meropriyatieRepository.findByDataAndLevel(startDate, endDate, Long.parseLong(level));
            }
        } else {
            if (level.equals("0")) {
                meropriyatieList = meropriyatieRepository.findByDataAndType(startDate, endDate, Long.parseLong(type));
            } else {
                meropriyatieList = meropriyatieRepository.findByDataAndLevelAndType(startDate, endDate, Long.parseLong(level), Long.parseLong(type));
            }
        }
        model.addAttribute("merop", meropriyatieList);
        return "spec/meropriyatiya :: table-merop";
    }

    @GetMapping("/spec/meropriyatiya/edit/{id}")
    public String openEditMeropriyatiya(Model model, @PathVariable("id") Long id) {
        Meropriyatie m = meropriyatieRepository.findById(id).orElseThrow(() -> new NotFoundException("Meropriyatie with id = " + id + " not found on server!"));
        meropEditId = id;
        editedMerop = m;
        model.addAttribute("levels", levelMeropriyatiyaRepository.findAll());
        model.addAttribute("types", typeMeropriyatiyaRepository.findAllByOrderByName());
        model.addAttribute("merop", m);
        return "spec/meropriyatiya-edit";
    }

    @PostMapping("/spec/meropriyatiya/edit/{id}/{name}/{data}/{place}/{type}/{level}/{otvetstvenniy}")
    public String saveEditedMeropriyatie(@PathVariable("id") Long id,
                                         @PathVariable("name") String name,
                                         @PathVariable("data") String data,
                                         @PathVariable("place") String place,
                                         @PathVariable("type") Long type_id,
                                         @PathVariable("level") Long level_id,
                                         @PathVariable("otvetstvenniy") String otvetstvenniy,
                                         Authentication authentication) throws ParseException {
        Meropriyatie m = meropriyatieRepository.findById(id).orElseThrow(() -> new NotFoundException("Meropriyatie with id = " + id + " not found on server!"));
        TypeMeropriyatiya t = typeMeropriyatiyaRepository.findById(type_id).orElseThrow(() -> new NotFoundException("Type Meropriyatiya with id = " + type_id + " not found on server!"));
        LevelMeropriyatiya l = levelMeropriyatiyaRepository.findById(level_id).orElseThrow(() -> new NotFoundException("Level Meropriyatiya with id = " + level_id + " not found on server!"));
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date d = dateFormat.parse(data);
        //TypeMeropriyatiya t = typeMeropriyatiyaRepository.findById(type).orElseThrow(() -> new NotFoundException("TypeMeropriyatiya with id = " + type + " not found on server!"));
        m.setName(name);
        m.setData(d);
        m.setPlace(place);
        m.setType(t);
        m.setLevel(l);
        m.setOtvetstvenniy(otvetstvenniy);
        meropriyatieRepository.save(m);
        log.warn("Edit Meropriyatie: {}", m);
        return "redirect:/spec/list-meropriyatiya";
    }

    @GetMapping("/spec/meropriyatiya/info/{id}")
    public String openInfoMeropriyatiya(Model model, @PathVariable("id") Long id) {
        meropIDForDost = id;
        model.addAttribute("dost", dostijenieRepository.findByMeropriyatieId(id));
        return "spec/meropriyatiya :: info-dost";
    }

    @GetMapping("/spec/meropriyatiya/dost/add/{id}")
    public String openDostAddPageForMerop(@PathVariable("id") Long id, Model model) {
        Meropriyatie m = meropriyatieRepository.findById(id).orElseThrow(() -> new NotFoundException("Meropriyatie with id = " + id + " not found on server!"));
        model.addAttribute("merop", m);
        model.addAttribute("meropriyaties", meropriyatieRepository.findAll());
        model.addAttribute("kids", kidRepository.findAll());
        model.addAttribute("teachers", teacherRepository.findAll());
        return "spec/dost-add";
    }

    @PostMapping("/spec/meropriyatiya/dost/add/{id}")
    public String saveDostAddPageForMerop(@PathVariable("id") Long id,
                                          @RequestParam String name,
                                          @RequestParam String place,
                                          @RequestParam String napravlenie,
                                          //@RequestParam String meropriyatie,
                                          @RequestParam String kid,
                                          @RequestParam String data,
                                          @RequestParam String krujok,
                                          @RequestParam String teacher,
                                          Model model) throws ParseException {
        System.out.println(id);
        Meropriyatie m = meropriyatieRepository.findById(id).orElseThrow(() -> new NotFoundException("Meropriyatie with id = " + id + " not found on server!"));
        Kid k = kidRepository.findById(Long.valueOf(kid)).orElseThrow(() -> new NotFoundException("Kid with id = " + kid + " not found on server!"));
        Teacher t = teacherRepository.findById(Long.valueOf(teacher)).orElseThrow(() -> new NotFoundException("Teacher with id = " + teacher + " not found on server!"));
        Krujok kr = krujokRepository.findById(Long.valueOf(krujok)).orElseThrow(() -> new NotFoundException("Teacher with id = " + krujok + " not found on server!"));
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d = dateFormat.parse(data);
        Dostijenie dost = dostijenieRepository.save(new Dostijenie(name, place, napravlenie, d, m, k, t, kr));
        log.warn("Save New Dostijenie: {}", dost);
        return "redirect:/spec/list-meropriyatiya";
    }

    @GetMapping("/spec/meropriyatiya/dost/add")
    public String openDostAddPage(Model model) {
        Meropriyatie m = meropriyatieRepository.findById(meropIDForDost).orElseThrow(() -> new NotFoundException("Meropriyatie with id = " + meropIDForDost + " not found on server!"));
        model.addAttribute("merop", m);
        model.addAttribute("meropriyaties", meropriyatieRepository.findAll());
        model.addAttribute("kids", kidRepository.findAll());
        model.addAttribute("teachers", teacherRepository.findAll());
        return "spec/dost-add";
    }

    @PostMapping("/spec/meropriyatiya/dost/add")
    public String saveDostAddPage(@RequestParam String name,
                                  @RequestParam String place,
                                  @RequestParam String napravlenie,
                                  // @RequestParam String meropriyatie,
                                  @RequestParam String kid,
                                  @RequestParam String teacher,
                                  @RequestParam String data,
                                  @RequestParam String krujok,
                                  Model model) throws ParseException {
        Meropriyatie m = meropriyatieRepository.findById(meropIDForDost).orElseThrow(() -> new NotFoundException("Meropriyatie with id = " + meropIDForDost + " not found on server!"));
        Kid k = kidRepository.findById(Long.valueOf(kid)).orElseThrow(() -> new NotFoundException("Kid with id = " + kid + " not found on server!"));
        Teacher t = teacherRepository.findById(Long.valueOf(teacher)).orElseThrow(() -> new NotFoundException("Teacher with id = " + teacher + " not found on server!"));
        Krujok kr = krujokRepository.findById(Long.valueOf(krujok)).orElseThrow(() -> new NotFoundException("Teacher with id = " + krujok + " not found on server!"));
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d = dateFormat.parse(data);
        Dostijenie dost = dostijenieRepository.save(new Dostijenie(name, place, napravlenie, d, m, k, t, kr));
        log.warn("Save New Dostijenie: {}", dost);
        return "redirect:/spec/list-meropriyatiya";
    }

    @GetMapping("/spec/meropriyatiya/kids-info/{id}")
    public String openKidsInfoMeropriyatiya(Model model, @PathVariable("id") Long id) {
        meropIDForKids = id;
        //List<Object[]> ids = krujokRepository.findByMeropriyatieId(id);
        List<Uchastnik> uchstniki = uchastnikRepository.findByMeropriyatieId(id);
        List<KidsOnMeropModel> kids = new ArrayList<>();
//        for (Object[] o: ids){
//            CreativeAssociation ca = caRepository.findById(Long.parseLong(o[0].toString())).orElseThrow(() -> new NotFoundException("CA with id = " + Long.parseLong(o[0].toString()) + " not found on server!"));
//            Krujok kr = krujokRepository.findById(Long.parseLong(o[1].toString())).orElseThrow(() -> new NotFoundException("Krujok with id = " + Long.parseLong(o[1].toString()) + " not found on server!"));
//            Kid kid = kidRepository.findById(Long.parseLong(o[2].toString())).orElseThrow(() -> new NotFoundException("Kid with id = " + Long.parseLong(o[2].toString()) + " not found on server!"));
//            kids.add(new KidsOnMeropModel(ca, kr, kid));
//        }
        for (Uchastnik u : uchstniki) {
            CreativeAssociation ca = u.getKrujok().getCreativeAssociation();
            Krujok kr = u.getKrujok();
            Kid kid = u.getKid();
            kids.add(new KidsOnMeropModel(ca, kr, kid));
        }
        model.addAttribute("kids", kids);
        return "spec/meropriyatiya :: info-kids";
    }

    @GetMapping("/spec/meropriyatiya/kids/add")
    public String openAddKidsToMerop(Model model) {
        Meropriyatie m = meropriyatieRepository.findById(meropIDForKids).orElseThrow(() -> new NotFoundException("Meropriyatie with id = " + meropIDForKids + " not found on server!"));
        model.addAttribute("krujki", krujokRepository.findAll());
        model.addAttribute("types", typeKrujokRepository.findAll());
        model.addAttribute("merop", m);
        return "spec/kids-add-to-merop";
    }

    @GetMapping("/spec/meropriyatie/kids/add/filter-by-krujok/{id}")
    public String filterKidsByKrujok(Model model, @PathVariable("id") Long id) {
        List<Kid> kids = kidRepository.findNotIncludedByKrujokAndMeripriyatie(id, meropIDForKids);
        model.addAttribute("kids", kids);
        return "spec/kids-add-to-merop :: table-kids";
    }

    @PostMapping("/spec/meropriyatie/kids/add/list-kid/{list}")
    public String getListKidsToMerop(@PathVariable("list") List<String> ids) {
        Meropriyatie m = meropriyatieRepository.findById(meropIDForKids).orElseThrow(() -> new NotFoundException("Meropriyatie with id = " + meropIDForKids + " not found on server!"));
        ids.forEach(s -> {
            String[] parts = s.split(":");
            long num1 = Long.parseLong(parts[0]);
            long num2 = Long.parseLong(parts[1]);
            Krujok kr = krujokRepository.findById(num2).orElseThrow(() -> new NotFoundException("Krujok with id = " + num2 + " not found on server!"));
            Kid kid = kidRepository.findById(num1).orElseThrow(() -> new NotFoundException("Kid with id = " + num1 + " not found on server!"));
            Uchastnik u = uchastnikRepository.save(new Uchastnik(kid, kr, m));
            log.warn("Saved new uchastnik : {}", u);
        });
//          Типо быстрее обработает запрос, но пока нет необходимости
//        List<Long> num1List = new ArrayList<>();
//        List<Long> num2List = new ArrayList<>();
//
//        ids.forEach(s -> {
//            String[] parts = s.split(":");
//            long num1 = Long.parseLong(parts[0]);
//            long num2 = Long.parseLong(parts[1]);
//            num1List.add(num1);
//            num2List.add(num2);
//        });
//
//        Iterable<Krujok> krujoks = krujokRepository.findAllById(num2List);
//        Iterable<Kid> kids = kidRepository.findAllById(num1List);
//
//        ids.forEach(s -> {
//            String[] parts = s.split(":");
//            long num1 = Long.parseLong(parts[0]);
//            long num2 = Long.parseLong(parts[1]);
//            Krujok kr = StreamSupport.stream(krujoks.spliterator(), false)
//                    .filter(k -> k.getId() == num2)
//                    .findFirst()
//                    .orElseThrow(() -> new NotFoundException("Krujok with id = " + num2 + " not found on server!"));
//            Kid kid = StreamSupport.stream(kids.spliterator(), false)
//                    .filter(k -> k.getId() == num1)
//                    .findFirst()
//                    .orElseThrow(() -> new NotFoundException("Kid with id = " + num1 + " not found on server!"));
//            Uchastnik uchastnik = uchastnikRepository.save(new Uchastnik(kid, kr, m));
//            log.warn("Saved new uchastnik : {}", u);
//        });

        return "redirect:/spec/list-meropriyatiya";
    }

    @GetMapping("/spec/meropriyatie/kids/add/filter-kruj-by-type/{type}")
    public String filterKrujokByType(Model model, @PathVariable("type") String type) {
        if (type.equals("0")) {
            model.addAttribute("krujki", krujokRepository.findAll());
        } else {
            model.addAttribute("krujki", krujokRepository.findByTypeKrujokIdByOrderByCreativeAssociationName(Long.parseLong(type)));
        }
        return "spec/kids-add-to-merop :: table-kruj";
    }

    @GetMapping("/spec/meropriyatie/kids/get-info/{id}")
    public String getPortfolioFromMerop(@PathVariable("id") Long id, Model model) {
        Kid k = kidRepository.findById(id).orElseThrow(() -> new NotFoundException("Kid with id = " + id + " not found on server!"));
        List<Dostijenie> dostijenies = dostijenieRepository.findByKidId(id);
        List<Krujok> krujoks = krujokRepository.findByKidId(id);
        Map<String, Double> marks = new HashMap<>();
        krujoks.forEach(krujok -> {
            List<KidZanyatie> kidZanyaties = kidZanyatieRepository.findByKidIdAndKrujokId(id, krujok.getId());
            int size = 0;
            double summ = 0.0;
            for (KidZanyatie zanyatie : kidZanyaties) {
                if (zanyatie.getOtsenka() > 0) {
                    size++;
                    summ += zanyatie.getOtsenka();
                }
            }
            double average = size > 0 ? summ / size : 0.0;
            marks.put(krujok.getName(), average);
        });

        model.addAttribute("kid", k);
        model.addAttribute("dost", dostijenies);
        model.addAttribute("marks", marks);
        model.addAttribute("krujki", StreamSupport.stream(krujokRepository.findAll().spliterator(), false)
                .filter(item -> !krujoks.contains(item))
                .collect(Collectors.toList()));
        model.addAttribute("kr", krujoks);

        return "spec/kids-add-to-merop :: info-kid";
    }


    //---------------------- Достижения -------------------------
    @GetMapping("/spec/dost-list")
    public String openDostPage(Model model) {
        model.addAttribute("dost", dostijenieRepository.findAll());
        return "spec/dost-list";
    }

    @PostMapping("/spec/dost/del/{id}")
    public String delDost(@PathVariable("id") Long id, Model model) {
        dostijenieRepository.deleteById(id);
        return "redirect:/spec/dost-list";
    }

    @GetMapping("/spec/dost-add")
    public String addDost(Model model) {
        model.addAttribute("meropriyaties", meropriyatieRepository.findAll());
        model.addAttribute("kids", kidRepository.findAll());
        model.addAttribute("teachers", teacherRepository.findAll());
        model.addAttribute("krujki", krujokRepository.findAllByOrderByCreativeAssociationNameAndKrujokName());
        return "spec/dost-add";
    }

    @PostMapping("/spec/dost-add")
    public String saveDost(@RequestParam String name,
                           @RequestParam String place,
                           @RequestParam String napravlenie,
                           @RequestParam Long kid,
                           @RequestParam Long meropriyatie,
                           @RequestParam Long teacher,
                           @RequestParam String data,
                           @RequestParam Long krujok,
                           Model model) throws ParseException {
        Meropriyatie m = meropriyatieRepository.findById(meropriyatie).orElseThrow(() -> new NotFoundException("Meropriyatie with id = " + meropriyatie + " not found on server!"));
        Kid k = kidRepository.findById(kid).orElseThrow(() -> new NotFoundException("Kid with id = " + kid + " not found on server!"));
        Teacher t = teacherRepository.findById(teacher).orElseThrow(() -> new NotFoundException("Teacher with id = " + teacher + " not found on server!"));
        Krujok kr = krujokRepository.findById(krujok).orElseThrow(() -> new NotFoundException("Teacher with id = " + krujok + " not found on server!"));
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d = dateFormat.parse(data);
        Dostijenie dost = dostijenieRepository.save(new Dostijenie(name, place, napravlenie, d, m, k, t, kr));
        log.warn("Save New Dostijenie: {}", dost);
        return "redirect:/spec/dost-list";
    }

    @GetMapping("/spec/dost/edit/{id}")
    public String openEditDost(Model model, @PathVariable("id") Long id) {
        Dostijenie d = dostijenieRepository.findById(id).orElseThrow(() -> new NotFoundException("Dostijenie with id = " + id + " not found on server!"));
        model.addAttribute("meropriyaties", meropriyatieRepository.findAll());
        model.addAttribute("kids", kidRepository.findAll());
        model.addAttribute("teachers", teacherRepository.findAll());
        model.addAttribute("krujki", krujokRepository.findAllByOrderByCreativeAssociationNameAndKrujokName());
        model.addAttribute("dost", d);
        return "spec/dost-edit";
    }

    @PostMapping("/spec/dost/edit/{id}/{name}/{place}/{meropriyatie}/{kid}/{teacher}/{napravlenie}/{data}/{krujok}")
    public String saveDostAfterEditing(@PathVariable("id") Long id,
                                       @PathVariable("name") String name,
                                       @PathVariable("napravlenie") String napravlenie,
                                       @PathVariable("place") String place,
                                       @PathVariable("kid") Long kid,
                                       @PathVariable("meropriyatie") Long meropriyatie,
                                       @PathVariable("teacher") Long teacher,
                                       @PathVariable("data") String data,
                                       @PathVariable("krujok") Long krujok,
                                       Model model) throws ParseException {
        Meropriyatie m = meropriyatieRepository.findById(meropriyatie).orElseThrow(() -> new NotFoundException("Meropriyatie with id = " + meropriyatie + " not found on server!"));
        Kid k = kidRepository.findById(kid).orElseThrow(() -> new NotFoundException("Kid with id = " + kid + " not found on server!"));
        Teacher t = teacherRepository.findById(teacher).orElseThrow(() -> new NotFoundException("Teacher with id = " + teacher + " not found on server!"));
        Dostijenie dost = dostijenieRepository.findById(id).orElseThrow(() -> new NotFoundException("Dostijenie with id = " + id + " not found on server!"));
        Krujok kr = krujokRepository.findById(krujok).orElseThrow(() -> new NotFoundException("Teacher with id = " + krujok + " not found on server!"));
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d = dateFormat.parse(data);
        dost.setName(name);
        dost.setWinPlace(place);
        dost.setKid(k);
        dost.setMeropriyatie(m);
        dost.setTeacher(t);
        dost.setNapravlenie(napravlenie);
        dost.setData(d);
        dost.setKrujok(kr);
        dostijenieRepository.save(dost);
        log.warn("Save edited Dostijenie: {}", dost);
        return "redirect:/spec/dost-list";
    }

    //-------------------- кружки ------------------------------
    //-------------------- виды кружков ---------------------
    @GetMapping("/spec/kruj-type-list")
    public String openKryjTypePage(Model model) {
        model.addAttribute("type", new TypeKrujok());
        model.addAttribute("types", typeKrujokRepository.findAll());
        return "spec/kruj-type-list";
    }

    @PostMapping("/spec/kruj-type-list/save-new/{name}")
    public String saveKryjType(Model model,
                               @PathVariable("name") String name) {
        TypeKrujok t = typeKrujokRepository.save(new TypeKrujok(name));
        log.warn("Save new type kruj: {}", t);
        return "redirect:/spec/kruj-type-list";
    }

    @GetMapping("/spec/kruj-type/edit/{id}")
    public String editKrujType(Model model,
                               @PathVariable("id") Long id) {
        TypeKrujok t = typeKrujokRepository.findById(id).orElseThrow(() -> new NotFoundException("Type krujok with id = " + id + " not found on server!"));
        model.addAttribute("type", t);
        return "spec/kruj-type-list :: form-type";
    }

    @PostMapping("/spec/kruj-type-list/edit/{id}/{name}")
    public String saveEditedKryjType(Model model,
                                     @PathVariable("id") Long id,
                                     @PathVariable("name") String name) {
        TypeKrujok t = typeKrujokRepository.findById(id).orElseThrow(() -> new NotFoundException("Type krujok with id = " + id + " not found on server!"));
        t.setName(name);
        typeKrujokRepository.save(t);
        return "redirect:/spec/kruj-type-list";
    }

    @PostMapping("/spec/kruj-type/del/{id}")
    public String delTypeKruj(@PathVariable("id") Long id) {
        typeKrujokRepository.deleteById(id);
        log.warn("Deleted type kruj with id: {}", id);
        return "redirect:/spec/kruj-type-list";
    }

    //------------------- твороческие объединения ----------

    @GetMapping("/spec/kruj-ca")
    public String openCAPage(Model model) {
        model.addAttribute("cas", caRepository.findAllByOrderByName());
        model.addAttribute("ca", new CreativeAssociation());
        model.addAttribute("types", typeKrujokRepository.findAll());
        model.addAttribute("typesE", typeKrujokRepository.findAll());
        model.addAttribute("teachers", teacherRepository.findAll());
        model.addAttribute("krujok", new Krujok());
        return "spec/creative-association";
    }

    @GetMapping("/spec/ca-add-new/{name}/{pdoSurname}/{pdoName}/{pdoPatronymic}")
    public String addNewCA(Model model, @PathVariable("name") String name,
                           @PathVariable("pdoSurname") String pdoSurname,
                           @PathVariable("pdoName") String pdoName,
                           @PathVariable("pdoPatronymic") String pdoPatronymic) {
        caRepository.save(new CreativeAssociation(name, pdoSurname, pdoName, pdoPatronymic));
        model.addAttribute("cas", caRepository.findAllByOrderByName());
        return "spec/creative-association :: ca-table";
    }

    @PostMapping("/spec/ca-delete/{id}")
    public String delCA(@PathVariable("id") Long id) {
        caRepository.deleteById(id);
        return "redirect:/spec/kruj-ca";
    }


    @GetMapping("/spec/ca-edit/{id}")
    public String editCA(Model model, @PathVariable("id") Long id) {
        CreativeAssociation ca = caRepository.findById(id).orElseThrow(() -> new NotFoundException("Creative Association with id = " + id + " not found on server!"));
        model.addAttribute("ca", ca);
        return "spec/creative-association :: ca-edit";
    }

    @GetMapping("/spec/ca-save-edit/{id}/{name}/{pdoSurname}/{pdoName}/{pdoPatronymic}")
    public String saveEditCA(Model model, @PathVariable("id") Long id,
                             @PathVariable("name") String name,
                             @PathVariable("pdoSurname") String pdoSurname,
                             @PathVariable("pdoName") String pdoName,
                             @PathVariable("pdoPatronymic") String pdoPatronymic) {
        CreativeAssociation ca = caRepository.findById(id).orElseThrow(() -> new NotFoundException("Creative Association with id = " + id + " not found on server!"));
        System.out.println(pdoName);
        System.out.println(pdoSurname);
        System.out.println(pdoPatronymic);

        ca.setName(name);
        ca.setPdoSurname(pdoSurname);
        ca.setPdoName(pdoName);
        ca.setPdoPatronymic(pdoPatronymic);
        caRepository.save(ca);
        model.addAttribute("cas", caRepository.findAllByOrderByName());
        return "spec/creative-association :: ca-table";
    }

    @GetMapping("/spec/kruj-load-for/{id}")
    public String loadKruj(Model model, @PathVariable("id") Long id) {
        krujok_id = id;
        model.addAttribute("krujki", krujokRepository.findByCreativeAssociationId(id));
        return "spec/creative-association :: kruj-table";
    }

    @GetMapping("/spec/kruj-add-new/{name}/{id}/{vozrast}/{type}")
    public String saveKruj(Model model,
                           @PathVariable("id") Long id,
                           @PathVariable("name") String name,
                           @PathVariable("vozrast") String vozrast,
                           @PathVariable("type") Long type) {
        //Krujok k = krujokRepository.findById(id).orElseThrow(() -> new NotFoundException("Krujok with id = " + id + " not found on server!"));
        CreativeAssociation ca = caRepository.findById(id).orElseThrow(() -> new NotFoundException("Creative Association with id = " + id + " not found on server!"));
        TypeKrujok tk = typeKrujokRepository.findById(type).orElseThrow(() -> new NotFoundException("Type kryjok with id = " + type + " not found on server!"));
        Krujok k = new Krujok();
        k.setName(name);
        k.setVozrast(vozrast);
        k.setTypeKrujok(tk);
        k.setCreativeAssociation(ca);
        krujokRepository.save(k);
        model.addAttribute("krujki", krujokRepository.findByCreativeAssociationId(id));
        return "spec/creative-association :: kruj-table";
    }

    @GetMapping("/spec/kruj-add-new/{name}/{id}/{vozrast}/{type}/list-ids/{list}")
    public String saveKrujWithTeachers(Model model,
                                       @PathVariable("id") Long id,
                                       @PathVariable("name") String name,
                                       @PathVariable("vozrast") String vozrast,
                                       @PathVariable("type") Long type,
                                       @PathVariable("list") List<String> ids) {
        List<Teacher> teachers = ids.stream()
                .map(Long::valueOf)
                .map(teacherRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        CreativeAssociation ca = caRepository.findById(id).orElseThrow(() -> new NotFoundException("Creative Association with id = " + id + " not found on server!"));
        TypeKrujok tk = typeKrujokRepository.findById(type).orElseThrow(() -> new NotFoundException("Type kryjok with id = " + type + " not found on server!"));
        Krujok k = new Krujok();
        k.setName(name);
        k.setVozrast(vozrast);
        k.setTypeKrujok(tk);
        k.setCreativeAssociation(ca);
        k.setTeachers(teachers);
        krujokRepository.save(k);
        model.addAttribute("krujki", krujokRepository.findByCreativeAssociationId(id));
        return "spec/creative-association :: kruj-table";
    }


    @GetMapping("/spec/kruj-edit/{id}")
    public String editKruj(Model model, @PathVariable("id") Long id) {
        Krujok k = krujokRepository.findById(id).orElseThrow(() -> new NotFoundException("Krujok with id = " + id + " not found on server!"));
        model.addAttribute("krujok", k);
        model.addAttribute("teachers_e", teacherRepository.findAll());
        model.addAttribute("typesE", typeKrujokRepository.findAll());
        return "spec/creative-association :: edit-kruj";
    }


    @GetMapping("/spec/kruj-edit/{id}/{name}/{vozrast}/{type}")
    public String saveEditedKruj(Model model,
                                 @PathVariable("id") Long id,
                                 @PathVariable("name") String name,
                                 @PathVariable("vozrast") String vozrast,
                                 @PathVariable("type") Long type) {
        Krujok k = krujokRepository.findById(id).orElseThrow(() -> new NotFoundException("Krujok with id = " + id + " not found on server!"));
        TypeKrujok tk = typeKrujokRepository.findById(type).orElseThrow(() -> new NotFoundException("Type kryjok with id = " + type + " not found on server!"));
        k.setName(name);
        k.setVozrast(vozrast);
        k.setTypeKrujok(tk);
        krujokRepository.save(k);
        model.addAttribute("krujki", krujokRepository.findByCreativeAssociationId(k.getCreativeAssociation().getId()));
        return "spec/creative-association :: kruj-table";
    }


    @GetMapping("/spec/kruj-edit/{id}/{name}/{vozrast}/{type}/list-ids/{list}")
    public String saveEditedKrujWithTeachers(Model model,
                                             @PathVariable("id") Long id,
                                             @PathVariable("name") String name,
                                             @PathVariable("vozrast") String vozrast,
                                             @PathVariable("type") Long type,
                                             @PathVariable("list") List<String> ids) {
        Krujok k = krujokRepository.findById(id).orElseThrow(() -> new NotFoundException("Krujok with id = " + id + " not found on server!"));
        TypeKrujok tk = typeKrujokRepository.findById(type).orElseThrow(() -> new NotFoundException("Type kryjok with id = " + type + " not found on server!"));
        List<Teacher> teachers = ids.stream()
                .map(Long::valueOf)
                .map(teacherRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        k.setName(name);
        k.setVozrast(vozrast);
        k.setTypeKrujok(tk);
        k.setTeachers(teachers);
        krujokRepository.save(k);
        model.addAttribute("krujki", krujokRepository.findByCreativeAssociationId(k.getCreativeAssociation().getId()));
        return "spec/creative-association :: kruj-table";
    }

    @GetMapping("/spec/del-kruj/{id}")
    public String delKruj(Model model, @PathVariable("id") Long id) {
        Krujok k = krujokRepository.findById(id).orElseThrow(() -> new NotFoundException("Krujok with id = " + id + " not found on server!"));
        Long id_ca = k.getCreativeAssociation().getId();
        krujokRepository.deleteById(id);
        model.addAttribute("krujki", krujokRepository.findByCreativeAssociationId(id_ca));
        return "spec/creative-association :: kruj-table";
    }
    //--------------- portfolio-----------------

    @GetMapping("/spec/get-portfolio/{id}")
    public String getPortfolio(@PathVariable("id") Long id, Model model) throws JsonProcessingException {
        Kid k = kidRepository.findById(id).orElseThrow(() -> new NotFoundException("Kid with id = " + id + " not found on server!"));
        portfolioKidId = id;
        List<Parents> parents = parentsRepository.findByKidsId(id);
        List<Dostijenie> dostijenies = dostijenieRepository.findByKidId(id);
        List<Krujok> krujoks = krujokRepository.findByKidId(id);
        Map<String, Integer> marks = new HashMap<>();
        List<String> s = new ArrayList<>();
        List<List<Integer>> g = new ArrayList<>();
        krujoks.forEach(krujok -> {
            //List<KidZanyatie> kidZanyaties = kidZanyatieRepository.findByKidIdAndKrujokId(id, krujok.getId());
            //int size = 0;
            //double summ = 0.0;
            //for (KidZanyatie zanyatie : kidZanyaties) {
            //    if (zanyatie.getOtsenka() > 0) {
            //        size++;
            //        summ += zanyatie.getOtsenka();
            //    }
            //}
            //double average = size > 0 ? summ / size : 0.0;
            s.add(krujok.getName());
            List<Ocenka> ocenki = ocenkaRepository.findByKidIdAndKrujokId(id, krujok.getId());
            List<Integer> bally = new ArrayList<>();
            int size = ocenki.size();
            if (size > 0) {
                marks.put(krujok.getName(), ocenki.get(size - 1).getBall());
                for (Ocenka o : ocenki) {
                    bally.add(o.getBall());
                }
            } else {
                marks.put(krujok.getName(), 0);
            }
            g.add(bally);


        });


        // Определение размеров двумерного массива
        int rowCount = g.size();
        int colCount = 0;

// Нахождение максимальной длины внутренних списков
        for (List<Integer> row : g) {
            colCount = Math.max(colCount, row.size());
        }

// Создание и заполнение двумерного массива
        int[][] grades = new int[rowCount][colCount];
        for (int i = 0; i < rowCount; i++) {

            List<Integer> row = g.get(i);
            for (int j = 0; j < row.size(); j++) {
                grades[i][j] = row.get(j);
            }
        }
        int[] dates = new int[colCount];
        for (int i = 0; i < colCount; i++) {
            dates[i] = i + 1;
        }
        String[] subjects = s.toArray(new String[0]);


// Передача строк JSON в модель
        model.addAttribute("dates", dates);
        model.addAttribute("subjects", subjects);
        model.addAttribute("grades", grades);
        model.addAttribute("kid", k);
        model.addAttribute("parents", parents);
        model.addAttribute("dost", dostijenies);
        model.addAttribute("marks", marks);
        model.addAttribute("krujki", StreamSupport.stream(krujokRepository.findAll().spliterator(), false)
                .filter(item -> !krujoks.contains(item))
                .collect(Collectors.toList()));
        model.addAttribute("kr", krujoks);

        return "spec/portfolio";
    }


    @GetMapping("/spec/portfolio/add-to-kruj/{id}")
    public String addKidToKrujok(@PathVariable("id") Long id, Model model) {
        Krujok kr = krujokRepository.findById(id).orElseThrow(() -> new NotFoundException("Krujok with id = " + id + " not found on server!"));
        Kid kid = kidRepository.findById(portfolioKidId).orElseThrow(() -> new NotFoundException("Kid with id = " + portfolioKidId + " not found on server!"));
        List<Kid> kids = kr.getKids();
        kids.add(kid);
        kr.setKids(kids);
        krujokRepository.save(kr);
        log.warn("Saved new krujok-kid: {}", kr);
        List<Krujok> krujoks = krujokRepository.findByKidId(portfolioKidId);
        model.addAttribute("krujki", StreamSupport.stream(krujokRepository.findAll().spliterator(), false)
                .filter(item -> !krujoks.contains(item))
                .collect(Collectors.toList()));

        return "spec/portfolio :: kruj-table";
    }

    @GetMapping("/spec/portfolio/del-from-kruj/{id}")
    public String delKidFromKrujok(@PathVariable("id") Long id, Model model) {
        Krujok kr = krujokRepository.findById(id).orElseThrow(() -> new NotFoundException("Krujok with id = " + id + " not found on server!"));
        Kid kid = kidRepository.findById(portfolioKidId).orElseThrow(() -> new NotFoundException("Kid with id = " + portfolioKidId + " not found on server!"));
        List<Kid> kids = kr.getKids();
        kids.remove(kid);
        kr.setKids(kids);
        krujokRepository.save(kr);
        log.warn("Deleted krujok-kid: {}", kr);
        List<Krujok> krujoks = krujokRepository.findByKidId(portfolioKidId);
        model.addAttribute("kr", krujoks);

        return "spec/portfolio :: kruj-table-vih";
    }

    @GetMapping("/spec/documents/{id}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable("id") Long docId) {
        Document document = documentRepository.findById(docId).orElseThrow(() -> new NotFoundException("Document with id = " + docId + " not found on server!"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(document.getName()).build());
        return new ResponseEntity<>(document.getContent(), headers, HttpStatus.OK);
    }


    //------------------------------- statistiks by kids ---------------------

    @GetMapping("/spec/stat-kids")
    public String showDiagram(Model model) {
        int kidRF = kidRepository.findByGrazhdanstvoAndArchive("РФ", false).size();
        int kidRK = kidRepository.findByGrazhdanstvoAndArchive("РК", false).size();
        int kidOther = kidRepository.findByGrazhdanstvoAndArchive("Другое", false).size();

        int kidM = kidRepository.findBySexAndArchive(true, false).size();
        int kidW = kidRepository.findBySexAndArchive(false, false).size();

        int[] dataByGender = {kidM, kidW}; // количество мальчиков и девочек
        int[] dataByCitizenship = {kidRF, kidRK, kidOther}; // количество по гражданству

        List<Kid> kidsAll = kidRepository.findByArchive(false);
        int from3to5m = 0;
        int from6to8m = 0;
        int from9to11m = 0;
        int from12to15m = 0;
        int from16to17m = 0;

        int from3to5f = 0;
        int from6to8f = 0;
        int from9to11f = 0;
        int from12to15f = 0;
        int from16to17f = 0;

        for (Kid kid : kidsAll) {
            int age = kid.getAge();
            if (kid.isSex()) {
                if (age >= 3 && age <= 5) {
                    from3to5m++;
                } else if (age >= 6 && age <= 8) {
                    from6to8m++;
                } else if (age >= 9 && age <= 11) {
                    from9to11m++;
                } else if (age >= 12 && age <= 15) {
                    from12to15m++;
                } else if (age >= 16 && age <= 17) {
                    from16to17m++;
                }
            } else {
                if (age >= 3 && age <= 5) {
                    from3to5f++;
                } else if (age >= 6 && age <= 8) {
                    from6to8f++;
                } else if (age >= 9 && age <= 11) {
                    from9to11f++;
                } else if (age >= 12 && age <= 15) {
                    from12to15f++;
                } else if (age >= 16 && age <= 17) {
                    from16to17f++;
                }
            }
        }
        int[] dataByAgesM = {from3to5m, from6to8m, from9to11m, from12to15m, from16to17m};
        int[] dataByAgesF = {from3to5f, from6to8f, from9to11f, from12to15f, from16to17f};

        //таблица с подробными данными
        List<StatKids> list = new ArrayList<>();
        List<Object[]> objectList = kidRepository.getStatByKids();

        for (Object[] obj : objectList) {
            list.add(new StatKids((String) obj[0], (String) obj[1], Integer.parseInt(obj[2].toString()), Integer.parseInt(obj[3].toString()), Integer.parseInt(obj[4].toString()), Integer.parseInt(obj[5].toString()), Integer.parseInt(obj[6].toString()), Integer.parseInt(obj[7].toString())));
        }
        List<String> napravleniya = new ArrayList<>();
        List<Integer> numNapr = new ArrayList<>();
        Iterable<TypeKrujok> types = typeKrujokRepository.findAll();
        for (TypeKrujok t : types) {
            napravleniya.add(t.getName());
            int count = 0;
            List<Krujok> krujki = krujokRepository.findByTypeKrujokIdByOrderByCreativeAssociationName(t.getId());
            for (Krujok k : krujki) {
                count = count + k.getKids().size();
            }
            numNapr.add(count);
        }
        String[] labelNapr = napravleniya.toArray(new String[0]);
        int[] arrNapr = numNapr.stream().mapToInt(Integer::intValue).toArray();

        List<Dostijenie> dostijeniya = new ArrayList<>();
        kidsAll.forEach(kid -> {
            dostijeniya.addAll(dostijenieRepository.findByKidId(kid.getId()));
        });
        List<String> napravleniya2 = dostijenieRepository.getNapravlenie();
        int[] numdost = new int[napravleniya2.size()];
        int i = 0;
        for (String s : napravleniya2) {
            int k = 0;
            for (Dostijenie d : dostijeniya) {
                if (d.getNapravlenie().equals(s)) {
                    k++;
                }
            }
            numdost[i] = k;
            i++;
        }
        String[] labelNapr2 = napravleniya2.toArray(new String[0]);
        model.addAttribute("dataByGender", dataByGender);
        model.addAttribute("dataByCitizenship", dataByCitizenship);
        model.addAttribute("total", list);
        model.addAttribute("dataByAgesM", dataByAgesM);
        model.addAttribute("dataByAgesF", dataByAgesF);
        model.addAttribute("labelNapr", labelNapr);
        model.addAttribute("arrNapr", arrNapr);
        model.addAttribute("labelNapr2", labelNapr2);
        model.addAttribute("numDost", numdost);
        return "stat/stat-by-kids";
    }

    @GetMapping("/spec/stat-teacher")
    public String showTeacherStat(Model model) {
        int year = LocalDate.now().getYear(); // текущий год
        // дата 1 января текущего года
        String d1 = LocalDate.of(year, 1, 1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        //текущая дата
        String d2 = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // дата 31 декабря текущего года
        //String d2 = LocalDate.of(year, 12, 31).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        model.addAttribute("d1", d1);
        model.addAttribute("d2", d2);
        //таблица с подробными данными
        List<StatTeacher> list = new ArrayList<>();
        List<Object[]> objectList = teacherRepository.getStatByDost(d1, d2);

        for (Object[] obj : objectList) {
            list.add(new StatTeacher((String) obj[1], Integer.parseInt(obj[2].toString()), Integer.parseInt(obj[3].toString()),
                    Integer.parseInt(obj[4].toString()), Integer.parseInt(obj[5].toString()), Integer.parseInt(obj[6].toString()),
                    Integer.parseInt(obj[7].toString()), Integer.parseInt(obj[8].toString()), Integer.parseInt(obj[9].toString()),
                    Integer.parseInt(obj[10].toString()), teacherRepository.getNumKidsByTeacher(Long.parseLong(obj[0].toString())),
                    Objects.requireNonNull(teacherRepository.findById(Long.parseLong(obj[0].toString())).orElse(null)).getKrujki().size()));
        }
        model.addAttribute("total", list);
        return "stat/stat-by-teacher";
    }

    @GetMapping("/spec/stat-teacher/{d1}/{d2}")
    public String filterTeacherStat(Model model, @PathVariable("d1") String d1,
                                    @PathVariable("d2") String d2) {

        //таблица с подробными данными
        List<StatTeacher> list = new ArrayList<>();

        List<Object[]> objectList = teacherRepository.getStatByDost(d1, d2);

        for (Object[] obj : objectList) {
            list.add(new StatTeacher((String) obj[1], Integer.parseInt(obj[2].toString()), Integer.parseInt(obj[3].toString()),
                    Integer.parseInt(obj[4].toString()), Integer.parseInt(obj[5].toString()), Integer.parseInt(obj[6].toString()),
                    Integer.parseInt(obj[7].toString()), Integer.parseInt(obj[8].toString()), Integer.parseInt(obj[9].toString()),
                    Integer.parseInt(obj[10].toString()), teacherRepository.getNumKidsByTeacher(Long.parseLong(obj[0].toString())),
                    Objects.requireNonNull(teacherRepository.findById(Long.parseLong(obj[0].toString())).orElse(null)).getKrujki().size()));
        }
        model.addAttribute("total", list);

        return "stat/stat-by-teacher :: table-stat";
    }

    //-------------------------- нормы -----------------------

    @GetMapping("/spec/norma-list")
    public String openNormaPage(Model model) {
        model.addAttribute("norma", new Norma());
        model.addAttribute("types", typeKrujokRepository.findAll());
        model.addAttribute("norms", normaRepository.findAll());
        return "spec/norms";
    }

    @PostMapping("/spec/norma/save-new/{type}/{age}/{hoursPreWeek}/{hoursPerDay}")
    public String saveNorma(Model model,
                            @PathVariable("type") Long id_type,
                            @PathVariable("age") String age,
                            @PathVariable("hoursPreWeek") int hoursPreWeek,
                            @PathVariable("hoursPerDay") int hoursPerDay) {
        TypeKrujok type = typeKrujokRepository.findById(id_type).orElseThrow(() -> new NotFoundException("Type krujok with id = " + id_type + " not found on server!"));

        Norma norma = normaRepository.save(new Norma(age, hoursPreWeek, hoursPerDay, type));

        log.warn("Save new norma: {}", norma);
        return "redirect:/spec/norma-list";
    }

    @GetMapping("/spec/norma/edit/{id}")
    public String editNorma(Model model,
                            @PathVariable("id") Long id) {
        Norma norma = normaRepository.findById(id).orElseThrow(() -> new NotFoundException("Norma with id = " + id + " not found on server!"));
        model.addAttribute("norma", norma);
        model.addAttribute("types", typeKrujokRepository.findAll());
        return "spec/norms :: form-norma";
    }

    @PostMapping("/spec/norma/edit/{id}/{type}/{age}/{hoursPreWeek}/{hoursPerDay}")
    public String saveEditedNorma(Model model,
                                  @PathVariable("id") Long id,
                                  @PathVariable("type") Long id_type,
                                  @PathVariable("age") String age,
                                  @PathVariable("hoursPreWeek") int hoursPreWeek,
                                  @PathVariable("hoursPerDay") int hoursPerDay) {
        TypeKrujok t = typeKrujokRepository.findById(id).orElseThrow(() -> new NotFoundException("Type krujok with id = " + id + " not found on server!"));
        Norma norma = normaRepository.findById(id).orElseThrow(() -> new NotFoundException("Norma with id = " + id + " not found on server!"));
        norma.setAge(age);
        norma.setHoursPerWeek(hoursPreWeek);
        norma.setHoursPerDay(hoursPerDay);
        norma.setTypeKrujok(t);
        normaRepository.save(norma);
        return "redirect:/spec/norma-list";
    }

    @PostMapping("/spec/norma/del/{id}")
    public String delNorma(@PathVariable("id") Long id) {
        normaRepository.deleteById(id);
        log.warn("Deleted norma with id: {}", id);
        return "redirect:/spec/norma-list";
    }
    //------------------------- Расптсание ----------------------

    private String calculateEndTime(String startTime, int duration) {
        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = start.plusHours(duration);
        return end.toString();
    }

    /*
        private Norma findNormaByAge(List<Norma> normi, String vozrast) {
            for (Norma norma : normi) {
                if (vozrast.equals(norma.getAge())) {
                    return norma;
                }
            }
            return new Norma("",4,2,null);
        }

        private List<Teacher> findAvailableTeachers(List<Teacher> teachers, Krujok krujok, List<WorkTime> workTimes) {
            List<Teacher> availableTeachers = new ArrayList<>();
            for (Teacher teacher : teachers) {
                if (teacher.getKrujki().contains(krujok) && hasAvailableWorkTime(workTimes, teacher)) {
                    availableTeachers.add(teacher);
                }
            }
            return availableTeachers;
        }

        private boolean hasAvailableWorkTime(List<WorkTime> workTimes, Teacher teacher) {
            for (WorkTime workTime : workTimes) {
                if (workTime.getTeacher().equals(teacher)) {
                    return true;
                }
            }
            return false;
        }

        private List<WorkTime> findAvailableWorkTimes(List<WorkTime> workTimes, String dayOfWeek, List<Teacher> availableTeachers) {
            List<WorkTime> availableWorkTimes = new ArrayList<>();
            for (WorkTime workTime : workTimes) {
                if (workTime.getDayOfWeek().equalsIgnoreCase(dayOfWeek) && availableTeachers.contains(workTime.getTeacher())) {
                    availableWorkTimes.add(workTime);
                }
            }
            return availableWorkTimes;
        }

        private String getDayOfWeekByIndex(int index) {
            String[] daysOfWeek = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"};
            if (index >= 0 && index < daysOfWeek.length) {
                return daysOfWeek[index];
            }
            return "";
        }

        public void generateSchedule(List<Krujok> krujki, List<Teacher> teachers, List<Norma> normi, List<WorkTime> workTimes) {
            for (Krujok krujok : krujki) {
                Norma norma = findNormaByAge(normi, krujok.getVozrast());
                List<Teacher> availableTeachers = findAvailableTeachers(teachers, krujok, workTimes);
                int maxHoursPerWeek = norma.getHoursPerWeek();
                int maxHoursPerDay = norma.getHoursPerDay();

                int hoursPerWeek = 0;
                int hoursPerDay = 0;
                int dayOfWeekIndex = 0;

                for (Raspisanie raspisanie : krujok.getRaspisanie()) {
                    if (hoursPerWeek >= maxHoursPerWeek) {
                        break;
                    }

                    String dayOfWeek = getDayOfWeekByIndex(dayOfWeekIndex);
                    List<WorkTime> availableWorkTimes = findAvailableWorkTimes(workTimes, dayOfWeek, availableTeachers);

                    if (availableWorkTimes.isEmpty()) {
                        continue;
                    }

                    if (hoursPerDay >= maxHoursPerDay) {
                        dayOfWeekIndex++;
                        hoursPerDay = 0;
                        continue;
                    }

                    WorkTime workTime = availableWorkTimes.get(0);
                    raspisanie.setTeacher(workTime.getTeacher());

                    String startTime = workTime.getHour();
                    String endTime = calculateEndTime(startTime, maxHoursPerDay); // Рассчитываем время окончания занятия

                    switch (dayOfWeek) {
                        case "Понедельник":
                            raspisanie.setMonday(startTime + "-" + endTime);
                            break;
                        case "Вторник":
                            raspisanie.setTuesday(startTime + "-" + endTime);
                            break;
                        case "Среда":
                            raspisanie.setWednesday(startTime + "-" + endTime);
                            break;
                        case "Четверг":
                            raspisanie.setThursday(startTime + "-" + endTime);
                            break;
                        case "Пятница":
                            raspisanie.setFriday(startTime + "-" + endTime);
                            break;
                        case "Суббота":
                            raspisanie.setSaturday(startTime + "-" + endTime);
                            break;
                        case "Воскресенье":
                            raspisanie.setSunday(startTime + "-" + endTime);
                            break;
                    }
                    System.out.println(raspisanie.toString());
                    raspisanieRepository.save(raspisanie);

                    hoursPerWeek++;
                    hoursPerDay++;
                }
            }
        }
    */
    @GetMapping("/spec/raspisanie/create")
    public String createRaspisanie() {
        List<Krujok> krujki = krujokRepository.findAllByOrderByCreativeAssociationNameAndKrujokName();
        String[] daysOfWeek = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"};
        List<Raspisanie> raspisanie = new ArrayList<>();
        List<WorkTime> busyTime = new ArrayList<>();
        for (Krujok krujok : krujki) {
            int usedHoursWeek = 0;
            List<Norma> norms = normaRepository.findByAgeAndTypeKrujokId(krujok.getVozrast(), krujok.getTypeKrujok().getId());
            if (norms.size() > 0) {
                Norma norma = norms.get(0);
                for (Teacher t : krujok.getTeachers()) {
                    Raspisanie rasp = new Raspisanie();
                    for (String day : daysOfWeek) {
                        List<WorkTime> times = workTimeRepository.findByDayOfWeekAndTeacherId(day, t.getId());
                        for (WorkTime time : busyTime) {
                            times.remove(time);
                        }
                        if (t.getSurname().equals("Герасимчук")){
                            System.out.println(times.size());
                            System.out.println(norma.getHoursPerDay());
                            System.out.println(usedHoursWeek);
                            System.out.println(norma.getHoursPerWeek());
                            System.out.println("------------------------------");
                        }
                        if ((times.size() >= norma.getHoursPerDay()) && (usedHoursWeek <= (norma.getHoursPerWeek() - norma.getHoursPerDay()))) {
                            String startTime = times.get(0).getHour();
                            String endTime = calculateEndTime(startTime, norma.getHoursPerDay());
                            for (int i = 0; i < norma.getHoursPerDay(); i++) {
                                busyTime.add(times.get(i));
                            }
                            switch (day) {
                                case "Понедельник":
                                    rasp.setMonday(startTime + "-" + endTime);
                                    break;
                                case "Вторник":
                                    rasp.setTuesday(startTime + "-" + endTime);
                                    break;
                                case "Среда":
                                    rasp.setWednesday(startTime + "-" + endTime);
                                    break;
                                case "Четверг":
                                    rasp.setThursday(startTime + "-" + endTime);
                                    break;
                                case "Пятница":
                                    rasp.setFriday(startTime + "-" + endTime);
                                    break;
                                case "Суббота":
                                    rasp.setSaturday(startTime + "-" + endTime);
                                    break;
                                case "Воскресенье":
                                    rasp.setSunday(startTime + "-" + endTime);
                                    break;
                            }
                            usedHoursWeek = usedHoursWeek + norma.getHoursPerDay();
                        } else {
                            switch (day) {
                                case "Понедельник":
                                    rasp.setMonday("");
                                    break;
                                case "Вторник":
                                    rasp.setTuesday("");
                                    break;
                                case "Среда":
                                    rasp.setWednesday("");
                                    break;
                                case "Четверг":
                                    rasp.setThursday("");
                                    break;
                                case "Пятница":
                                    rasp.setFriday("");
                                    break;
                                case "Суббота":
                                    rasp.setSaturday("");
                                    break;
                                case "Воскресенье":
                                    rasp.setSunday("");
                                    break;
                            }
                        }
                    }
                    rasp.setTeacher(t);
                    rasp.setKrujok(krujok);
                    raspisanie.add(rasp);
                }
            }
        }

        raspisanie.forEach(raspisanie1 -> System.out.println(raspisanie1.toString()));
        return "home";
    }


}


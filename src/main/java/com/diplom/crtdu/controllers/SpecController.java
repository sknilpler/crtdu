package com.diplom.crtdu.controllers;

import com.diplom.crtdu.exception.NotFoundException;
import com.diplom.crtdu.models.*;
import com.diplom.crtdu.repo.*;
import com.diplom.crtdu.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
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


//    public static String cyrillicToLatin(String input) {
//        // Массив для хранения заменяемых символов
//        String[][] replacements = {
//                {"а", "a"}, {"б", "b"}, {"в", "v"}, {"г", "g"}, {"д", "d"}, {"е", "e"}, {"ё", "yo"}, {"ж", "zh"},
//                {"з", "z"}, {"и", "i"}, {"й", "y"}, {"к", "k"}, {"л", "l"}, {"м", "m"}, {"н", "n"}, {"о", "o"},
//                {"п", "p"}, {"р", "r"}, {"с", "s"}, {"т", "t"}, {"у", "u"}, {"ф", "f"}, {"х", "h"}, {"ц", "c"},
//                {"ч", "ch"}, {"ш", "sh"}, {"щ", "sch"}, {"ъ", ""}, {"ы", "y"}, {"ь", ""}, {"э", "e"}, {"ю", "yu"},
//                {"я", "ya"}, {"А", "A"}, {"Б", "B"}, {"В", "V"}, {"Г", "G"}, {"Д", "D"}, {"Е", "E"}, {"Ё", "Yo"},
//                {"Ж", "Zh"}, {"З", "Z"}, {"И", "I"}, {"Й", "Y"}, {"К", "K"}, {"Л", "L"}, {"М", "M"}, {"Н", "N"},
//                {"О", "O"}, {"П", "P"}, {"Р", "R"}, {"С", "S"}, {"Т", "T"}, {"У", "U"}, {"Ф", "F"}, {"Х", "H"},
//                {"Ц", "C"}, {"Ч", "Ch"}, {"Ш", "Sh"}, {"Щ", "Sch"}, {"Ъ", ""}, {"Ы", "Y"}, {"Ь", ""}, {"Э", "E"},
//                {"Ю", "Yu"}, {"Я", "Ya"}
//        };
//
//        // Заменяем символы в строке
//        for (String[] replacement : replacements) {
//            input = input.replace(replacement[0], replacement[1]);
//        }
//
//        return input;
//    }

    public static String convertDate(String inputDate) {
        try {
            // Создание объекта для парсинга входной даты
            //SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (zzz)", Locale.ENGLISH);
            //inputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
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

    @PostMapping("/spec/list-kids/edit/{id}/{surname}/{name}/{patronymic}/{birthday}/{sex}/{grazhdanstvo}/{adres}/{phone}/{school}/{klas}")
    public String saveEditKid(Model model, @PathVariable("id") Long id,
                              @PathVariable("surname") String surname,
                              @PathVariable("name") String name,
                              @PathVariable("patronymic") String patronymic,
                              @PathVariable("birthday") String birthday,
                              @PathVariable("sex") String sex,
                              @PathVariable("grazhdanstvo") String grazhdanstvo,
                              @PathVariable("adres") String adres,
                              @PathVariable("phone") String phone,
                              @PathVariable("school") String school,
                              @PathVariable("klas") String klas) {
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


    //---------------- преподаватели ------------------------

    @GetMapping("/spec/list-teachers")
    public String openTeacherListPage(Model model) {
        model.addAttribute("teachers", teacherRepository.findAll());
        return "spec/teacher-list";
    }

    @GetMapping("/spec/teacher-add")
    public String openTeacherAddPage(Model model) {
        return "spec/teacher-add";
    }

    @PostMapping("/spec/teacher-add/{surname}/{name}/{patronymic}/{doljnost}/{username}/{password}")
    public String addTeacher(@PathVariable("surname") String surname,
                             @PathVariable("name") String name,
                             @PathVariable("patronymic") String patronymic,
                             @PathVariable("doljnost") String doljnost,
                             @PathVariable("username") String username,
                             @PathVariable("password") String password,
                             Authentication authentication) {

        Role role = roleRepository.findByName("ROLE_TEACHER");
        User user = new User(username, password, "teacher", surname, name, patronymic, password);
        user.setRoles(Collections.singleton(role));
        userService.saveUser(user);
        log.warn("ADD new User: {}", user);
        Teacher teach = new Teacher(surname, name, patronymic, doljnost);
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

    @PostMapping("/spec/list-teachers/edit/{id}/{surname}/{name}/{patronymic}/{doljnost}")
    public String saveEditTeacher(Model model, @PathVariable("id") Long id,
                                  @PathVariable("surname") String surname,
                                  @PathVariable("name") String name,
                                  @PathVariable("patronymic") String patronymic,
                                  @PathVariable("doljnost") String doljnost) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new NotFoundException("Teacher with id = " + id + " not found on server!"));
        teacher.setSurname(surname);
        teacher.setName(name);
        teacher.setPatronymic(patronymic);
        teacher.setDoljnost(doljnost);
        teacherRepository.save(teacher);
        log.warn("Edit Teacher: {}", teacher);
        return "redirect:/spec/list-teachers";
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
        model.addAttribute("merop", meropriyatieRepository.findByData(d1,d2));
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
                                          //@RequestParam String meropriyatie,
                                          @RequestParam String kid,
                                          @RequestParam String teacher,
                                          Model model) {
        System.out.println(id);
        Meropriyatie m = meropriyatieRepository.findById(id).orElseThrow(() -> new NotFoundException("Meropriyatie with id = " + id + " not found on server!"));
        Kid k = kidRepository.findById(Long.valueOf(kid)).orElseThrow(() -> new NotFoundException("Kid with id = " + kid + " not found on server!"));
        Teacher t = teacherRepository.findById(Long.valueOf(teacher)).orElseThrow(() -> new NotFoundException("Teacher with id = " + teacher + " not found on server!"));
        Dostijenie d = dostijenieRepository.save(new Dostijenie(name, place, m, k, t));
        log.warn("Save New Dostijenie: {}", d);
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
                                  // @RequestParam String meropriyatie,
                                  @RequestParam String kid,
                                  @RequestParam String teacher,
                                  Model model) {
        Meropriyatie m = meropriyatieRepository.findById(meropIDForDost).orElseThrow(() -> new NotFoundException("Meropriyatie with id = " + meropIDForDost + " not found on server!"));
        Kid k = kidRepository.findById(Long.valueOf(kid)).orElseThrow(() -> new NotFoundException("Kid with id = " + kid + " not found on server!"));
        Teacher t = teacherRepository.findById(Long.valueOf(teacher)).orElseThrow(() -> new NotFoundException("Teacher with id = " + teacher + " not found on server!"));
        Dostijenie d = dostijenieRepository.save(new Dostijenie(name, place, m, k, t));
        log.warn("Save New Dostijenie: {}", d);
        return "redirect:/spec/list-meropriyatiya";
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
        return "spec/dost-add";
    }

    @PostMapping("/spec/dost-add")
    public String saveDost(@RequestParam String name,
                           @RequestParam String place,
                           @RequestParam Long kid,
                           @RequestParam Long meropriyatie,
                           @RequestParam Long teacher,
                           Model model) {
        Meropriyatie m = meropriyatieRepository.findById(meropriyatie).orElseThrow(() -> new NotFoundException("Meropriyatie with id = " + meropriyatie + " not found on server!"));
        Kid k = kidRepository.findById(kid).orElseThrow(() -> new NotFoundException("Kid with id = " + kid + " not found on server!"));
        Teacher t = teacherRepository.findById(teacher).orElseThrow(() -> new NotFoundException("Teacher with id = " + teacher + " not found on server!"));
        Dostijenie d = dostijenieRepository.save(new Dostijenie(name, place, m, k, t));
        log.warn("Save New Dostijenie: {}", d);
        return "redirect:/spec/list-meropriyatiya";
    }

    @GetMapping("/spec/dost/edit/{id}")
    public String openEditDost(Model model, @PathVariable("id") Long id) {
        Dostijenie d = dostijenieRepository.findById(id).orElseThrow(() -> new NotFoundException("Dostijenie with id = " + id + " not found on server!"));
        model.addAttribute("meropriyaties", meropriyatieRepository.findAll());
        model.addAttribute("kids", kidRepository.findAll());
        model.addAttribute("teachers", teacherRepository.findAll());
        model.addAttribute("dost", d);
        return "spec/dost-edit";
    }

    @PostMapping("/spec/dost/edit/{id}/{name}/{place}/{meropriyatie}/{kid}/{teacher}")
    public String saveDostAfterEditing(@PathVariable("id") Long id,
                                       @PathVariable("name") String name,
                                       @PathVariable("place") String place,
                                       @PathVariable("kid") Long kid,
                                       @PathVariable("meropriyatie") Long meropriyatie,
                                       @PathVariable("teacher") Long teacher,
                                       Model model) {
        Meropriyatie m = meropriyatieRepository.findById(meropriyatie).orElseThrow(() -> new NotFoundException("Meropriyatie with id = " + meropriyatie + " not found on server!"));
        Kid k = kidRepository.findById(kid).orElseThrow(() -> new NotFoundException("Kid with id = " + kid + " not found on server!"));
        Teacher t = teacherRepository.findById(teacher).orElseThrow(() -> new NotFoundException("Teacher with id = " + teacher + " not found on server!"));
        Dostijenie d = dostijenieRepository.findById(id).orElseThrow(() -> new NotFoundException("Dostijenie with id = " + id + " not found on server!"));
        d.setName(name);
        d.setWinPlace(place);
        d.setKid(k);
        d.setMeropriyatie(m);
        d.setTeacher(t);
        dostijenieRepository.save(d);
        log.warn("Save edited Dostijenie: {}", d);
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

    @GetMapping("/spec/ca-add-new/{name}")
    public String addNewCA(Model model, @PathVariable("name") String name) {
        caRepository.save(new CreativeAssociation(name));
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

    @GetMapping("/spec/ca-save-edit/{id}/{name}")
    public String saveEditCA(Model model, @PathVariable("id") Long id, @PathVariable("name") String name) {
        CreativeAssociation ca = caRepository.findById(id).orElseThrow(() -> new NotFoundException("Creative Association with id = " + id + " not found on server!"));
        ca.setName(name);
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
    public String getPortfolio(@PathVariable("id") Long id, Model model) {
        Kid k = kidRepository.findById(id).orElseThrow(() -> new NotFoundException("Kid with id = " + id + " not found on server!"));
        portfolioKidId = id;
        List<Parents> parents = parentsRepository.findByKidsId(id);
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

}


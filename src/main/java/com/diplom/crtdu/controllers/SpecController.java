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
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Controller
public class SpecController {

    private long kidEditId;
    private Kid editedKid;
    private long teachEditId;
    private Teacher editedTeach;
    private Long meropEditId;
    private Meropriyatie editedMerop;

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
        model.addAttribute("merop", meropriyatieRepository.findAll());
        return "spec/meropriyatiya";
    }

    @GetMapping("/spec/meropriyatiya-add")
    public String openMeropriyatiyaAddPage(Model model) {
        model.addAttribute("types", typeMeropriyatiyaRepository.findAllByOrderByName());
        return "spec/meropriyatiya-add";
    }

    @PostMapping("/spec/meropriyatiya-add")
    public String saveMeropriyatie(@RequestParam String name,
                                   @RequestParam String data,
                                   @RequestParam String place,
                                   @RequestParam String type,
                                   @RequestParam String level,
                                   @RequestParam String otvetstvenniy,
                                   Authentication authentication) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date d = dateFormat.parse(data);
        //TypeMeropriyatiya t = typeMeropriyatiyaRepository.findById(type).orElseThrow(() -> new NotFoundException("TypeMeropriyatiya with id = " + type + " not found on server!"));
        Meropriyatie m = new Meropriyatie(name, d, type, place, level, otvetstvenniy);
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
                                      @PathVariable("endDate") String endDate) {
        System.out.println(type);
        System.out.println(level);
        System.out.println(startDate);
        System.out.println(endDate);
        model.addAttribute("merop", meropriyatieRepository.findAll());
        return "spec/meropriyatiya :: table-merop";
    }

    @GetMapping("/spec/meropriyatiya/edit/{id}")
    public String openEditMeropriyatiya(Model model, @PathVariable("id") Long id) {
        Meropriyatie m = meropriyatieRepository.findById(id).orElseThrow(() -> new NotFoundException("Meropriyatie with id = " + id + " not found on server!"));
        meropEditId = id;
        editedMerop = m;
        model.addAttribute("types", typeMeropriyatiyaRepository.findAllByOrderByName());
        model.addAttribute("merop", m);
        return "spec/meropriyatiya-edit";
    }

    @PostMapping("/spec/meropriyatiya/edit/{id}/{name}/{data}/{place}/{type}/{level}/{otvetstvenniy}")
    public String saveEditedMeropriyatie(@PathVariable("id") Long id,
                                         @PathVariable("name") String name,
                                         @PathVariable("data") String data,
                                         @PathVariable("place") String place,
                                         @PathVariable("type") String type,
                                         @PathVariable("level") String level,
                                         @PathVariable("otvetstvenniy") String otvetstvenniy,
                                         Authentication authentication) throws ParseException {
        Meropriyatie m = meropriyatieRepository.findById(id).orElseThrow(() -> new NotFoundException("Meropriyatie with id = " + id + " not found on server!"));
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date d = dateFormat.parse(data);
        //TypeMeropriyatiya t = typeMeropriyatiyaRepository.findById(type).orElseThrow(() -> new NotFoundException("TypeMeropriyatiya with id = " + type + " not found on server!"));
        m.setName(name);
        m.setData(d);
        m.setPlace(place);
        m.setType(type);
        m.setLevel(level);
        m.setOtvetstvenniy(otvetstvenniy);
        meropriyatieRepository.save(m);
        log.warn("Edit Meropriyatie: {}", m);
        return "redirect:/spec/list-meropriyatiya";
    }
}


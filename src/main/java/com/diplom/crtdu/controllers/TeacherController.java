package com.diplom.crtdu.controllers;

import com.diplom.crtdu.models.*;
import com.diplom.crtdu.repo.*;
import com.diplom.crtdu.services.TeacherService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.BiFunction;

@Slf4j
@Controller
public class TeacherController {

    @Autowired
    private KidRepository kidRepository;
    @Autowired
    private ZanyatieRepository zanyatieRepository;
    @Autowired
    private KidZanyatieRepository kidZanyatieRepository;
    @Autowired
    private KrujokRepository krujokRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private TeacherService teacherService;
    private Long id_kruj;


    @GetMapping("/teacher/posehenie")
    public String openPosehenie(Model model, Authentication authentication) {
        Teacher teacher = teacherRepository.findByUsername(authentication.getName());
        List<Krujok> krujki = krujokRepository.findByTeachersId(teacher.getId());
        id_kruj = krujki.get(0).getId();
        if (krujki.size() > 0) {
            List<Zanyatie> zanyaties = zanyatieRepository.findByKrujokId(krujki.get(0).getId());
            List<Kid> fromKrujok = krujki.get(0).getKids();
            model.addAttribute("krujki", krujki);
            model.addAttribute("kids", fromKrujok);
            model.addAttribute("zanyatiya", zanyaties);
            model.addAttribute("teacherService", teacherService);
        }
        return "teacher/poseshaemost";
    }

    @GetMapping("/teacher/posehenie/{id}")
    public String openPosehenieByKruj(Model model, Authentication authentication, @PathVariable("id") Long id) {
        Teacher teacher = teacherRepository.findByUsername(authentication.getName());
        Krujok krujok = krujokRepository.findById(id).orElse(null);
        id_kruj = krujok.getId();
            List<Zanyatie> zanyaties = zanyatieRepository.findByKrujokId(id);
            List<Kid> fromKrujok = krujok.getKids();
            model.addAttribute("kids", fromKrujok);
            model.addAttribute("zanyatiya", zanyaties);
            model.addAttribute("teacherService", teacherService);
        return "teacher/poseshaemost :: table-pos";
    }

    @GetMapping("/teacher/posehenie/mark/{id}")
    public String markKruj(Model model, Authentication authentication, @PathVariable("id") Long id) {

        KidZanyatie kz = kidZanyatieRepository.findById(id).orElse(null);
        kz.setPoseshchenie(!kz.isPoseshchenie());
        kidZanyatieRepository.save(kz);
        Krujok krujok = krujokRepository.findById(id_kruj).orElse(null);
        List<Zanyatie> zanyaties = zanyatieRepository.findByKrujokId(id_kruj);
        List<Kid> fromKrujok = krujok.getKids();
        model.addAttribute("kids", fromKrujok);
        model.addAttribute("zanyatiya", zanyaties);
        model.addAttribute("teacherService", teacherService);
        return "teacher/poseshaemost :: table-pos";
    }

    @GetMapping("/teacher/posehenie/new-zanyatie/{data}/{dlitelnost}/{mesto}")
    public String newZan(Model model, Authentication authentication,
                         @PathVariable("data") String data,
                         @PathVariable("dlitelnost") int dlitelnost,
                         @PathVariable("mesto") String mesto){

        System.out.println(data);
        Krujok krujok = krujokRepository.findById(id_kruj).orElse(null);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date();
        try {
            d = format.parse(data);
        } catch (ParseException e) {
            // e.printStackTrace();
            System.out.println("Не удалось преобразовать дату");
        }
        Zanyatie zanyatie= zanyatieRepository.save(new Zanyatie(d,dlitelnost,mesto,krujok));
        for (Kid k: krujok.getKids()) {
            kidZanyatieRepository.save(new KidZanyatie(k,zanyatie));
        }
        model.addAttribute("kids",  krujok.getKids());
        model.addAttribute("zanyatiya", zanyatieRepository.findByKrujokId(id_kruj));
        model.addAttribute("teacherService", teacherService);
        return "teacher/poseshaemost :: table-pos";
    }


}

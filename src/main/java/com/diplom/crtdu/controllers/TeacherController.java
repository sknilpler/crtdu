package com.diplom.crtdu.controllers;

import com.diplom.crtdu.models.Krujok;
import com.diplom.crtdu.models.Teacher;
import com.diplom.crtdu.models.Zanyatie;
import com.diplom.crtdu.repo.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

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


    @GetMapping("/teacher/posehenie")
    public String openPosehenie(Model model, Authentication authentication){
       // Teacher teacher = teacherRepository.findByUsername(authentication.getName());
       // List<Krujok> krujki = krujokRepository.findByTeachersId(teacher.getId());
       // List<Zanyatie> zanyaties = zanyatieRepository.findByKrujokId(krujki.get(0).getId());

        return "teacher/poseshaemost";
    }

}

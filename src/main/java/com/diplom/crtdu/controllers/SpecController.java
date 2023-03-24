package com.diplom.crtdu.controllers;

import com.diplom.crtdu.models.Kid;
import com.diplom.crtdu.repo.KidRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Controller
public class SpecController {

    @Autowired
    private KidRepository kidRepository;

    @GetMapping("/spec/list-kids")
    public String openKidsListPage(Model model) {
        model.addAttribute("kids", kidRepository.findAll());
        return "spec/list-kids";
    }

    @GetMapping("/spec/kid-add")
    public String openKidAddPage() {
        return "spec/kids-add";
    }

    @PostMapping("/spec/kid-add")
    public String addKid(@RequestParam String surname,
                         @RequestParam String name,
                         @RequestParam String patronymic,
                         @RequestParam String birthday,
                         @RequestParam String sex,
                         @RequestParam String grazhdanstvo,
                         @RequestParam String adres,
                         @RequestParam String phone,
                         @RequestParam String school,
                         @RequestParam String klas,
                         Authentication authentication) {

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
        kidRepository.save(kid);
        log.warn("ADD new Kid: {}", kid);
        return "redirect:/spec/list-kids";
    }
}

package com.diplom.crtdu.controllers;

import com.diplom.crtdu.exception.NotFoundException;
import com.diplom.crtdu.models.Kid;
import com.diplom.crtdu.models.Parents;
import com.diplom.crtdu.repo.KidRepository;
import com.diplom.crtdu.repo.ParentsRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
public class SpecController {

    private long kidEditId;
    private Kid editedKid;

    @Autowired
    private KidRepository kidRepository;

    @Autowired
    private ParentsRepository parentsRepository;

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

    @GetMapping("/spec/list-kids/info/{id}")
    public String openInfoKid(Model model, @PathVariable("id") Long id) {
        Kid kid = kidRepository.findById(id).orElseThrow(() -> new NotFoundException("Kid with id = " + id + " not found on server!"));
        model.addAttribute("kid", kid);
        model.addAttribute("parents", parentsRepository.findByKidsId(kid.getId()));
        return "spec/list-kids :: info-kids";
    }

    @PostMapping("/spec/list-kids/{id}/remove")
    public String kidDelete(@PathVariable(value = "id") long id, Model model) {
        Kid kid = kidRepository.findById(id).orElseThrow(() -> new NotFoundException("Kid with id = " + id + " not found on server!"));
        kidRepository.deleteById(id);
        return "redirect:/spec/list-kids";
    }

    @GetMapping("/spec/list-kids/edit/{id}")
    public String openEditKid(Model model, @PathVariable("id") Long id) {
        Kid kid = kidRepository.findById(id).orElseThrow(() -> new NotFoundException("Kid with id = " + id + " not found on server!"));
        kidEditId = id;
        editedKid = kid;
        List<Parents> parents = parentsRepository.findByKidsId(kid.getId());
        model.addAttribute("kid", kid);
        model.addAttribute("parents", parents);
        model.addAttribute("parent", parents.size() > 0 ? parents.get(0) : new Parents("", "", "", "", ""));
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

    @GetMapping("/spec/list-kids/kid/new-parent/{surname}/{name}/{patronymic}/{adres}/{phone}")
    public String saveNewParent(Model model, @PathVariable("surname") String surname,
                              @PathVariable("name") String name,
                              @PathVariable("patronymic") String patronymic,
                              @PathVariable("adres") String adres,
                              @PathVariable("phone") String phone) {
        Parents parents = new Parents(surname,name,patronymic,adres,phone);
        List<Kid> kids = new ArrayList<>();
        kids.add(editedKid);
        parents.setKids(kids);
        parentsRepository.save(parents);
        List<Parents> p = parentsRepository.findByKidsId(kidEditId);
        model.addAttribute("parents", p);
        return "spec/kids-edit :: parents-table";
    }

    @GetMapping("/spec/list-kids/kid/edit-parent/{id}/{surname}/{name}/{patronymic}/{adres}/{phone}")
    public String saveEditParent(Model model, @PathVariable("surname") String surname,
                                @PathVariable("name") String name,
                                @PathVariable("patronymic") String patronymic,
                                @PathVariable("adres") String adres,
                                @PathVariable("phone") String phone,
                                 @PathVariable("id") Long id) {
        Parents parents = parentsRepository.findById(id).orElseThrow(() -> new NotFoundException("Parent with id = " + id + " not found on server!"));
        parents.setSurname(surname);
        parents.setName(name);
        parents.setPatronymic(patronymic);
        parents.setAdres(adres);
        parents.setPhone(phone);
        parentsRepository.save(parents);
        List<Parents> p = parentsRepository.findByKidsId(kidEditId);
        model.addAttribute("parents", p);
        return "spec/kids-edit :: parents-table";
    }

    @GetMapping("/spec/list-kids/edit/del-parent/{id}")
    public String delParent(Model model, @PathVariable("id") Long id) {
        parentsRepository.deleteById(id);
        List<Parents> p = parentsRepository.findByKidsId(kidEditId);
        model.addAttribute("parents", p);
        return "spec/kids-edit :: parents-table";
    }

    @GetMapping("/spec/list-kids/del/{id}")
    public String delKid(Model model, @PathVariable("id") Long id) {
        boolean haveAnotherKid = false;

        List<Parents> p = parentsRepository.findByKidsId(kidEditId);
        parentsRepository.deleteById(id);
        model.addAttribute("parents", p);
        return "spec/kids-edit :: parents-table";
    }
}


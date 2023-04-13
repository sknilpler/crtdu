package com.diplom.crtdu.controllers;

import com.diplom.crtdu.models.Role;
import com.diplom.crtdu.models.TypeMeropriyatiya;
import com.diplom.crtdu.models.User;
import com.diplom.crtdu.repo.RoleRepository;
import com.diplom.crtdu.repo.TypeMeropriyatiyaRepository;
import com.diplom.crtdu.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.Date;

@Slf4j
@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TypeMeropriyatiyaRepository typeMeropriyatiyaRepository;

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        //если программа впервые запускается на сервере
        if (roleRepository.findAll().size() == 0){
            System.out.println("ROLES in Database NOT present! Add them to DB");
            roleRepository.save(new Role("ROLE_ADMIN"));
            roleRepository.save(new Role("ROLE_USER"));
            roleRepository.save(new Role("ROLE_KID"));
            roleRepository.save(new Role("ROLE_TEACHER"));
            System.out.println("Roles successfully added!");
        }
        if (userService.allUsers().size() == 0) {
            System.out.println("ADMIN in Users NOT present! Add him to DB");
            User user = new User("admin", "admin", "admin");
            user.setRoles(Collections.singleton(roleRepository.findByName("ROLE_ADMIN")));
            userService.saveUser(user);
            System.out.println("User ADMIN successfully saved!");
        }

        if (typeMeropriyatiyaRepository.findAll().size() == 0){
            System.out.println("Add list of types meropriyatiya");
            typeMeropriyatiyaRepository.save(new TypeMeropriyatiya("Балет"));
            typeMeropriyatiyaRepository.save(new TypeMeropriyatiya("Вечеринка"));
            typeMeropriyatiyaRepository.save(new TypeMeropriyatiya("Викторина"));
            typeMeropriyatiyaRepository.save(new TypeMeropriyatiya("Встреча с автором"));
            typeMeropriyatiyaRepository.save(new TypeMeropriyatiya("Выставка"));
            typeMeropriyatiyaRepository.save(new TypeMeropriyatiya("Гастрономический мастер-класс"));
            typeMeropriyatiyaRepository.save(new TypeMeropriyatiya("Игровой турнир"));
            typeMeropriyatiyaRepository.save(new TypeMeropriyatiya("Караоке"));
            typeMeropriyatiyaRepository.save(new TypeMeropriyatiya("Киносеанс"));
            typeMeropriyatiyaRepository.save(new TypeMeropriyatiya("Кинопоказ с обсуждением"));
            typeMeropriyatiyaRepository.save(new TypeMeropriyatiya("Книжная выставка"));
            typeMeropriyatiyaRepository.save(new TypeMeropriyatiya("Конкурс"));
            typeMeropriyatiyaRepository.save(new TypeMeropriyatiya("Конференция"));
            typeMeropriyatiyaRepository.save(new TypeMeropriyatiya("Концерт"));
            typeMeropriyatiyaRepository.save(new TypeMeropriyatiya("Кулинарный мастер-класс"));
            typeMeropriyatiyaRepository.save(new TypeMeropriyatiya("Лекция"));
            typeMeropriyatiyaRepository.save(new TypeMeropriyatiya("Мастер-класс"));
            typeMeropriyatiyaRepository.save(new TypeMeropriyatiya("Музыкальная премьера"));
            typeMeropriyatiyaRepository.save(new TypeMeropriyatiya("Мюзикл"));
            typeMeropriyatiyaRepository.save(new TypeMeropriyatiya("Показ мод"));
            typeMeropriyatiyaRepository.save(new TypeMeropriyatiya("Спектакль-интерактив"));
            typeMeropriyatiyaRepository.save(new TypeMeropriyatiya("Спектакль с музыкой"));
            typeMeropriyatiyaRepository.save(new TypeMeropriyatiya("Спортивное мероприятие"));
            typeMeropriyatiyaRepository.save(new TypeMeropriyatiya("Театральное представление"));
            typeMeropriyatiyaRepository.save(new TypeMeropriyatiya("Фестиваль"));
            typeMeropriyatiyaRepository.save(new TypeMeropriyatiya("Флешмоб"));
            typeMeropriyatiyaRepository.save(new TypeMeropriyatiya("Фотовыставка"));
            typeMeropriyatiyaRepository.save(new TypeMeropriyatiya("Цирковое представление"));
            typeMeropriyatiyaRepository.save(new TypeMeropriyatiya("Шоу-программа"));
        }

        model.addAttribute("title", "Главная страница");
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "login");
        return "login";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("title", "Администрирование");
        return "admin/admin";
    }

    @GetMapping("/success-login")
    public String successLogin(Model model, Authentication authentication){
        model.addAttribute("userType", userService.getUserByUsername(authentication.getName()).getType());
        return "success-login";
    }

}

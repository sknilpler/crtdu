package com.diplom.crtdu.controllers;

import com.diplom.crtdu.models.Role;
import com.diplom.crtdu.models.User;
import com.diplom.crtdu.repo.RoleRepository;
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

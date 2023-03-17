package com.diplom.crtdu.controllers;


import com.diplom.crtdu.exception.NotFoundException;
import com.diplom.crtdu.models.Role;
import com.diplom.crtdu.models.User;
import com.diplom.crtdu.repo.RoleRepository;
import com.diplom.crtdu.repo.UserRepository;
import com.diplom.crtdu.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/admin/all-users")
    public String userList(Model model, Authentication authentication) {
        //Получаем пользователя, под которым выполнен вход (страница доступна только апдмину, соответсвенно пользователь будет только с ролью админа.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //Получаем списко всех пользователей, для отображения на странице
        List<User> var_list = userService.allUsers();
        //Удаляем текущего пользователя из списка для отображения, чтобы он не мог удалить сам себя
        var_list.remove(userRepository.findByUsername(auth.getName()));
        model.addAttribute("tUsers", var_list.stream().filter(user -> user.getType().contains("teacher")).collect(Collectors.toList()));
        model.addAttribute("kUsers", var_list.stream().filter(user -> user.getType().contains("kid")).collect(Collectors.toList()));
        model.addAttribute("oUsers", var_list.stream().filter(user -> user.getType().contains("other")).collect(Collectors.toList()));
        return "admin/all-users";
    }

    /**
     * Формируем динамически страницу для каждого пользователя. Внутри страницы можно сделать операции над пользователем
     */
    @GetMapping("/admin/all-users/user-details/{id}")
    public String userDetails(@PathVariable(value = "id") long id, Model model) {
        if (!userRepository.existsById(id)) {
            return "redirect:/admin/all-users";
        }
        Optional<User> user = userRepository.findById(id);
        ArrayList<User> res = new ArrayList<>();
        user.ifPresent(res::add);
        model.addAttribute("user", res);
        Iterable<Role> roles = roleRepository.findAll();
        //удаление из списка ролей тех что уже назначены пользователю
        Iterator<Role> itr = roles.iterator();
        List<Role> userRoles = new ArrayList<>(res.get(0).getRoles());
        //цикл удаления
        while (itr.hasNext()) {
            Role role = itr.next();
            for (Role r : userRoles) {
                if (r.equals(role)) {
                    itr.remove();
                }
            }
        }
        model.addAttribute("roles", roles);
        //Создаем объект var_AdminRole и добавляем его в атрибуты страницы.
        //чтобы потом определять пользователей у которого есть такая роль и не выводить кнопку "Сделать администратором" на странице user-details
        Role var_AdminRole = new Role(2L, "ROLE_ADMIN");
        model.addAttribute("var_AdminRole", var_AdminRole);
        return "admin/user-details";
    }

    @PostMapping("/admin/all-users/user-details/{id}/add_admin")
    public String add_admin(@PathVariable(value = "id") long id, Authentication authentication) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with ID " + id + " not found"));
        user.getRoles().add(new Role(2L, "ROLE_ADMIN"));
        userRepository.save(user);
        return "redirect:/admin/all-users";
    }

    @PostMapping("/admin/all-users/user-details/{id}/remove")
    public String delete_user(@PathVariable(value = "id") long id, Authentication authentication) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with ID " + id + " not found"));
        userRepository.delete(user);
        return "redirect:/admin/all-users";
    }

    @GetMapping("/admin/all-users/add-user")
    public String add_user(Model model) {
        Iterable<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);
        return "admin/add-user";
    }

    @PostMapping("/admin/all-users/add-user")
    public String save_user(@RequestParam String username,
                            @RequestParam String password,
                            @RequestParam String surname,
                            @RequestParam String name,
                            @RequestParam String patronymic,
                            @RequestParam String type,
                            @RequestParam Long dropRole, Authentication authentication) {
        Role role = roleRepository.findById(dropRole).orElseThrow(() -> new NotFoundException("Role " + dropRole + " not found"));
        User user = new User(username, password, type, surname, name, patronymic, password);
        user.setRoles(Collections.singleton(role));
        userService.saveUser(user);
        return "redirect:/admin/all-users";
    }

    @PostMapping("/admin/all-users/user-details/{user_id}/add_roles/{selRecs}")
    public String addRolesToUser(@PathVariable(value = "user_id") long id,
                                 @PathVariable(value = "selRecs") List<Long> ids,
                                 Model model, Authentication authentication) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with ID " + id + " not found"));
        Set<Role> roles = new HashSet<>();
        for (Long i : ids) {
            roles.add(roleRepository.findById(i).orElseThrow(() -> new NotFoundException("Role with ID " + i + " not found")));
        }
        roles.addAll(user.getRoles());
        user.setRoles(roles);
        userRepository.save(user);
        ArrayList<User> res = new ArrayList<>();
        res.add(user);
        model.addAttribute("user", res);
        return "admin/user-details :: user-info";
    }

    @PostMapping("/admin/all-users/user-details/{user_id}/remove-role/{role_id}")
    public String deleteRoleFromUser(@PathVariable(value = "user_id") long u_id,
                                     @PathVariable(value = "role_id") long r_id,
                                     Model model, Authentication authentication) {
        User user = userRepository.findById(u_id).orElseThrow(() -> new NotFoundException("User with ID " + u_id + " not found"));
        Role role = roleRepository.findById(r_id).orElseThrow(() -> new NotFoundException("Role with ID " + r_id + " not found"));
        user.getRoles().remove(role);
        userRepository.save(user);
        ArrayList<User> res = new ArrayList<>();
        res.add(user);
        model.addAttribute("user", res);
        return "admin/user-details :: user-info";
    }

}

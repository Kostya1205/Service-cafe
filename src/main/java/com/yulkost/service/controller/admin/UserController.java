package com.yulkost.service.controller.admin;

import com.yulkost.service.dto.UserEditDto;
import com.yulkost.service.model.User;
import com.yulkost.service.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String Users(Model model) {
        List<User> users = new ArrayList<>();
        userService.findAll().iterator().forEachRemaining(users::add);
        model.addAttribute("form", new UserEditDto(users));
        return "adminUsers"; }

    @PostMapping("/users")
    public String UsersEdit(Model model ,@ModelAttribute UserEditDto form) {
        userService.saveAll(form.getUsers());
        return "redirect:/admin"; }

    @GetMapping("/users/add")
    public String UserAdd(Model model) {
        return "adminUserAdd"; }

    @PostMapping("/users/add")
    public String UsersAdd(Model model ,User user) {
        userService.save(user);
        return "redirect:/admin"; }
}

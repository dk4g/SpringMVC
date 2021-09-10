package com.dk.SpringMVC.controllers;


import com.dk.SpringMVC.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WebController {

    @GetMapping("/list")
    public String getList(Model model) {

        List<User> userList = new ArrayList<>();
        userList.add(new User("Robert", "Smith", "New York"));
        userList.add(new User("Stephen", "Collins", "Washington DC"));

        model.addAttribute("userList", userList);
        return "list";
    }
}

package com.dk.SpringMVC.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/list")
    public String getList() {

        return "list";
    }
}

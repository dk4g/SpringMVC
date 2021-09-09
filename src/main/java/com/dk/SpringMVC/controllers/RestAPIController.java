package com.dk.SpringMVC.controllers;


import com.dk.SpringMVC.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RestAPIController {

    private List<User> userList;

    @GetMapping("/api/users")
    public List<User> sayHello() {
//        final HttpHeaders httpHeaders= new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//        return new ResponseEntity<String>("{\"msg\": \"Hello World\"}", httpHeaders, HttpStatus.OK);

        userList = new ArrayList<>();
        userList.add(new User("Robert", "Smith", "New York"));
        userList.add(new User("Stephen", "Collins", "Washington DC"));

        return userList;
    }
}

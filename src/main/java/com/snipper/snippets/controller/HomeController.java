package com.snipper.snippets.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome to home Page";
    }
    @GetMapping("/secure")
    public String secure(){
        return  "welcome to the secure content";
    }
}

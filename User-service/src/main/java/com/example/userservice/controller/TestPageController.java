package com.example.userservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestPageController {

    @GetMapping("/test")
    public String testPage() {
        // This corresponds to src/main/resources/templates/test.html
        return "redirect:/test.html";
    }
}

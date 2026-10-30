package com.dylej.taskapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TaskController {

    @GetMapping("/profile")
    public String profile() {
        return "Protected endpoint reached";
    }
}
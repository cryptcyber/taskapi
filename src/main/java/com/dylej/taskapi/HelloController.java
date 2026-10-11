package com.dylej.taskapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {


    @GetMapping("/hello")
    public String hello() {
        return "Hello Spring Boot!";
    }

    @GetMapping("/greeting")
    public String greeting() {
        return "Welcome to my first Spring Boot project!";
    }

    @GetMapping("/hello/{name}")
    public String hello(@PathVariable String name) {
    return "Hello " + name + "!";
}
}
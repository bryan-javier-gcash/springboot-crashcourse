package com.example.gcashtrainingspringboot.controller;

import jakarta.servlet.annotation.HandlesTypes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HelloWorldController {
    @Value("${app.greeting.message}")
    private String customMessage;

    @GetMapping("/greet")
    public String greet(){
        return customMessage;
    }

    @GetMapping("/greeting")
    public String greetWithParams(@RequestParam(value = "name", required = false, defaultValue = "Alice")String name){
        return String.format("Hello %s", name);
    }

    @GetMapping("/superhero/{name}")
    public String superhero(@PathVariable String name){
        return String.format("Hello %s", name);
    }

    @GetMapping("/sum")
    public int calculateSum(@RequestParam(value = "firstnumber",required = true) int firstNumber,
                            @RequestParam(value = "secondnumber", required = true) int secondNumber){
        return firstNumber + secondNumber;
    }

    @GetMapping("/information")
    public Map<String, String> getAppInfo(){
        Map<String, String> info = new HashMap<>();
        info.put("appName", "Gcash Springboot");
        info.put("version", "1.0.0");
        info.put("status", "Running");
        return info;
    }

    @GetMapping("/features")
    public List<String> getFeatures(){
        return Arrays.asList("REST API", "Springboot", "Easy setup", "Faster Deployment");
    }
}

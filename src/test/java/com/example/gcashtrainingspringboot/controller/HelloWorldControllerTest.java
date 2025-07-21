package com.example.gcashtrainingspringboot.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(HelloWorldController.class)
class HelloWorldControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGreetWithParameters() throws Exception{
        mockMvc.perform(get("/greeting").param("name", "Alice"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello Alice"));
    }

    @Test
    void testSuperheroWithName() throws Exception{
        mockMvc.perform(get("/superhero/batman"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello batman"));
    }

    @Test
    void testGreet() throws Exception{
        mockMvc.perform(get("/greet"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello from My Springboot App!"));
    }

    @Test
    void testCalculateSum() throws Exception{
        mockMvc.perform(get("/sum").param("firstnumber", "10").param("secondnumber", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("12"));
    }

    @Test
    void testAppInfo() throws Exception{
        mockMvc.perform(get("/information"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.appName").value("Gcash Springboot"))
                .andExpect(jsonPath("$.version").value("1.0.0"))
                .andExpect(jsonPath("$.status").value("Running"));
    }
}
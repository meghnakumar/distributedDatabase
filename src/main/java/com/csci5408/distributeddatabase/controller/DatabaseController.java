package com.csci5408.distributeddatabase.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DatabaseController
{
    @GetMapping("/")
    public String homePage()
    {
        System.err.println("get api called");
        return "Distributed databse application";
    }
}

package com.csci5408.distributeddatabase.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatabaseController
{
    @GetMapping("/")
    public String homePage()
    {
        System.err.println("get api called");
        return "Distributed databse application";
    }

    @GetMapping("/home")
    public String indexPage()
    {
        System.err.println("index api called");
        return "Index API return";
    }
}

package com.csci5408.distributeddatabase.controller;

import com.csci5408.distributeddatabase.globalmetadatahandler.GlobalMetadataHandler;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.AsyncRestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

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

    @GetMapping("/test")
    public String redirectTest()
    {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = "http://localhost:8080/home";
        ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl, String.class);
        return response.getBody();
    }

    @GetMapping("/getGlobalMetaDataProp")
    public Properties getGlobalMetaData()
    {
        GlobalMetadataHandler globalMetadataHandler = new GlobalMetadataHandler();
        return globalMetadataHandler.getGlobalMetadataProperties();
    }

    @PostMapping("/redirectUpdateGlobalMetaDataProp")
    public Properties redirectUpdateGlobalMetadataProp(@RequestParam  String propName, @RequestParam  String propValue)
    {
        System.err.println("received value= "+propName+" _ "+propValue);
        GlobalMetadataHandler globalMetadataHandler = new GlobalMetadataHandler();
        globalMetadataHandler.writeToMetaData(propName, propValue);
        return globalMetadataHandler.getGlobalMetadataProperties();
    }

    @PostMapping("/updateGlobalMetaDataProp")
    public String updateGlobalMetadataProp(@RequestParam  String propName, @RequestParam  String propValue)
    {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/redirectUpdateGlobalMetaDataProp";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("propName", propName);
        map.add("propValue", propValue);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );

        return response.getBody();
    }
}

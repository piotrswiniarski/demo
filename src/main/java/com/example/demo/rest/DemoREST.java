package com.example.demo.rest;

import com.example.demo.service.RomanNumeralsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class DemoREST {
    @Autowired
    RomanNumeralsService romanNumeralsUtil;

    private static final Logger log = LoggerFactory.getLogger(DemoREST.class);
    @GetMapping("/status")
    String status() {
        return "up";
    }

    @PostMapping("/convert")
    ResponseEntity<Object> newRL(@RequestBody String input){
        return romanNumeralsUtil.convertToNumber(input);
    }

}

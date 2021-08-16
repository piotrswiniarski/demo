package com.example.demo.rest;

import com.example.demo.service.RomanNumeralsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    Integer newRL(@RequestBody String input) throws Exception{
        return romanNumeralsUtil.convertToNumber(input);
    }

}

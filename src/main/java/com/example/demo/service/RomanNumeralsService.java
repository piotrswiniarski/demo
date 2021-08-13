package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
@Service
public class RomanNumeralsService {
    private static final Logger log = LoggerFactory.getLogger(RomanNumeralsService.class);

    @Value("#{${roman.numerals}}")
    private Map<Character, Integer> romanNumerals;
    public Integer convertToNumber(String input){
        validateInput(input);

        return getValueFromString(input);
    }

    public int getValueFromString(String input) {
        int result=0;
        for(int i=0;i<input.length();i++)
        {
            char ch = input.charAt(i);
            if(i>0 && romanNumerals.get(ch) > romanNumerals.get(input.charAt(i-1))) {
                result += romanNumerals.get(ch) - 2*romanNumerals.get(input.charAt(i-1));
            } else {
                result += romanNumerals.get(ch);
            }
        }

        return result;
    }

    private static void validateInput(String input) {
        log.info(input);
    }
}

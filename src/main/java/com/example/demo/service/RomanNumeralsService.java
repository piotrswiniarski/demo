package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RomanNumeralsService {
    private static final Logger log = LoggerFactory.getLogger(RomanNumeralsService.class);

    @Value("#{${roman.numerals}}")
    private Map<Character, Integer> romanNumerals;

    @Value("#{${max.char.sequence.count}}")
    private Map<Character, Integer> additionCountAllowed;

    @Value("#{${substraction.prechars.allowed}}")
    private Map<Character, String> substractionPrecharsAllowed;

    public Integer convertToNumber(String input) throws Exception {
        validateInput(input.toCharArray());
        return getValueFromString(input);
    }
    public int getValueFromString(String input) {
        int result = 0;
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if (i > 0 && romanNumerals.get(ch) > romanNumerals.get(input.charAt(i - 1))) {
                result += romanNumerals.get(ch) - 2 * romanNumerals.get(input.charAt(i - 1));
            } else {
                result += romanNumerals.get(ch);
            }
        }

        return result;
    }

    private void validateInput(char[] charArray) throws Exception {
        validateInputChars(charArray);
        validateMaxCharSequenceCount(charArray);
        validateSubstractionPrecharsAllowed(charArray);
    }

    private void validateSubstractionPrecharsAllowed(char[] charArray) throws Exception {
        int inputLength = charArray.length;
        int count = 1;
        for (int i = 1; i < inputLength; i++) {
            if (substractionPrecharsAllowed.get(charArray[i - 1]).indexOf(charArray[i])>-1) {
                continue;
            } else {
                throw new Exception();
            }
        }
    }

    private void validateInputChars(char[] charArray) throws NullPointerException {
        for (char c : charArray) {
            romanNumerals.get(c);
        }
    }

    private void validateMaxCharSequenceCount(char[] charArray) throws Exception {
        int inputLength = charArray.length;
        if (inputLength < 2) {
            return;
        }
        int count = 1;
        for (int i = 1; i < inputLength; i++) {
            if (charArray[i] == charArray[i - 1]) {
                count++;
                if (count > additionCountAllowed.get(charArray[i])) {
                    throw new Exception();
                }
            } else {
                count = 0;
            }
        }
    }

}

package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RomanNumeralsService {
    private static final Logger log = LoggerFactory.getLogger(RomanNumeralsService.class);

    @Value("#{${roman.numerals}}")
    private Map<Character, Integer> romanNumerals;

    @Value("#{${max.char.sequence.count}}")
    private Map<Character, Integer> additionCountAllowed;

    @Value("#{${substraction.prechars.allowed}}")
    private Map<Character, String> substractionPrecharsAllowed;

    @Value("${forbidden.substraction.sequences}")
    private String[] forbiddenSequences;

    public ResponseEntity<Object> convertToNumber(String input) {
        char[] charArray = input.toCharArray();
        try {
            validateInputChars(charArray);
            validateMaxCharSequenceCount(charArray);
            validateSubstractionPrecharsAllowed(charArray);
            validateForbiddenSequence(input);
        } catch (IllegalArgumentException illegalArgumentException) {
            return new ResponseEntity<>(
                    illegalArgumentException.getMessage(), HttpStatus.BAD_REQUEST);
        }

        int result = 0;

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;

        while ((input.length() > 0) && (i < romanNumerals.size())) {
            RomanNumeral symbol = romanNumerals.get(i);
            if (input.startsWith(symbol.name())) {
                result += symbol.getValue();
                input = input.substring(symbol.name().length());
            } else {
                i++;
            }
        }


        return new ResponseEntity<Object>(
                result, HttpStatus.OK);
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

    private void validateInput(char[] charArray) {
        try {
            validateInputChars(charArray);
            validateMaxCharSequenceCount(charArray);
            validateSubstractionPrecharsAllowed(charArray);
        } catch (Exception illegalArgumentException) {
            System.out.println(illegalArgumentException);
        }
    }

    public void validateInputChars(char[] charArray) {
        for (char c : charArray) {
            if (romanNumerals.get(c) == null) {
                throw new IllegalArgumentException("Provided input does not consist of allowed Roman Numbers.");
            }
        }
    }

    public void validateMaxCharSequenceCount(char[] charArray) {
        int inputLength = charArray.length;
        if (inputLength < 2) {
            return;
        }
        int count = 1;
        for (int i = 1; i < inputLength; i++) {
            if (charArray[i] == charArray[i - 1]) {
                count++;
                if (count > additionCountAllowed.get(charArray[i])) {
                    throw new IllegalArgumentException("Exceeded max characters in sequence.");
                }
            } else {
                count = 1;
            }
        }
    }

    public void validateSubstractionPrecharsAllowed(char[] charArray) {
        int inputLength = charArray.length;
        int count = 1;
        for (int i = 1; i < inputLength; i++) {
            if (romanNumerals.get(charArray[i]) > romanNumerals.get(charArray[i - 1])) {
                if (substractionPrecharsAllowed.get(charArray[i - 1]).indexOf(charArray[i]) == -1 ||
                        (i > 1 && romanNumerals.get(charArray[i - 1]) == romanNumerals.get(charArray[i - 2])) ||
                        (i < (inputLength - 1) && romanNumerals.get(charArray[i]) == romanNumerals.get(charArray[i + 1]))) {
                    throw new IllegalArgumentException("Provided input is not valid for substraction.");
                }
            }
        }
    }

    public void validateForbiddenSequence(String input) {
        for (String forbiddenSequence : forbiddenSequences) {
            if (input.contains(forbiddenSequence)) {
                throw new IllegalArgumentException("Provided input is not valid for substraction.");
            }
        }
    }

    enum RomanNumeral {
        I(1), IV(4), V(5), IX(9), X(10),
        XL(40), L(50), XC(90), C(100),
        CD(400), D(500), CM(900), M(1000);

        private int value;

        RomanNumeral(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static List<RomanNumeral> getReverseSortedValues() {
            return Arrays.stream(values())
                    .sorted(Comparator.comparing((RomanNumeral e) -> e.value).reversed())
                    .collect(Collectors.toList());
        }
    }
}

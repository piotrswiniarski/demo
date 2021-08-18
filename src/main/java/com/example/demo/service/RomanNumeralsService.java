package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

    public Integer convertToNumber(String input) throws Exception {
        validateInput(input.toCharArray());

        String romanNumeral = input.toUpperCase();
        int result = 0;

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;

        while ((romanNumeral.length() > 0) && (i < romanNumerals.size())) {
            RomanNumeral symbol = romanNumerals.get(i);
            if (romanNumeral.startsWith(symbol.name())) {
                result += symbol.getValue();
                romanNumeral = romanNumeral.substring(symbol.name().length());
            } else {
                i++;
            }
        }

        if (romanNumeral.length() > 0) {
            throw new IllegalArgumentException(input + " cannot be converted to a Roman Numeral");
        }

        return result;
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
        for (int i = 1; i <inputLength; i++) {
            if (romanNumerals.get(charArray[i]) > romanNumerals.get(charArray[i-1])) {
                if (substractionPrecharsAllowed.get(charArray[i - 1]).indexOf(charArray[i])==-1) {
                    throw new Exception();
                }
            }
        }
    }

    private void validateInputChars(char[] charArray) throws NullPointerException {
        for (char c : charArray) {
            romanNumerals.get(c);
        }
    }

    public void validateMaxCharSequenceCount(char[] charArray) throws Exception {
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

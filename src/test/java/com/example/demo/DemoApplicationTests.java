package com.example.demo;

import com.example.demo.service.RomanNumeralsService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;

import java.util.Iterator;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@TestPropertySources({
        @TestPropertySource("classpath:romanNumbers.yaml"),
        @TestPropertySource("classpath:invalidInput.yaml"),
})
class DemoApplicationTests {
    @Autowired
    RomanNumeralsService romanNumeralsService;

    @Value("#{${test.set1}}")
    public Map<Integer, String> set1;
    @Value("#{${test.set2}}")
    public Map<Integer, String> set2;
    @Value("#{${test.set3}}")
    public Map<Integer, String> set3;
    @Value("#{${test.set4}}")
    public Map<Integer, String> set4;

    @Value("#{${invalid.chars}}")
    public String[] invalidChars;

    @Value("#{${invalid.char.sequence.count}}")
    public String[] invalidCharSequenceCount;

    @Value("#{${invalid.substraction.sequences}}")
    public String[] invalidSubstractionSequences;

    @Value("#{${forbidden.sequences}}")
    public String[] forbiddenSequences;

    @Test
    public void givenMaxCharSequenceCount_ReturnErrorForWrongInput() {
        for (String charSequence : invalidCharSequenceCount) {
            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                    () -> {
                        romanNumeralsService.validateMaxCharSequenceCount(charSequence.toCharArray());
                    });
            assertTrue(illegalArgumentException.getMessage().equals("Exceeded max characters in sequence."));
        }
    }

    @Test
    public void givenValidInputCharacters_ReturnErrorForWrongInput() {
        for (String invalidChar : invalidChars) {
            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                    () -> {
                        romanNumeralsService.validateInputChars(invalidChar.toCharArray());
                    });
            assertTrue(illegalArgumentException.getMessage().equals("Provided input does not consist of allowed Roman Numbers."));
        }
    }

    @Test
    public void givenValidSubstractionPrechars_ReturnErrorForWrongInput() {
        for (String invalidSubstractionSequence : invalidSubstractionSequences) {
            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                    () -> {
                        romanNumeralsService.validateSubstractionPrecharsAllowed(invalidSubstractionSequence.toCharArray());
                    });
            assertTrue(illegalArgumentException.getMessage().equals("Provided input is not valid for substraction."));
        }
    }

    @Test
    public void givenForbiddenSequence_ReturnErrorForWrongInput() {
        for (String forbiddenSequence : forbiddenSequences) {
            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                    () -> {
                        romanNumeralsService.validateForbiddenSequence(forbiddenSequence);
                    });
            assertTrue(illegalArgumentException.getMessage().equals("Provided input is not valid for substraction."));
        }
    }

    @Test
    public void givenTestSets_ReturnProperValues() throws Exception {
        validateTestSet(set1);
        validateTestSet(set2);
        validateTestSet(set3);
        validateTestSet(set4);
    }

    private void validateTestSet(Map<Integer, String> testData) throws Exception {
        Iterator it = testData.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            assertThat(pair.getKey()).isEqualTo(romanNumeralsService.convertToNumber((String) pair.getValue()).getBody());
        }
    }
}
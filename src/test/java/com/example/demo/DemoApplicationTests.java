package com.example.demo;

import com.example.demo.service.RomanNumeralsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
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

	@Value("#{${invalid.char.sequence.count}}")
	public String[] invalidCharSequenceCount;

	@Test
	public void givenMaxCharSequenceCount_ReturnErrorForWrongInput() throws Exception {
		for (String charSequence: invalidCharSequenceCount){
			romanNumeralsService.validateMaxCharSequenceCount(charSequence.toCharArray());
		}
	}

	@Test
	public void givenTestSets_ReturnPropreValues() throws Exception {
		validateTestSet(set1);
		validateTestSet(set2);
		validateTestSet(set3);
		validateTestSet(set4);
	}

	private void validateTestSet(Map<Integer, String> testData) throws Exception {
		Iterator it = testData.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			assertThat(pair.getKey()).isEqualTo(romanNumeralsService.convertToNumber((String) pair.getValue()));
		}
	}
}
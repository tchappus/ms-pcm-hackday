package com.myapp.writer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
public class PaymentWriterAppTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void sampleTest() {
		assertTrue("Sample test", true);
	}
}
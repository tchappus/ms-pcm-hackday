package com.myapp.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
public class PaymentApiTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void sampleTest() {
		assertTrue("Sample test", true);
	}
}
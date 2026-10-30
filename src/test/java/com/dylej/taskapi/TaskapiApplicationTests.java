package com.dylej.taskapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"jwt.secret=test-secret-key-that-is-at-least-32-bytes-long",
		"jwt.expiration=60000"
})
class TaskapiApplicationTests {

	@Test
	void contextLoads() {
	}

}

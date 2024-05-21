package com.es.examen;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ExamenApplicationTests {

	@Test
	void contextLoads() {
		Long id = 1L;
		assertEquals(id, 1L);
	}

}

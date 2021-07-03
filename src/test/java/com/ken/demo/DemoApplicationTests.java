package com.ken.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class DemoApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void putIllegalUserTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
				.put("/user")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content("name=ken%"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void getUserTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.
				get("/user"))
				.andExpect(status().isOk())
				.andExpect(content().string("KEN"));
	}

	@Test
	public void contextLoads() {
	}

}

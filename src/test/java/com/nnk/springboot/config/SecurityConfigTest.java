package com.nnk.springboot.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void unauthenticatedUser_AccessProtectedUrl_RedirectToLogin() throws Exception{
		mockMvc.perform(get("/bidList/list"))
		.andExpect(status().isFound())
		.andExpect(redirectedUrlPattern("**/login"));
	}
	
	@Test
	@WithMockUser(username="user", roles= {"USER"})
	public void authenticatedUser_AccessProtectedUrl_GrantsAccess() throws Exception {
		mockMvc.perform(get("/bidList/list"))
		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(username="user", roles= {"USER"})
	public void userUser_AccessAdminUrl_Forbidden() throws Exception {
		mockMvc.perform(get("/admin"))
		.andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUser(username="user", roles= {"USER"})
	public void publicUrl_permitAll() throws Exception {
		mockMvc.perform(get("/login")).andExpect(status().isOk());
	}
}

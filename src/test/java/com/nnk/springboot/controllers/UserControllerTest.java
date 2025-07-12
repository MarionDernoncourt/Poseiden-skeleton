package com.nnk.springboot.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.nnk.springboot.controllers.UserController;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.service.UserService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testGetAllUsers_WhenSuccess() throws Exception {
		List<User> users = List.of(new User(1, "FullName", null, "Username", "User"));

		when(userService.getAllUsers()).thenReturn(users);

		mockMvc.perform(get("/user/list")).andExpect(status().isOk()).andExpect(view().name("user/list"))
				.andExpect(model().attributeExists("users")).andExpect(model().attribute("users", users));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testGetAllUsers_WhenException() throws Exception {
		when(userService.getAllUsers()).thenThrow(new RuntimeException("Erreur lors de la récupération des users"));

		mockMvc.perform(get("/user/list")).andExpect(status().isOk()).andExpect(view().name("user/list"))
				.andExpect(model().attributeExists("error"))
				.andExpect(model().attribute("error", "Erreur lors de la récupération des users"));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testSaveUser_WhenSuccess() throws Exception {
		User newUser = new User();
		newUser.setFullname("fullname");
		newUser.setPassword("Password123@");
		newUser.setUsername("username");
		newUser.setRole("user");

		when(userService.saveUser(any(User.class))).thenReturn(newUser);

		mockMvc.perform(post("/user/validate").with(csrf()).param("fullname", "fullname")
				.param("password", "Password123@").param("username", "username").param("role", "user"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/user/list"));
	}

	@Test
	@WithMockUser(username = "user", roles = {"USER"})
	public void testSaveUser_WhenResultHasErrors() throws Exception {
		User newUser = new User();
		newUser.setFullname("fullname");
		newUser.setPassword("");
		newUser.setUsername("username");
		newUser.setRole("user");

		when(userService.saveUser(any(User.class))).thenReturn(newUser);

		mockMvc.perform(post("/user/validate").with(csrf()).param("fullname", "fullname").param("password", "")
				.param("username", "username").param("role", "user")).andExpect(status().isOk())
				.andExpect(view().name("user/add")).andExpect(model().attributeExists("user"))
				.andExpect(model().attribute("user", newUser));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testSaveUser_WhenException() throws Exception {
		when(userService.saveUser(any(User.class))).thenThrow(new RuntimeException("Echec lors de l'ajout du user"));

		mockMvc.perform(post("/user/validate").with(csrf()).param("fullname", "fullname")
				.param("password", "Password123@").param("username", "username").param("role", "user"))
				.andExpect(status().isOk()).andExpect(view().name("user/add"))
				.andExpect(model().attributeExists("error"))
				.andExpect(model().attribute("error", "Echec lors de l'ajout du user"));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testShowUpdateForm_WhenSuccess() throws Exception {
		User user = new User();
		user.setFullname("fullname");
		user.setPassword("password123@");
		user.setUsername("username");
		user.setRole("user");

		when(userService.getUserById(anyInt())).thenReturn(user);

		mockMvc.perform(get("/user/update/1").with(csrf()).param("fullname", "fullname").param("password", "")
				.param("username", "username").param("role", "user")).andExpect(status().isOk())
				.andExpect(view().name("user/update")).andExpect(model().attributeExists("user"))
				.andExpect(model().attribute("user", hasProperty("password", is(""))));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testShowUpdateForm_WhenException() throws Exception {

		when(userService.getUserById(anyInt()))
				.thenThrow(new RuntimeException("Erreur lors de la récupération du user"));

		mockMvc.perform(get("/user/update/1").with(csrf())).andExpect(status().isOk())
				.andExpect(view().name("user/list")).andExpect(model().attributeExists("error"))
				.andExpect(model().attribute("error", "Erreur lors de la récupération du user"));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testUpdateUserById_WhenSuccess() throws Exception {
		User user = new User();
		user.setFullname("newFullname");
		user.setPassword("newPassword123@");
		user.setUsername("newUsername");
		user.setRole("user");

		when(userService.updateUserById(anyInt(), any(User.class))).thenReturn(user);

		mockMvc.perform(post("/user/update/1").with(csrf()).param("fullname", "newFullname")
				.param("password", "newPassword123@").param("username", "newUsername").param("role", "user"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/user/list"));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testUpdateUserById_WhenResultHasError() throws Exception {
		User user = new User();
		user.setId(1);
		user.setFullname("newFullname");
		user.setPassword("newPassword");
		user.setUsername("newUsername");
		user.setRole("user");

		when(userService.updateUserById(anyInt(), any(User.class))).thenReturn(user);

		mockMvc.perform(post("/user/update/1").with(csrf()).param("fullname", "newFullname")
				.param("password", "newPassword").param("username", "newUsername").param("role", "user"))
				.andExpect(status().isOk()).andExpect(view().name("user/update"))
				.andExpect(model().attributeExists("user")).andExpect(model().attribute("user", user));
	}

	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void testUpdateUserById_WhenException() throws Exception {
		when(userService.updateUserById(anyInt(), any(User.class)))
				.thenThrow(new RuntimeException("Erreur lors de la mise à jour du user"));

		mockMvc.perform(post("/user/update/1").with(csrf()).param("fullname", "newFullname")
				.param("password", "newPassword123@").param("username", "newUsername").param("role", "user"))
				.andExpect(status().isOk()).andExpect(view().name("user/update"))
				.andExpect(model().attributeExists("error"))
				.andExpect(model().attribute("error", "Erreur lors de la mise à jour du user"));
	}
	
	@Test
	@WithMockUser(username="user", roles= {"USER"})
	public void testDeleteUserById_WhenSuccess() throws Exception {
		doNothing().when(userService).deleteUserById(anyInt());
		
		mockMvc.perform(get("/user/delete/1")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/user/list"));
	}
	
	@Test
	@WithMockUser(username="user", roles= {"USER"})
	public void testDeleteUserById_WhenException() throws Exception {
		doThrow(new RuntimeException("Erreur lors de la suppression du user")).when(userService).deleteUserById(anyInt());
		
		mockMvc.perform(get("/user/delete/1")).andExpect(status().isOk()).andExpect(view().name("user/list"))
		.andExpect(model().attributeExists("error")).andExpect(model().attribute("error", "Erreur lors de la suppression du user"));
	}
	}

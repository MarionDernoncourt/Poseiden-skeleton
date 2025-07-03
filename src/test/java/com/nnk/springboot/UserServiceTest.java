package com.nnk.springboot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;

@SpringBootTest
public class UserServiceTest {

	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void userTest() {
		User user = new User(1, "userName", "fullName", "Password123@", "user");
		
		//save
		user = userRepository.save(user);
		assertNotNull(user.getId());
		assertEquals("userName", user.getUsername());
		
		//Update
		user.setFullname("MonNom");
		user = userRepository.save(user);
		assertEquals("MonNom", user.getFullname());
		
		//Find
		List<User> listUser = userRepository.findAll();
		assertTrue(listUser.size() > 0);
		
		//Delete
		Integer id = user.getId();
		userRepository.delete(user);
		Optional<User> userList = userRepository.findById(id);
		assertFalse(userList.isPresent());
		}
}


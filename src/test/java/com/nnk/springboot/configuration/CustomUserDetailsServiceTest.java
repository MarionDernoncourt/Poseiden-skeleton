package com.nnk.springboot.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.nnk.springboot.configuration.CustomUserDetailsService;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	private CustomUserDetailsService customUserDetailsService;
	
	@Test
	public void loadUserByUsername_WhenSuccess() {
		User user = new User();
		user.setUsername("username");
		user.setPassword("$2a$12$CrWSXAh1CwnjxTfd/yoye.iqXrrXeZJlXmxyZ4qVFkrfdclQhGqBy");
		user.setRole("ADMIN");
		
		when(userRepository.findByUsername(anyString())).thenReturn(user);
		
		UserDetails userDetails = customUserDetailsService.loadUserByUsername("username");
		
		assertNotNull(userDetails);
		assertEquals("username", userDetails.getUsername());
		assertEquals("$2a$12$CrWSXAh1CwnjxTfd/yoye.iqXrrXeZJlXmxyZ4qVFkrfdclQhGqBy", userDetails.getPassword());
		assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
	}
	
	@Test
	public void loadUserDetailsService_WhenException() {
		when(userRepository.findByUsername(anyString())).thenReturn(null);
		
		assertThrows(UsernameNotFoundException.class, () -> {
			customUserDetailsService.loadUserByUsername("nonExistantUser");
		});
	}
}

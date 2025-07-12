package com.nnk.springboot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.utils.PasswordUtils;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordUtils passwordUtils;

	@InjectMocks
	private UserService userService;

	private User user;

	@BeforeEach
	void setUp() {

		user = new User(1, "userName", "fullName", "Password123@", "user");
	}

	@Test
	public void testGetAllUser_WhenSuccess() {
		List<User> users = new ArrayList<>();
		users.add(user);

		when(userRepository.findAll()).thenReturn(users);

		List<User> actualUsers = userService.getAllUsers();

		assertEquals(users, actualUsers);
		assertEquals(users.size(), actualUsers.size());
		verify(userRepository, times(1)).findAll();
	}

	@Test
	public void testSaveUser_WhenSuccess() {
		when(userRepository.save(any(User.class))).thenReturn(user);

		User savedUser = userService.saveUser(user);

		assertEquals(user.getFullname(), savedUser.getFullname());
		assertEquals(user.getUsername(), savedUser.getUsername());
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	public void testSaveUser_WhenException() {
		doThrow(new RuntimeException("Erreur lors de la sauvegarde")).when(userRepository).save(any(User.class));

		RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
			userService.saveUser(user);
		});
		assertEquals("Erreur lors de la sauvegarde", thrown.getMessage());
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	public void testGetUserById_WhenSuccess() {
		when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

		User foundUser = userService.getUserById(1);

		assertEquals(user, foundUser);
		verify(userRepository, times(1)).findById(anyInt());
	}

	@Test
	public void tesGetUserById_WhenNotFound() {
		when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class, () -> {
			userService.getUserById(5);
		});
		verify(userRepository, times(1)).findById(anyInt());
	}

	@Test
	public void testGetUserById_WhenException() {
		doThrow(new RuntimeException("Erreur lors de la récupération du user")).when(userRepository).findById(anyInt());

		assertThrows(RuntimeException.class, () -> {
			userService.getUserById(1);
		});
		verify(userRepository, times(1)).findById(anyInt());
	}

	@Test
	public void testUpdateUser_WhenSuccess() {
		when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

		User newUserData = new User();
		newUserData.setFullname("newFullName");
		newUserData.setUsername("newUsername");

		User expectedSavedUser = new User();
		expectedSavedUser.setFullname(user.getFullname());
		expectedSavedUser.setUsername(user.getUsername());
		expectedSavedUser.setId(user.getId());

		when(userRepository.save(any(User.class))).thenReturn(expectedSavedUser);

		User actualUpdatedUser = userService.updateUserById(user.getId(), newUserData);

		assertEquals(expectedSavedUser, actualUpdatedUser);
		verify(userRepository, times(1)).findById(anyInt());
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	public void testUpdateUser_WhenExceptionDuringSave() {
		when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

		User newUserData = new User();
		newUserData.setFullname("newFullName");
		newUserData.setUsername("newUsername");

		doThrow(new RuntimeException("Erreur lors de la sauvegarde de la mise à jour")).when(userRepository)
				.save(any(User.class));

		assertThrows(RuntimeException.class, () -> {
			userService.updateUserById(user.getId(), newUserData);
		});

		verify(userRepository, times(1)).findById(anyInt());
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	public void testUpdateUser_WhenNotFound() {
		when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

		User newUserData = new User();
		newUserData.setFullname("newFullName");
		newUserData.setUsername("newUsername");

		assertThrows(IllegalArgumentException.class, () -> {
			userService.updateUserById(user.getId(), newUserData);
		});

		verify(userRepository, times(1)).findById(anyInt());
		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	public void testDeleteUser_WhenSuccess() {
		when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

		userService.deleteUserById(1);

		verify(userRepository, times(1)).delete(any(User.class));
	}

	@Test
	public void testDeleteUser_WhenNotFound() {
		when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class, () -> {
			userService.deleteUserById(5);
		});
		verify(userRepository, times(1)).findById(anyInt());
		verify(userRepository, never()).delete(any(User.class));
	}
	
	@Test
    public void testUserGettersAndSetters() {
        User user = new User();

        user.setId(1);
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setFullname("Test User");
        user.setRole("ADMIN");

        assertEquals(1, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("Test User", user.getFullname());
        assertEquals("ADMIN", user.getRole());
    }
	
	 @Test
	    public void testUserEqualsAndHashCode() {
	        User user1 = new User(1, "user1", "pass1", "Full Name 1", "ADMIN");
	        User user2 = new User(1, "user1", "pass1", "Full Name 1", "ADMIN");
	        User user3 = new User(2, "user2", "pass2", "Full Name 2", "USER"); // Different ID

	        assertEquals(user1, user2);
	        assertNotEquals(user1, user3);
	        
	        assertEquals(user1.hashCode(), user2.hashCode());
	        assertNotEquals(user1.hashCode(), user3.hashCode()); 
	    }
}

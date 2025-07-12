package com.nnk.springboot.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.nnk.springboot.utils.PasswordUtils;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;

@Service
public class UserService {

	private final static Logger logger = LoggerFactory.getLogger(UserService.class);

	private final UserRepository userRepository;
	private final PasswordUtils passwordUtils;

	public UserService(UserRepository userRepository, PasswordUtils passwordUtils) {
		this.userRepository = userRepository;
		this.passwordUtils = passwordUtils;
	}

	public List<User> getAllUsers() {
		logger.info("Tentative de récupération de tous les users");

		List<User> users = userRepository.findAll();

		logger.info("Nombre de users trouvés : {}", users.size());
		return users;
	}

	public User saveUser(User user) {
		logger.info("Tentative de sauvegarde d'un user");

		User newUser = new User();
		newUser.setFullname(user.getFullname());
		newUser.setUsername(user.getUsername());
		if (user.getPassword() != null && !user.getPassword().isEmpty()) {
			newUser.setPassword(passwordUtils.hashPassword(user.getPassword()));
		} else {
			logger.warn("Le mot de passe n'est pas valide");
			throw new IllegalArgumentException("Mot de passe invalide");
		}
		newUser.setRole(user.getRole());

		User savedUser = userRepository.save(newUser);
		logger.info("L'utilisateur {} a bien été sauvegardé", savedUser.getFullname());

		return savedUser;
	}

	public User getUserById(Integer id) {
		logger.info("Tentative de récupération du user id {}", id);

		User user = userRepository.findById(id).orElseThrow(() -> {
			logger.warn("Aucun utilisateur trouvé avec l'id {}", id);
			return new IllegalArgumentException("Aucun utilisateur trouvé avec l'id " + id);
		});

		logger.info("Le user id {} a bien été trouvé", id);
		return user;
	}

	public User updateUserById(Integer id, User user) {
		logger.info("Tentative de mise à jour du user id {}", id);

		User userToUpdate = userRepository.findById(id).orElseThrow(() -> {
			logger.warn("Aucun user trouvé avec l'id {}", id);
			return new IllegalArgumentException("Aucun user trouvé");
		});

		userToUpdate.setFullname(user.getFullname());
		userToUpdate.setUsername(user.getUsername());
		if (user.getPassword() != null && !user.getPassword().isEmpty()) {
			userToUpdate.setPassword(passwordUtils.hashPassword(user.getPassword()));
		}
		userToUpdate.setRole(user.getRole());

		User updatedUser = userRepository.save(userToUpdate);
		logger.info("Le profil du user id {} a bien été mis à jour", id);

		return updatedUser;
	}

	public void deleteUserById(Integer id) {
		logger.info("Tentative de suppression du user id {}", id);

		User userToDelete = userRepository.findById(id).orElseThrow(() -> {
			logger.warn("Echec de la suppression: aucun user id {}", id);
			return new IllegalArgumentException("Aucun user trouvé");
		});
		userRepository.delete(userToDelete);
		logger.info("La suppression du user id {} a bien été effectuée", id);
	}

}

package com.nnk.springboot.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service utilitaire pour le hachage et la vérification des mots de passe
 * utilisant {@link BCryptPasswordEncoder} de SpringSecurity
 */

@Service
public class PasswordUtils {

	private final BCryptPasswordEncoder encoder;

	public PasswordUtils(BCryptPasswordEncoder encoder) {
		this.encoder = encoder;
	}

	/**
	 * Methode pour hacher un mot de passe brut en utilisant {@link BCryptPasswordEncoder}.
	 * 
	 * @param rawPassword -> le mot de passe a encoder
	 * @return -> password encodé (haché)
	 */
	public String hashPassword(String rawPassword) {
		return encoder.encode(rawPassword);
	}

	/**
	 * Verifie si un mot de passe en clair correspond à un mot de passe haché
	 * en utilisant {@link BCryptPasswordEncoder}
	 * 
	 * @param rawPassword le mot de passe en clair saisi par l'utilisateur
	 * @param hashedPassword le mot de passe encodé stocké en bdd
	 * @return {@code true} si le mot de passe correspond, {@code false} sinon
	 */
	public boolean verifyPassword(String rawPassword, String hashedPassword) {
		return encoder.matches(rawPassword, hashedPassword);
	}

	
}

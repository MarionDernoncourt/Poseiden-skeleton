package com.nnk.springboot.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nnk.springboot.repositories.UserRepository;

/**
 * Implémentation personnalisée de l'interface {@link UserDetailsService}.
 * 
 * Cette classe est utilisée par SpringSecurity pour charger les détails d'un
 * utilisateur (nom d'utilisateur, mot de passe et rôle) à partir de la base de
 * données lors de l'authentification.
 * 
 * Elle récupère un utilisateur via son nom d'utilisateur via le
 * {@link UserRepository} et convertit son rôle en une liste d'autorisations
 * reconnues par SpringSecurity.
 * 
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {

	 @Autowired
	private UserRepository userRepository;
	
	

	/**
	 * Charge un utilisateur à partir de son nom d'utilisateur (username).
	 * 
	 * @param username, le nom d'utilisateur fourni lors de la tentative de
	 *                  connexion
	 * @return un objet {@link UserDetails} contenant le nom d'utilisateur, le mot
	 *         de passe et les autorités de sécurité de l'utilisateur
	 * @throws UsernameNotFoundException si aucun utilisateur correspondant n'est
	 *                                   trouvé
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		com.nnk.springboot.domain.User user = userRepository.findByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException("Utilisateur non trouvé :" + username);
		}
		return new User(user.getUsername(), user.getPassword(), getGrantedAuthorities(user.getRole()));
	}

	/**
	 * Transforme un rôle utilisateur en une liste d'autorités Spring Security
	 * 
	 * @param role, le rôle de l'utilisateur tel qu'enregistré dans la base de
	 *              données
	 * @return une liste contenant une seule autorité avec le préfixe "ROLE_"
	 */
	private List<GrantedAuthority> getGrantedAuthorities(String role) {

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		authorities.add(new SimpleGrantedAuthority("ROLE_" + role));

		return authorities;
	}

}

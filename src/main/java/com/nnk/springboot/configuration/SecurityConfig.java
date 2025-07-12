package com.nnk.springboot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration de la sécurité globale pour l'application. Cette classe met en
 * place les règles de sécurité (autorisation des URL, connexion par formulaire
 * cmportement déconnexion et l'encodage des mots de passe.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled=true)
public class SecurityConfig {

	/**
	 * 
	 * Configure la chaines de filtres de sécurité pour les requêtes HTTP. Définit
	 * les règles d'accès aux URL, l'authentification basée sur les formulaires et
	 * le comportement de déconnexion
	 * 
	 * @param http, l'objet HttpSecurity à configurer
	 * @return Une instance de SecurityFilterChain qui définit la chaine de filtres de sécurité
	 * @throws Exception Si une erreur survient pendant la configuration
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/login").anonymous()
						.requestMatchers("/", "/css/**", "/js/**").permitAll()
						.requestMatchers("/admin/**").hasRole("ADMIN")
						.anyRequest().hasAnyRole("USER", "ADMIN"))
				.formLogin(form -> form.defaultSuccessUrl("/bidList/list", true).permitAll())
				.logout(logout -> logout.logoutSuccessUrl("/login").permitAll())
				.exceptionHandling(exceptions -> exceptions
						.accessDeniedPage("/access-denied"));

		return http.build();
	}

	/**
	 * Fournit un bean {@link BCryptPasswordEncoder} pour encoder les mot de passe
	 * 
	 * Ce bean sera utilisé pour le hachage des mots de passe, garantissant la
	 * sécurité lors de l'enregistrement et la vérificaton.
	 * 
	 * @return une instance de {@link BCryptPasswordEncoder}
	 */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}

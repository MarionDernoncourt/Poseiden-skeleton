package com.nnk.springboot.validations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Annotation personnalisée de validation conditionnelle pour un mot de passe.
 *
 * Cette contrainte permet de valider un mot de passe uniquement s'il est
 * présent (non vide). Elle est utile notamment lors d'une mise à jour, où
 * l'utilisateur peut choisir de ne pas modifier son mot de passe. Si le champ
 * est vide, la validation passe. Sinon, la valeur doit respecter le format
 * requis : au moins 8 caractères, une majuscule, un chiffre et un caractère
 * spécial.
 *
 *
 * Utilisation : {@code @ValidPasswordIfPresent private String password; }
 *
 * Exemple avec groupes de validation : {@code @ValidPasswordIfPresent(groups =
 * ValidationGroup.OnUpdate.class) }
 *
 * Le validateur associé est {@link com.nnk.springboot.validations.ValidPasswordIfPresentValidator}
 *
 * @see com.nnk.springboot.validations.ValidPasswordIfPresentValidator
 */

@Documented
@Constraint(validatedBy = ValidPasswordIfPresentValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPasswordIfPresent {

	/**
	 * Message d'erreur si la validation échoue.
	 */
	String message() default "Le mot de passe doit contenir au moins 8 caractères, une majuscule, un chiffre et un caractère spécial.";

	/**
	 * Groupes de validation permettant d'appliquer la contrainte
	 * conditionnellement.
	 */
	Class<?>[] groups() default {};

	/**
	 * Informations supplémentaires à destination des clients de l'API de
	 * validation.
	 **/
	Class<? extends Payload>[] payload() default {};

}

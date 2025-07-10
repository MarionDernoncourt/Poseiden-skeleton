package com.nnk.springboot.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Valide une chaîne de caractères représentant un mot de passe contre un motif d'expression régulière prédéfini,
 * mais uniquement si le mot de passe n'est ni null ni vide.

 * Ce validateur est destiné à être utilisé avec l'annotation {@link ValidPasswordIfPresent}.
 * Il permet à un champ de mot de passe d'être facultatif (les valeurs null ou vides sont considérées comme valides)
 * mais applique des règles de mot de passe fort si une valeur est fournie.
 
 * Le mot de passe doit respecter les critères suivants :
  *Au moins 8 caractères de long.
 * Au moins une lettre majuscule.
 * Au moins un chiffre.
 * Au moins un caractère spécial (non alphanumérique et non underscore).
 *
 *
 * @see ValidPasswordIfPresent
 */
public class ValidPasswordIfPresentValidator  implements ConstraintValidator<ValidPasswordIfPresent, String>{

	private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$";
	
	 /**
     * Valide la chaîne de caractères du mot de passe donnée.
     *
     * Si le mot de passe est {@code null} ou une chaîne vide, il est considéré comme valide,
     * car ce validateur permet au mot de passe d'être facultatif.
     * Si le mot de passe n'est ni nul ni vide, il doit correspondre à l'expression
     * régulière {@link #PASSWORD_REGEX} pour être considéré comme valide.
     *
     * @param password Le mot de passe à valider.
     * @param context Le contexte dans lequel la contrainte est évaluée.
     * @return {@code true} si le mot de passe est null, vide ou correspond au motif requis ;
     * {@code false} sinon.
     */
	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		if (password == null || password.isEmpty()) {
			return true;
		}
		return password.matches(PASSWORD_REGEX);
	}

}

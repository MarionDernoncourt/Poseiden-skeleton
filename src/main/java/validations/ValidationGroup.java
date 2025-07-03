package validations;

/**
 * Définition de groupes de validation utilisés pour appliquer des contraintes
 * différentes selon le contexte (création ou mise à jour d'une entité).
 * 
 * Cette classe contient deux interfaces vides qui servent uniquement à marquer
 * les opérations lors de la validation avec {@code @Validated} ou {@code @Valid}.
 * 
 *  {@link OnCreate} : groupe utilisé pour les validations lors de la création d'une entité.
 *  {@link OnUpdate} : groupe utilisé pour les validations lors de la mise à jour d'une entité.
 * 
 *
 * Exemple d'utilisation dans une entité :
 * {@code
 * @NotBlank(groups = ValidationGroup.OnCreate.class)
 * private String password;
 * }
 * 
 * Exemple dans un contrôleur :
 * {@code
 * @PostMapping("/user/update/{id}")
 * public String updateUser(@Validated(OnUpdate.class) User user, ...)
 * }
 *
 */

public class ValidationGroup {
	  public interface OnCreate {}
	    public interface OnUpdate {}
}

package com.nnk.springboot.domain;

import com.nnk.springboot.validations.ValidPasswordIfPresent;
import com.nnk.springboot.validations.ValidationGroup.OnCreate;
import com.nnk.springboot.validations.ValidationGroup.OnUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class User {
	
	
	
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank(message = "Username is mandatory", groups = {OnCreate.class, OnUpdate.class})
    private String username;
 
    @ValidPasswordIfPresent(groups = OnUpdate.class)
    @NotBlank(groups= OnCreate.class, message = "Merci de renseigner un mot de passe")
    @Pattern(
    	    regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=<>?{}\\[\\]~\\-]).{8,}$",
    	    message = "Le mot de passe doit contenir au moins 8 caractères, une majuscule, un chiffre et un caractère spécial",
    	    groups = OnCreate.class
    	)
    private String password;
    
    @NotBlank(message = "FullName is mandatory", groups = { OnCreate.class, OnUpdate.class })
    private String fullname;
    
    @NotBlank(message = "Role is mandatory", groups = { OnCreate.class, OnUpdate.class })
    private String role;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

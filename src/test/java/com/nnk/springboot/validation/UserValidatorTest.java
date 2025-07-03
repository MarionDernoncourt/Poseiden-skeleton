package com.nnk.springboot.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintValidatorContext;
import validations.ValidPasswordIfPresentValidator;

public class UserValidatorTest {

	private ValidPasswordIfPresentValidator validator;
	private ConstraintValidatorContext context;

	@BeforeEach()
	void setUp() {
		validator = new ValidPasswordIfPresentValidator();
		context = null;
	}

	@Test
	public void shoulReturnTrue_WhenPasswordIsNull() {
		assertTrue(validator.isValid(null, context));
	}

	@Test
	public void shouldReturnTrue_WhenPasswordIsEmpty() {
		assertTrue(validator.isValid("", context));
	}

	@Test
	public void shouldReturnTrue_WhenPasswordMeetsCriteria() {
		assertTrue(validator.isValid("Password123@", context));
		assertTrue(validator.isValid("StrongPassword1.", context));
	}

	@Test
	public void shouldReturnFalse_WhenPasswordIsTooShort() {
		assertFalse(validator.isValid("a@1", context));
	}

	@Test
	public void shouldReturnFalse_WhenPasswordHasNoUppercase() {
		assertFalse(validator.isValid("password123@", context));
	}

	@Test
	public void shouldReturnFalse_WhenPasswordHasNoDigit() {
		assertFalse(validator.isValid("Password@", context));
	}

	@Test
	public void shouldReturnFalse_WhenPasswordHasNoSpecialChar() {
		assertFalse(validator.isValid("Password123", context));
	}

}

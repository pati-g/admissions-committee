package com.patrycjagalant.admissionscommittee.utils.validators;

import com.patrycjagalant.admissionscommittee.annotations.ValidPassword;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * The validated input must either be empty or of size between 8 - 127 characters.
 * <p>
 * {@code null} elements are considered valid.
 *
 * @author Patrycja Galant
 */
public class ChangePasswordValidator implements ConstraintValidator<ValidPassword, Object> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
    /**
     * @return true if the element is null, empty or valid
     */
    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        if (object == null) {
            return true;
        }
        String field = (String) object;
        if(field.isBlank()) {
            return true;
        }
        return isPasswordValid(field);
    }

    /**
     * @return true if the element has size between 8-127 characters
     */
    private boolean isPasswordValid(String password) {
        return password.length() >= 8 && password.length() <= 127;
    }
}

package com.patrycjagalant.admissionscommittee.utils.validators;

import com.patrycjagalant.admissionscommittee.annotations.PasswordMatcher;
import com.patrycjagalant.admissionscommittee.dto.UserDto;
import com.patrycjagalant.admissionscommittee.dto.other.UserDtoForEditing;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
/**
 * The validated object must be of type {@link UserDtoForEditing}
 * and must have fields: {@code password} and {@code matchingPassword}.
 * The validator checks if values from both fields are equal
 * (using {@code s1.equals(s2)} method from {@link String} class)
 * <p>
 * {@code null} elements are considered valid.
 *
 * @author Patrycja Galant
 * @see UserDtoForEditing
 * @see String
 */
public class PasswordMatcherValidator implements ConstraintValidator<PasswordMatcher, Object> {

    private String message;

    @Override
    public void initialize(PasswordMatcher constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }

    /**
     * @return a result of String comparison between object fields {@code password} and {@code matchingPassword}
     */
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        boolean isValid = false;
        if(obj.getClass().equals(UserDto.class)) {
            UserDto user = (UserDto) obj;
            isValid = user.getPassword().equals(user.getMatchingPassword());

        } else if (obj.getClass().equals(UserDtoForEditing.class)) {
            UserDtoForEditing user = (UserDtoForEditing) obj;
            String password = user.getPassword();
            String matchingPassword = user.getMatchingPassword();
            if (password == null || matchingPassword == null) {
                return true;
            }
            isValid = password.equals(matchingPassword);

        }
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("matchingPassword").addConstraintViolation();
        }

        return isValid;
    }
}

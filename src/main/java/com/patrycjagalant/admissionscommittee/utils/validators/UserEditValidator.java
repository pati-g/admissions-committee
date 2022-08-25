package com.patrycjagalant.admissionscommittee.utils.validators;

import com.patrycjagalant.admissionscommittee.annotations.EmptyOrValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.patrycjagalant.admissionscommittee.utils.Constants.EMAIL_PATTERN;

public class UserEditValidator implements ConstraintValidator<EmptyOrValid, Object> {

    @Override
    public void initialize(EmptyOrValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        if (object == null) {
            return true;
        }
        String field = (String) object;
        if(field.isBlank()) {
            return true;
        }

        if(field.contains("@")) {
            return isEmailValid(field);
        } else {
            return isPasswordValid(field);
        }
    }

    private boolean isEmailValid(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8 && password.length() <= 127;
    }
}

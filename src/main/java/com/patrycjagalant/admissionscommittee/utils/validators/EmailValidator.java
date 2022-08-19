package com.patrycjagalant.admissionscommittee.utils.validators;

import com.patrycjagalant.admissionscommittee.annotations.ValidEmail;
import com.patrycjagalant.admissionscommittee.service.UserService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
    private static final String EMAIL_PATTERN =
            "^(?=.{1,64}@)[\\p{L}\\d_-]+(\\.[\\p{L}\\d_-]++)*+@[^-][\\p{L}\\d-]+(\\.[\\p{L}\\d-]++)*(\\.\\p{L}{2,})$";
    private final UserService userService;
    private String message;

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email.isBlank()) {
            return false;
        }
        if (userService.findByEmail(email) != null) {
            this.message = "User with e-mail: " + email + " already exists";
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
            return false;
        }
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

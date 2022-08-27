package com.patrycjagalant.admissionscommittee.utils.validators;

import com.patrycjagalant.admissionscommittee.annotations.ValidEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.patrycjagalant.admissionscommittee.utils.Constants.EMAIL_PATTERN;

/**
 * The validated input must be blank or match the regex pattern <br>
 * {@link com.patrycjagalant.admissionscommittee.utils.Constants#EMAIL_PATTERN}.
 * <p>
 * {@code null} elements are considered valid.
 *</p>
 * @author Patrycja Galant
 */
public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        String message = constraintAnnotation.message();
    }

    /**
     * @return true if the element matches the regex pattern
     */
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email.isBlank()) {
            return true;
        }
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

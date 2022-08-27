package com.patrycjagalant.admissionscommittee.annotations;

import com.patrycjagalant.admissionscommittee.utils.validators.PasswordMatcherValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
/**
 * This is a class-level validation annotation that matches the input of an element
 * {@code password} with the input of an element {@code matchingPassword} and compares them.
 *
 * @author Patrycja Galant
 * @see PasswordMatcherValidator
 */
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordMatcherValidator.class)
@Documented
public @interface PasswordMatcher {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

package com.patrycjagalant.admissionscommittee.annotations;

import com.patrycjagalant.admissionscommittee.utils.validators.EmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
/**
 * The annotated String must be compliant with the constraint rules
 * described in the {@link EmailValidator} class.
 * {@code null} elements are considered valid.
 *
 * @author Patrycja Galant
 */
@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
@Documented
public @interface ValidEmail {
    String message() default "{validation.email}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
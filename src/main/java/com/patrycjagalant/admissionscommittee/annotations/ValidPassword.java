package com.patrycjagalant.admissionscommittee.annotations;

import com.patrycjagalant.admissionscommittee.utils.validators.ChangePasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
/**
 * The annotated String must be either blank or compliant with the constraint rules
 * described in the {@link ChangePasswordValidator} class.
 * {@code null} elements are considered valid.
 *
 * @author Patrycja Galant
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ChangePasswordValidator.class)
@Documented
public @interface ValidPassword {
    String message() default "{validation.emptyorvalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

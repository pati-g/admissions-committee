package com.patrycjagalant.admissionscommittee.annotations;

import com.patrycjagalant.admissionscommittee.utils.validators.UserEditValidator;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = UserEditValidator.class)
@Documented
public @interface EmptyOrValid {
    String message() default "{com.mycompany.constraints.nullornotempty}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

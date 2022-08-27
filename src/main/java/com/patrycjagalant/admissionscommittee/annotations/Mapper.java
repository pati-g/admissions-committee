package com.patrycjagalant.admissionscommittee.annotations;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;
/**
 * The annotation interface marks the annotated class as a mapper.
 * It also serves as a specialization of
 * {@link Component @Component}, allowing for implementation classes to be autodetected
 * through classpath scanning.
 *
 * @author Patrycja Galant
 * @see Component
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Mapper {
}

package com.patrycjagalant.admissionscommittee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
/**
 * A configuration class for encoding users' passwords with a {@link PasswordEncoder},
 * currently used implementation is {@link Argon2PasswordEncoder}.
 *
 * @author Patrycja Galant
 * @see PasswordEncoder
 * @see Argon2PasswordEncoder
 */

@Configuration
public class PasswordConfig {

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder();
    }

}

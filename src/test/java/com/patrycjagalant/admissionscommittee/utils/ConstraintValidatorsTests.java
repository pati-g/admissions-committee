package com.patrycjagalant.admissionscommittee.utils;

import com.patrycjagalant.admissionscommittee.dto.UserDto;
import com.patrycjagalant.admissionscommittee.dto.other.UserDtoForEditing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ConstraintValidatorsTests {

    private LocalValidatorFactoryBean validator;
    @Autowired
    private WebApplicationContext webApplicationContext;

    private UserDto dto;

    @BeforeEach
    public void setUp() {
        SpringConstraintValidatorFactory springConstraintValidatorFactory
                = new SpringConstraintValidatorFactory(webApplicationContext.getAutowireCapableBeanFactory());
        validator = new LocalValidatorFactoryBean();
        validator.setConstraintValidatorFactory(springConstraintValidatorFactory);
        validator.setApplicationContext(webApplicationContext);
        validator.afterPropertiesSet();
        dto = UserDto.builder().username("frodo").password("password")
                .matchingPassword("password").email("frodo.baggins@shire.com").build();
    }

    @Test
    void shouldAcceptValidInput() {
        Set<ConstraintViolation<UserDto>> constraintViolations = validator.validate(dto);
        assertEquals(0, constraintViolations.size());
    }

    @Test
    void shouldAddViolation_WhenInvalidEmail() {
        dto.setEmail("frodo.baggins");
        Set<ConstraintViolation<UserDto>> constraintViolations = validator.validate(dto);
        assertEquals(1, constraintViolations.size());
    }

    @Test
    void shouldAddViolation_WhenPasswordsDontMatch() {
        dto.setMatchingPassword("matchingpassword");
        Set<ConstraintViolation<UserDto>> constraintViolations = validator.validate(dto);
        assertEquals(1, constraintViolations.size());
    }

    @Test
    void shouldAccept_whenDtoIsValid() {
        UserDtoForEditing dtoEdit = UserDtoForEditing.builder()
                .email("valid-email@gmail.com")
                .build();
        Set<ConstraintViolation<UserDtoForEditing>> violations = validator.validate(dtoEdit);
        assertEquals(0, violations.size());

        UserDtoForEditing dtoEdit2 = UserDtoForEditing.builder()
                .password("PASSWORD")
                .matchingPassword("PASSWORD")
                .build();
        Set<ConstraintViolation<UserDtoForEditing>> violations2 = validator.validate(dtoEdit2);
        assertEquals(0, violations2.size());

        UserDtoForEditing dtoEdit3 = new UserDtoForEditing();
        Set<ConstraintViolation<UserDtoForEditing>> violations3 = validator.validate(dtoEdit3);
        assertEquals(0, violations3.size());
    }

    @Test
    void shouldAddViolation_whenDtoIsInvalid() {
        UserDtoForEditing dtoEdit = UserDtoForEditing.builder()
                .email("not-valid-email@")
                .build();
        Set<ConstraintViolation<UserDtoForEditing>> violations = validator.validate(dtoEdit);
        assertEquals(1, violations.size());

        UserDtoForEditing dtoEdit2 = UserDtoForEditing.builder()
                .password("password")
                .matchingPassword("PASSWORD")
                .build();
        Set<ConstraintViolation<UserDtoForEditing>> violations2 = validator.validate(dtoEdit2);
        assertEquals(1, violations2.size());
    }
}

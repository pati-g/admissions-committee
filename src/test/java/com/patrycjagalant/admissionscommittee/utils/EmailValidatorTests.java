package com.patrycjagalant.admissionscommittee.utils;

import com.patrycjagalant.admissionscommittee.service.mapper.ApplicantMapper;
import com.patrycjagalant.admissionscommittee.service.mapper.UserMapper;
import com.patrycjagalant.admissionscommittee.utils.validators.EmailValidator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = EmailValidator.class)
public class EmailValidatorTests {

    @Autowired
    private EmailValidator validator;


}

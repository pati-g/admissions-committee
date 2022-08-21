package com.patrycjagalant.admissionscommittee.repository;

import com.patrycjagalant.admissionscommittee.entity.Applicant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ApplicantRepositoryTests {

    @Autowired
    private ApplicantRepository repository;

    @Test
    void findByUserIdReturnsCorrectEntity() {
        Optional<Applicant> applicantOptional = repository.findByUserId(400L);
        assertThat(applicantOptional).isPresent();
        assertThat(applicantOptional.get()).satisfies(applicant -> {
            assertThat(applicant.getUser().getId()).isEqualTo(400L);
            assertThat(applicant.getFirstName()).isEqualTo("Donna");
        });
    }
}

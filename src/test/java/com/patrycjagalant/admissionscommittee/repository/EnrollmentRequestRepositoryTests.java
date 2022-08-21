package com.patrycjagalant.admissionscommittee.repository;

import com.patrycjagalant.admissionscommittee.entity.EnrollmentRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EnrollmentRequestRepositoryTests {

    @Autowired
    private EnrollmentRequestRepository repository;

    @Test
    void findByApplicantIdReturnsCorrectList() {
     List<EnrollmentRequest> requests = repository.findByApplicantId(13L);
     assertThat(requests).hasSize(10);
    }

    @Test
    void findByFacultyIdReturnsCorrectList() {
     List<EnrollmentRequest> requests = repository.findByFacultyId(200L);
     assertThat(requests.stream()
             .map(req -> req.getFaculty().getName()).collect(Collectors.toSet()))
             .hasSize(1).satisfies(name-> name.contains("Programming"));
    }
}

package com.patrycjagalant.admissionscommittee.repository;

import com.patrycjagalant.admissionscommittee.entity.EnrollmentRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface EnrollmentRequestRepository extends JpaRepository<EnrollmentRequest, Long> {
    public List<EnrollmentRequest> findByApplicantId(Long applicantId);
}
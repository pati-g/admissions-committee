package com.patrycjagalant.admissionscommittee.repository;

import com.patrycjagalant.admissionscommittee.entity.EnrollmentRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface EnrollmentRequestRepository extends JpaRepository<EnrollmentRequest, Long> {
    List<EnrollmentRequest> findByApplicantId(Long applicantId);

    List<EnrollmentRequest> findByFacultyId(Long facultyId);
    Page<EnrollmentRequest> findByFacultyId(Long facultyId, Pageable pageable);

}
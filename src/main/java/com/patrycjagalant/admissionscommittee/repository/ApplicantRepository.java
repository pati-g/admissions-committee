package com.patrycjagalant.admissionscommittee.repository;

import com.patrycjagalant.admissionscommittee.entity.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

    Optional<Applicant> findByUserId(Long userId);

}

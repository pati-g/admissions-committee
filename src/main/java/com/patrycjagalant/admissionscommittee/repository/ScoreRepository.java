package com.patrycjagalant.admissionscommittee.repository;

import com.patrycjagalant.admissionscommittee.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    public List<Score> findByApplicantId(Long applicantId);
}

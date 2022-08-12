package com.patrycjagalant.admissionscommittee.repository;

import com.patrycjagalant.admissionscommittee.entity.Statement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatementRepository extends JpaRepository<Statement, Long> {
//    public List<Statement> findByApplicantId(Long applicantId);

}

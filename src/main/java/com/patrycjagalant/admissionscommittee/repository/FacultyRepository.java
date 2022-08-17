package com.patrycjagalant.admissionscommittee.repository;

import com.patrycjagalant.admissionscommittee.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Faculty findByName(String name);
}

package com.patrycjagalant.admissionscommittee.repository;

import com.patrycjagalant.admissionscommittee.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    @Query("SELECT s FROM Subject s JOIN Faculty f WHERE f.id = :id")
    public Set<Subject> findAllByFacultyId(@Param("id") Long facultyID);

    public Set<Subject> findByFaculties_id(Long facultyId);
    Optional<Subject> findByName(String subjectName);

}

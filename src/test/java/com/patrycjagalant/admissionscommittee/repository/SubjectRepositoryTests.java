package com.patrycjagalant.admissionscommittee.repository;

import com.patrycjagalant.admissionscommittee.entity.Subject;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.Set;

@DataJpaTest
class SubjectRepositoryTests {

    @Autowired
    private SubjectRepository subjectRepository;
    private final String ENGLISH = "English";

    @Test
    void findByNameReturnsCorrectSubject() {
        Optional<Subject> subject = subjectRepository.findByName(ENGLISH);
        Condition<Optional<Subject>> subjectName = new Condition<>
                (s-> s.get().getName().equalsIgnoreCase(ENGLISH), "subjectName");
        assertThat(subject).has(subjectName);
    }

    @Test
    void findByFacultiesIdReturnsSetOfSubjects() {
        Long facultyId = 200L;
        Set<String> subjectNames = Set.of("Maths", "IT", "Physics");
        Set<Subject> subjectsFromDatabase = subjectRepository.findByFaculties_id(facultyId);
        subjectsFromDatabase.forEach(subject -> assertThat(subject.getName()).isIn(subjectNames));
    }

    @Test
    void findByFacultiesIdReturnsEmptySetIfIncorrectId() {
        Long wrongId = 99999999L;
        assertThat(subjectRepository.findByFaculties_id(wrongId)).isEmpty();
    }

    @Test
    void findByNameReturnsEmptySetIfIncorrectName() {
        String wrongName = "dummy";
        assertThat(subjectRepository.findByName(wrongName)).isEmpty();
    }
}
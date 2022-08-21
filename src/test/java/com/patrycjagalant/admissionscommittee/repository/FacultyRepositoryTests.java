package com.patrycjagalant.admissionscommittee.repository;

import com.patrycjagalant.admissionscommittee.entity.Faculty;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class FacultyRepositoryTests {

    @Autowired
    private FacultyRepository facultyRepository;

    @Test
    void findByNameReturnsCorrectEntity() {
        String name = "Programming";
        Faculty faculty = facultyRepository.findByName(name);
        assertThat(faculty.getName()).isEqualTo(name);
        assertThat(faculty.getBudgetPlaces()).isEqualTo(3);
        assertThat(faculty.getId()).isEqualTo(200L);
    }

    @Test
    void findByNameReturnsNullIfWrongNAme() {
        Faculty faculty = facultyRepository.findByName("dummyname");
        assertThat(faculty).isNull();
    }
}

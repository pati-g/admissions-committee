package com.patrycjagalant.admissionscommittee.service.mapper;

import com.patrycjagalant.admissionscommittee.dto.FacultyDto;
import com.patrycjagalant.admissionscommittee.entity.Faculty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = FacultyMapper.class)
class FacultyMapperTests {

    @Autowired
    FacultyMapper mapper;

    @Test
    void mapDtoToEntityReturnsCorrectEntity() {
        FacultyDto facultyDTO =
                new FacultyDto(null, "ChangedName", null, 30, null, null);
        Faculty faculty = new Faculty(1L, "TestName", 10, 20, null, null);
        mapper.mapToEntity(faculty, facultyDTO);
        assertEquals("ChangedName", faculty.getName());
        assertEquals(10, faculty.getBudgetPlaces());
        assertEquals(30, faculty.getTotalPlaces());
    }

    @Test
    void mapToDtoMapsCorrectly() {
        Faculty faculty = new Faculty(1L, "TestName", 10, 20, null, null);
        FacultyDto dto = mapper.mapToDto(faculty);
        Assertions.assertNotNull(dto);
        assertEquals("TestName", dto.getName());
        assertEquals(10, dto.getBudgetPlaces());
        assertEquals(20, dto.getTotalPlaces());
    }

    @Test
    void mapperCanDoMultipleMappingsCorrectly() {
        Faculty testEntityToDto1 =
                new Faculty(1L, "Bio", 9, 40, null, null);
        Faculty testEntityToDto2 =
                new Faculty(2L, "Chem", 15, 50, null, null);
        Faculty result4 = new Faculty(3L, "English", 20, 100, null, null);

        FacultyDto testDtoToNewEntity =
                new FacultyDto(null, "Molecular", 5, 30, null, null);
                FacultyDto testDtoToEntityEditFields1 =
                new FacultyDto(null, "Spanish", 55, 75, null, null);

        FacultyDto result1 = mapper.mapToDto(testEntityToDto1);
        FacultyDto result2 = mapper.mapToDto(testEntityToDto2);
        Faculty result3 = mapper.mapToEntity(testDtoToNewEntity);
        mapper.mapToEntity(result4, testDtoToEntityEditFields1);

        assertEquals(new FacultyDto
                (null, "Bio", 9, 40, null, null), result1);
        assertEquals(new FacultyDto
                (null, "Chem", 15, 50, null, null), result2);

        assertNotNull(result3);
        assertEquals("Molecular", result3.getName());
        assertNull(result3.getId());

        assertEquals("Spanish", result4.getName());
        assertEquals(55, result4.getBudgetPlaces());
        assertEquals(75, result4.getTotalPlaces());
    }

    @Test
    void whenMapDtoToEntityIfDtoValueIsNullThenEntityValueDoesntChange(){
        FacultyDto dto =
                new FacultyDto(null, "WF", null, null, null, null);
        Faculty faculty = new Faculty(4L, "Math", 23, 70, null, null);
        mapper.mapToEntity(faculty, dto);

        FacultyDto testDtoToEntityEditFields3 =
                new FacultyDto(null, null, null, 30, null, null);
        Faculty result6 = new Faculty(5L, "Physics", 41, 80, null, null);
        mapper.mapToEntity(result6, testDtoToEntityEditFields3);

        assertEquals("WF", faculty.getName());
        assertEquals(23, faculty.getBudgetPlaces());
        assertEquals(30, result6.getTotalPlaces());
    }
}

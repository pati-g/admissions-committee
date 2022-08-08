package com.patrycjagalant.admissionscommittee.service.mapper;

import com.patrycjagalant.admissionscommittee.dto.FacultyDto;
import com.patrycjagalant.admissionscommittee.entity.Faculty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class FacultyMapperTest {

    @Autowired
    FacultyMapper mapper;
    @Test
    void testMapDtoToEntityHappyPath(){
        FacultyDto facultyDTO = new FacultyDto(null, "ChangedName", null, 30);
        Faculty faculty = new Faculty(1L, "TestName", 10, 20);
        mapper.mapToEntity(faculty, facultyDTO);
        assertEquals("ChangedName", faculty.getName());
        assertEquals(10, faculty.getBudgetPlaces());
        assertEquals(30, faculty.getTotalPlaces());
    }

    @Test
    void testMapToDtoHappyPath(){
        Faculty faculty = new Faculty(1L, "TestName", 10, 20);
        FacultyDto dto = mapper.mapToDto(faculty);
        Assertions.assertNotNull(dto);
        assertEquals("TestName", dto.getName());
        assertEquals(10, dto.getBudgetPlaces());
        assertEquals(20, dto.getTotalPlaces());
    }

    @Test
    void testDoMappings6Times(){
        Faculty testEntityToDto1 = new Faculty(1L, "Bio", 9, 40);
        Faculty testEntityToDto2 = new Faculty(2L, "Chem", 15, 50);
        Faculty result4 = new Faculty(3L, "English", 20, 100);
        Faculty result5 = new Faculty(4L, "Math", 23, 70);
        Faculty result6 = new Faculty(5L, "Physics", 41, 80);

        FacultyDto testDtoToNewEntity = new FacultyDto(null, "Molecular", 5, 30);
        FacultyDto testDtoToEntityEditFields2 = new FacultyDto(null, "WF", null, null);
        FacultyDto testDtoToEntityEditFields1 = new FacultyDto(null, "Spanish", 55, 75);
        FacultyDto testDtoToEntityEditFields3 = new FacultyDto(null, null, null, 30);

        FacultyDto result1 = mapper.mapToDto(testEntityToDto1);
        FacultyDto result2 = mapper.mapToDto(testEntityToDto2);
        Faculty result3 = mapper.mapToEntity(testDtoToNewEntity);
        mapper.mapToEntity(result4, testDtoToEntityEditFields1);
        mapper.mapToEntity(result5, testDtoToEntityEditFields2);
        mapper.mapToEntity(result6, testDtoToEntityEditFields3);

        assertEquals(new FacultyDto(null, "Bio", 9, 40), result1);
        assertEquals(new FacultyDto(null, "Chem", 15, 50), result2);

        assertNotNull(result3);
        assertEquals("Molecular", result3.getName());
        assertNull(result3.getId());

        assertEquals("Spanish", result4.getName());
        assertEquals(55, result4.getBudgetPlaces());
        assertEquals(75, result4.getTotalPlaces());

        assertEquals("WF", result5.getName());
        assertEquals(23, result5.getBudgetPlaces());

        assertEquals(30, result6.getTotalPlaces());
    }
}

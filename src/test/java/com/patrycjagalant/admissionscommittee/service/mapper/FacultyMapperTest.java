package com.patrycjagalant.admissionscommittee.service.mapper;

import com.patrycjagalant.admissionscommittee.dto.FacultyDTO;
import com.patrycjagalant.admissionscommittee.entity.Faculty;
import com.patrycjagalant.admissionscommittee.service.mapper.FacultyMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FacultyMapperTest {

    @Test
    void testMapDtoToEntityHappyPath(){
        FacultyDTO facultyDTO = new FacultyDTO(null, "ChangedName", null, 30);
        Faculty faculty = new Faculty(1L, "TestName", 10, 20);
        FacultyMapper.mapToEntity(faculty, facultyDTO);
        assertEquals("ChangedName", faculty.getName());
        assertEquals(10, faculty.getBudgetPlaces());
        assertEquals(30, faculty.getTotalPlaces());
    }

    @Test
    void testMapToDtoHappyPath(){
        Faculty faculty = new Faculty(1L, "TestName", 10, 20);
        FacultyDTO dto = FacultyMapper.mapToDto(faculty);
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

        FacultyDTO testDtoToNewEntity = new FacultyDTO(10L, "Molecular", 5, 30);
        FacultyDTO testDtoToEntityEditFields2 = new FacultyDTO(null, "WF", null, null);
        FacultyDTO testDtoToEntityEditFields1 = new FacultyDTO(null, "Spanish", 55, 75);
        FacultyDTO testDtoToEntityEditFields3 = new FacultyDTO(null, null, null, 30);

        FacultyDTO result1 = FacultyMapper.mapToDto(testEntityToDto1);
        FacultyDTO result2 = FacultyMapper.mapToDto(testEntityToDto2);
        Faculty result3 = FacultyMapper.mapToEntity(testDtoToNewEntity);
        FacultyMapper.mapToEntity(result4, testDtoToEntityEditFields1);
        FacultyMapper.mapToEntity(result5, testDtoToEntityEditFields2);
        FacultyMapper.mapToEntity(result6, testDtoToEntityEditFields3);

        assertEquals(new FacultyDTO(1L, "Bio", 9, 40), result1);
        assertEquals(new FacultyDTO(2L, "Chem", 15, 50), result2);

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

package com.patrycjagalant.admissionscommittee.service.mapper;

import com.patrycjagalant.admissionscommittee.dto.FacultyDTO;
import com.patrycjagalant.admissionscommittee.entity.Faculty;

import java.util.List;
import java.util.stream.Collectors;

public class FacultyMapper {
    private FacultyMapper(){}

    public static FacultyDTO mapToDto(Faculty faculty) {
        FacultyDTO facultyDTO = new FacultyDTO();

        facultyDTO.setId(faculty.getId());
        facultyDTO.setName(faculty.getName());
        facultyDTO.setBudgetPlaces(faculty.getBudgetPlaces());
        facultyDTO.setTotalPlaces(faculty.getTotalPlaces());

        return facultyDTO;
    }

    public static List<FacultyDTO> mapToDto(List<Faculty> faculties) {
        return faculties.stream().map(FacultyMapper::mapToDto).collect(Collectors.toList());
    }

    public static Faculty mapToEntity(FacultyDTO facultyDTO) {
        Faculty faculty = new Faculty();

        faculty.setName(facultyDTO.getName());
        faculty.setBudgetPlaces(facultyDTO.getBudgetPlaces());
        faculty.setTotalPlaces(facultyDTO.getTotalPlaces());

        return faculty;
    }

    public static void mapToEntity(Faculty faculty, FacultyDTO facultyDTO) {
        String name = facultyDTO.getName();
        Integer budget = facultyDTO.getBudgetPlaces();
        Integer total = facultyDTO.getTotalPlaces();

        if (name != null && !name.isEmpty())
            faculty.setName(name);
        faculty.setBudgetPlaces(budget);
        faculty.setTotalPlaces(total);
    }
}

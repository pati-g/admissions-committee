package com.patrycjagalant.admissionscommittee.service.mapper;

import com.patrycjagalant.admissionscommittee.dto.FacultyDto;
import com.patrycjagalant.admissionscommittee.entity.Faculty;

import java.util.List;
import java.util.stream.Collectors;

public class FacultyMapper {

    public FacultyDto mapToDto(Faculty faculty) {
        FacultyDto facultyDTO = new FacultyDto();

        facultyDTO.setId(faculty.getId());
        facultyDTO.setName(faculty.getName());
        facultyDTO.setBudgetPlaces(faculty.getBudgetPlaces());
        facultyDTO.setTotalPlaces(faculty.getTotalPlaces());

        return facultyDTO;
    }

    public List<FacultyDto> mapToDto(List<Faculty> faculties) {
        FacultyMapper mapper = new FacultyMapper();
        return faculties.stream().map(mapper::mapToDto).collect(Collectors.toList());
    }

    public Faculty mapToEntity(FacultyDto facultyDTO) {
        Faculty faculty = new Faculty();

        faculty.setName(facultyDTO.getName());
        faculty.setBudgetPlaces(facultyDTO.getBudgetPlaces());
        faculty.setTotalPlaces(facultyDTO.getTotalPlaces());

        return faculty;
    }

    public void mapToEntity(Faculty faculty, FacultyDto facultyDTO) {
        String name = facultyDTO.getName();
        Integer budget = facultyDTO.getBudgetPlaces();
        Integer total = facultyDTO.getTotalPlaces();

        if (name != null && !name.isEmpty())
            faculty.setName(name);
        faculty.setBudgetPlaces(budget);
        faculty.setTotalPlaces(total);
    }
}

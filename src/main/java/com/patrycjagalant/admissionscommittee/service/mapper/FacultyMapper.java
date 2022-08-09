package com.patrycjagalant.admissionscommittee.service.mapper;

import com.patrycjagalant.admissionscommittee.dto.FacultyDto;
import com.patrycjagalant.admissionscommittee.entity.Faculty;

import java.util.List;
import java.util.stream.Collectors;

public class FacultyMapper {

    public FacultyDto mapToDto(Faculty faculty) {
        return FacultyDto.builder()
                .id(faculty.getId())
                .name(faculty.getName())
                .budgetPlaces(faculty.getBudgetPlaces())
                .totalPlaces(faculty.getTotalPlaces()).build();
    }

    public List<FacultyDto> mapToDto(List<Faculty> faculties) {
        FacultyMapper mapper = new FacultyMapper();
        return faculties.stream().map(mapper::mapToDto).collect(Collectors.toList());
    }

    public Faculty mapToEntity(FacultyDto facultyDTO) {
        return Faculty.builder()
                .name(facultyDTO.getName())
                .budgetPlaces(facultyDTO.getBudgetPlaces())
                .totalPlaces(facultyDTO.getTotalPlaces()).build();
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

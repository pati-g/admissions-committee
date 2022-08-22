package com.patrycjagalant.admissionscommittee.service.mapper;

import com.patrycjagalant.admissionscommittee.annotations.Mapper;
import com.patrycjagalant.admissionscommittee.dto.FacultyDto;
import com.patrycjagalant.admissionscommittee.entity.Faculty;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Mapper
public class FacultyMapper {

    public FacultyDto mapToDto(Faculty faculty) {
        log.debug("Faculty entity before mapping: {}", faculty);
        return FacultyDto.builder()
                .id(faculty.getId())
                .name(faculty.getName())
                .budgetPlaces(faculty.getBudgetPlaces())
                .totalPlaces(faculty.getTotalPlaces()).build();
    }

    public List<FacultyDto> mapToDto(List<Faculty> faculties) {
        log.debug("Mapping List<Faculty>");
        return faculties.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public Faculty mapToEntity(FacultyDto facultyDTO) {
        log.debug("Faculty DTO before mapping: {}", facultyDTO);
        return Faculty.builder()
                .name(facultyDTO.getName())
                .budgetPlaces(facultyDTO.getBudgetPlaces())
                .totalPlaces(facultyDTO.getTotalPlaces()).build();
    }

    public void mapToEntity(Faculty faculty, FacultyDto facultyDTO) {
        log.debug("Faculty entity before mapping: {}", faculty);

        String name = facultyDTO.getName();
        Integer budget = facultyDTO.getBudgetPlaces();
        Integer total = facultyDTO.getTotalPlaces();

        if (name != null && !name.isEmpty()) {
            faculty.setName(name);
        }
        if (budget != null) {
            faculty.setBudgetPlaces(budget);
        }
        if (total != null) {
            faculty.setTotalPlaces(total);
        }
    }

    public Faculty mapToEntityWithId(FacultyDto facultyDto) {
        log.debug("Mapping Faculty DTO with ID");
        Faculty faculty = this.mapToEntity(facultyDto);
        faculty.setId(facultyDto.getId());
        return faculty;
    }
}

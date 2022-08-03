package com.patrycjagalant.admissionscommittee.service.mapper;

import com.patrycjagalant.admissionscommittee.dto.FacultyDTO;
import com.patrycjagalant.admissionscommittee.entity.Faculty;

import javax.transaction.Transactional;

public class FacultyMapper {
    private FacultyMapper(){}
    @Transactional
    public static FacultyDTO mapToDto(Faculty faculty) {
        FacultyDTO facultyDTO = new FacultyDTO();

        facultyDTO.setName(faculty.getName());
        facultyDTO.setBudgetPlaces(faculty.getBudgetPlaces());
        facultyDTO.setTotalPlaces(faculty.getTotalPlaces());

        return facultyDTO;
    }
    @Transactional
    public static Faculty mapToEntity(FacultyDTO facultyDTO) {
        Faculty faculty = new Faculty();

        faculty.setName(facultyDTO.getName());
        faculty.setBudgetPlaces(facultyDTO.getBudgetPlaces());
        faculty.setTotalPlaces(facultyDTO.getTotalPlaces());

        return faculty;
    }
    @Transactional
    public static void mapToEntity(Faculty faculty, FacultyDTO facultyDTO) {
        String name = facultyDTO.getName();
        Integer budget = facultyDTO.getBudgetPlaces();
        Integer total = facultyDTO.getTotalPlaces();

        if (name != null && !name.isEmpty())
            faculty.setName(name);
        if (budget != null)
            faculty.setBudgetPlaces(budget);
        if (total != null)
            faculty.setTotalPlaces(total);
    }
}

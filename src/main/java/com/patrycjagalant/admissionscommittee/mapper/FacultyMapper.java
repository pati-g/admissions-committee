package com.patrycjagalant.admissionscommittee.mapper;

import com.patrycjagalant.admissionscommittee.dto.FacultyDTO;
import com.patrycjagalant.admissionscommittee.entity.Faculty;


//@Mapper
public interface FacultyMapper {

 //   FacultyMapper INSTANCE = Mappers.getMapper(FacultyMapper.class);

    FacultyDTO facultyToFacultyDTo (Faculty faculty);
    Faculty facultyDTOToFaculty (FacultyDTO facultyDTO);
}

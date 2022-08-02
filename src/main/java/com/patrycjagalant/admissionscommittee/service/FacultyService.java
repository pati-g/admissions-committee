package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.FacultyDTO;
import com.patrycjagalant.admissionscommittee.entity.Faculty;
import com.patrycjagalant.admissionscommittee.service.mapper.FacultyMapper;
import com.patrycjagalant.admissionscommittee.repository.FacultyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class FacultyService {
    private static final int PAGE_SIZE = 10;
    private final FacultyRepository facultyRepository;
    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    public  FacultyDTO findByName(String name) {
        Faculty faculty= facultyRepository.findByName(name);
        return FacultyMapper.mapToDto(faculty);
    }

    public FacultyDTO getOne(Long id) {
        Faculty faculty = facultyRepository.getReferenceById(id);
        return FacultyMapper.mapToDto(faculty);
    }

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    // Accessible only to admin:
    public void deleteFaculty(Long id) {facultyRepository.deleteById(id);}
    public Faculty addFaculty(FacultyDTO facultyDTO) {
        Faculty faculty = FacultyMapper.mapToEntity(facultyDTO);
        return facultyRepository.save(faculty);}
    @Transactional
    public Faculty editFaculty(FacultyDTO facultyDTO, Long id) {
        Faculty currentFaculty = facultyRepository.getReferenceById(id);
        FacultyMapper.mapToEntity(currentFaculty, facultyDTO);
        return facultyRepository.save(currentFaculty);
    }


}

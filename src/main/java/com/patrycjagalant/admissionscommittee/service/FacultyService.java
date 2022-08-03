package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.FacultyDTO;
import com.patrycjagalant.admissionscommittee.entity.Faculty;
import com.patrycjagalant.admissionscommittee.service.mapper.FacultyMapper;
import com.patrycjagalant.admissionscommittee.repository.FacultyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Page<Faculty> getAllFaculties(int page, int size, Sort.Direction sort, String sortBy) {
        return facultyRepository.findAll(PageRequest.of(page, size, Sort.by(sort, sortBy)));
    }

    public  FacultyDTO findByName(String name) {
        Faculty faculty= facultyRepository.findByName(name);
        return FacultyMapper.mapToDto(faculty);
    }

    public Faculty getOne(Long id) {
        return facultyRepository.getReferenceById(id);
    }


    // Accessible only to admin:
    public void deleteFaculty(Long id) {facultyRepository.deleteById(id);}
    public Faculty addFaculty(FacultyDTO facultyDTO) {
        Faculty faculty = FacultyMapper.mapToEntity(facultyDTO);
        return facultyRepository.save(faculty);}
    @Transactional
    public Faculty editFaculty(FacultyDTO facultyDTO, Long id) {
        Faculty currentFaculty = facultyRepository.findById(id).orElseThrow();
        FacultyMapper.mapToEntity(currentFaculty, facultyDTO);
        return currentFaculty;
    }


}

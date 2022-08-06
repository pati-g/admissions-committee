package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.FacultyDTO;
import com.patrycjagalant.admissionscommittee.entity.Faculty;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchFacultyException;
import com.patrycjagalant.admissionscommittee.service.mapper.ApplicantMapper;
import com.patrycjagalant.admissionscommittee.service.mapper.FacultyMapper;
import com.patrycjagalant.admissionscommittee.repository.FacultyRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }
    public Page<FacultyDTO> getAllFaculties(int page, int size, Sort.Direction sort, String sortBy) {
        long facultiesTotal = facultyRepository.count();
        if((long) page * size > facultiesTotal) {
            page = 1;
            size = 5;
        }

        Sort.Direction sortDirection = sort != null ? sort : Sort.Direction.DESC;
        Page<Faculty> facultyPage = facultyRepository.findAll(PageRequest.of(page-1, size, Sort.by(sortDirection, sortBy)));
        FacultyMapper facultyMapper = new FacultyMapper();
        List<FacultyDTO> facultyDTOS = facultyMapper.mapToDto(facultyPage.getContent());
        return new PageImpl<>(facultyDTOS, PageRequest.of(page-1, size, Sort.by(sortDirection, sortBy)), facultiesTotal);
    }

    public  FacultyDTO findByName(String name) {
        Faculty faculty= facultyRepository.findByName(name);
        FacultyMapper facultyMapper = new FacultyMapper();
        return facultyMapper.mapToDto(faculty);
    }

    public FacultyDTO getById(Long id) {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(NoSuchFacultyException::new);
        FacultyMapper facultyMapper = new FacultyMapper();
        return facultyMapper.mapToDto(faculty);
    }

    // Accessible only to admin:
    public void deleteFaculty(Long id) {
        if(facultyRepository.findById(id).isPresent()) {
            facultyRepository.deleteById(id);
        }
        else {
            throw new NoSuchFacultyException();
        }
    }
    public Faculty addFaculty(FacultyDTO facultyDTO) {
        FacultyMapper facultyMapper = new FacultyMapper();
        Faculty faculty = facultyMapper.mapToEntity(facultyDTO);
        return facultyRepository.save(faculty);}
    @Transactional
    public Faculty editFaculty(FacultyDTO facultyDTO, Long id) {
        Faculty currentFaculty = facultyRepository.findById(id).orElseThrow(NoSuchFacultyException::new);
        FacultyMapper facultyMapper = new FacultyMapper();
        facultyMapper.mapToEntity(currentFaculty, facultyDTO);
        return currentFaculty;
    }

}

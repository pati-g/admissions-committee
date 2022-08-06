package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.FacultyDTO;
import com.patrycjagalant.admissionscommittee.entity.Faculty;
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

    public Page<FacultyDTO> getAllFaculties(int pagestr, int sizestr, Sort.Direction sort, String sortBy) {
        long facultiesTotal = facultyRepository.count();
        if((long) pagestr * sizestr > facultiesTotal) {
            pagestr = 1;
            sizestr = 5;
        }

        Sort.Direction sortDirection = sort != null ? sort : Sort.Direction.DESC;
        Page<Faculty> facultyPage = facultyRepository.findAll(PageRequest.of(pagestr-1, sizestr, Sort.by(sortDirection, sortBy)));
        List<FacultyDTO> facultyDTOS = FacultyMapper.mapToDto(facultyPage.getContent());
        return new PageImpl<>(facultyDTOS, PageRequest.of(pagestr-1, sizestr, Sort.by(sortDirection, sortBy)), facultiesTotal);
    }

    public  FacultyDTO findByName(String name) {
        Faculty faculty= facultyRepository.findByName(name);
        return FacultyMapper.mapToDto(faculty);
    }

    public FacultyDTO getOne(Long id) {
        Faculty faculty = facultyRepository.getReferenceById(id);
        return FacultyMapper.mapToDto(faculty);
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

package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.FacultyDto;
import com.patrycjagalant.admissionscommittee.entity.Faculty;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchFacultyException;
import com.patrycjagalant.admissionscommittee.service.mapper.FacultyMapper;
import com.patrycjagalant.admissionscommittee.repository.FacultyRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    private final FacultyMapper facultyMapper;
    public FacultyService(FacultyRepository facultyRepository, FacultyMapper facultyMapper) {
        this.facultyRepository = facultyRepository;
        this.facultyMapper = facultyMapper;
    }
    public Page<FacultyDto> getAllFaculties(int page, int size, Sort.Direction sort, String sortBy) {
        long facultiesTotal = facultyRepository.count();
        if((long) page * size > facultiesTotal) {
            page = 1;
            size = 5;
        }

        Sort.Direction sortDirection = sort != null ? sort : Sort.Direction.DESC;
        Page<Faculty> facultyPage = facultyRepository.findAll(PageRequest.of(page-1, size, Sort.by(sortDirection, sortBy)));
        List<FacultyDto> facultyDtos = facultyMapper.mapToDto(facultyPage.getContent());
        return new PageImpl<>(facultyDtos, PageRequest.of(page-1, size, Sort.by(sortDirection, sortBy)), facultiesTotal);
    }

    public FacultyDto findByName(String name) {
        Faculty faculty= facultyRepository.findByName(name);
        return facultyMapper.mapToDto(faculty);
    }

    public FacultyDto getById(Long id) {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(NoSuchFacultyException::new);
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
    public FacultyDto addFaculty(FacultyDto facultyDTO) {
        Faculty faculty = facultyMapper.mapToEntity(facultyDTO);
        return facultyMapper.mapToDto(facultyRepository.save(faculty));
    }
    @Transactional
    public Faculty editFaculty(FacultyDto facultyDTO, Long id) {
        Faculty currentFaculty = facultyRepository.findById(id).orElseThrow(NoSuchFacultyException::new);
        facultyMapper.mapToEntity(currentFaculty, facultyDTO);
        return currentFaculty;
    }

}

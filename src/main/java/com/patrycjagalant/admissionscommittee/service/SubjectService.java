package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.SubjectDto;
import com.patrycjagalant.admissionscommittee.entity.Faculty;
import com.patrycjagalant.admissionscommittee.entity.Subject;
import com.patrycjagalant.admissionscommittee.repository.SubjectRepository;
import com.patrycjagalant.admissionscommittee.service.mapper.SubjectMapper;
import com.patrycjagalant.admissionscommittee.utils.ParamValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubjectMapper mapper;
    private final SubjectMapper subjectMapper;

    public SubjectService(SubjectRepository subjectRepository, SubjectMapper mapper, SubjectMapper subjectMapper) {
        this.subjectRepository = subjectRepository;
        this.mapper = mapper;
        this.subjectMapper = subjectMapper;
    }

    public Subject addNewSubject(SubjectDto dto) {
        // Check if subject already exists
        if (subjectRepository.findByName(dto.getName()).isPresent()) {
            throw new IllegalArgumentException("Subject with name: " + dto.getName() + " already exists!");
        }
        return subjectRepository.save(mapper.mapToEntity(dto));
    }

    public Subject editSubjectName(SubjectDto dto, Long id) {
        Subject subject = subjectRepository.findById(id).orElseThrow();
        subject.setName(dto.getName());
        return subjectRepository.save(subject);
    }

    public Subject getById(String id){
        if(ParamValidator.isNumeric(id)) {
            return subjectRepository.findById(Long.parseLong(id)).orElseThrow();
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void deleteSubject(Long id) {
        // Check if subject belongs to any faculty - if so, remove connections before deleting
        Subject subject = subjectRepository.findById(id).orElseThrow();
        List<Faculty> faculties = subject.getFaculties();
        if (!faculties.isEmpty()) {
            faculties.clear();
        }
        subjectRepository.deleteById(id);
    }

    public Set<SubjectDto> getAllForFaculty(Long facultyId) {
        Set<Subject> subjects = subjectRepository.findByFaculties_id(facultyId);
        return subjectMapper.mapToDto(subjects);
    }
}

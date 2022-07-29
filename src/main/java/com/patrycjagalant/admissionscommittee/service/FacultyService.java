package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.FacultyDTO;
import com.patrycjagalant.admissionscommittee.entity.Faculty;
import com.patrycjagalant.admissionscommittee.repository.FacultyRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
   // private FacultyDTO facultyDTO;
    public List<Faculty> getAllFaculties() { return facultyRepository.findAll(); }
    public  Faculty findByName(String name) { return facultyRepository.findByName(name); }

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    // Accessible only to admin:
    public void deleteFaculty(Long id) {facultyRepository.deleteById(id);}
    public Faculty addFaculty(FacultyDTO facultyDTO) {
        //FacultyMapper.INSTANCE.facultyDTOToFaculty(facultyDTO)
        Faculty faculty = new Faculty();
        faculty.setName(facultyDTO.getName());
        faculty.setBudgetPlaces(facultyDTO.getBudgetPlaces());
        faculty.setTotalPlaces(facultyDTO.getTotalPlaces());
        return facultyRepository.save(faculty);}
    @Transactional
    public Faculty editFaculty(FacultyDTO facultyDTO, Long id) {
        Faculty currentFaculty = facultyRepository.getReferenceById(id);
        currentFaculty.setName(facultyDTO.getName());
        currentFaculty.setBudgetPlaces(facultyDTO.getBudgetPlaces());
        currentFaculty.setTotalPlaces(facultyDTO.getTotalPlaces());
        return facultyRepository.save(currentFaculty);
    }


}

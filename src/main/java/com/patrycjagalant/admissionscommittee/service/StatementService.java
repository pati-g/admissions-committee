package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.StatementDTO;
import com.patrycjagalant.admissionscommittee.entity.Statement;
import com.patrycjagalant.admissionscommittee.service.mapper.StatementMapper;
import com.patrycjagalant.admissionscommittee.repository.StatementRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;


public class StatementService {

    private final StatementRepository statementRepository;
    private final StatementMapper mapper;
    public StatementService(StatementRepository statementRepository, StatementMapper mapper) {
        this.statementRepository = statementRepository;
        this.mapper = mapper;
    }

    // Create
    public void addNewStatement(StatementDTO statementDTO) {
        Statement statement = mapper.mapToEntity(statementDTO);
        statementRepository.save(statement);
    }

    // Read request by id
    public StatementDTO getStatementById(Long id) {
        Statement statement = statementRepository.getReferenceById(id);
        return mapper.mapToDto(statement);
    }

//    // Read requests for one applicant
//    public List<StatementDTO> getAllForApplicantId(Long id) {
//        List<Statement> statements = statementRepository.findByApplicantId(id);
//        List<StatementDTO> statementDTOS = new ArrayList<>();
//        for (Statement request: statements) {
//            statementDTOS.add(StatementMapper.mapToDto(request));
//        }
//        return statementDTOS;
//    }

    // Read all requests
    public Page<Statement> getAll(int page, int size, Sort.Direction sort, String sortBy) {
        return statementRepository.findAll(PageRequest.of(page, size, Sort.by(sort, sortBy)));

    }

    // Update a request
    @Transactional
    public void editApplicationRequest(StatementDTO statementDTO, Long id) {
        Statement statement = statementRepository.getReferenceById(id);
        mapper.mapToEntity(statement, statementDTO);
        statementRepository.save(statement);
    }

    // Delete a request
    public void deleteApplicationRequest(Long id) {
        statementRepository.deleteById(id);
    }
}

package com.patrycjagalant.admissionscommittee.service.mapper;

import com.patrycjagalant.admissionscommittee.dto.StatementDto;
import com.patrycjagalant.admissionscommittee.entity.Statement;

public class StatementMapper {


    public StatementDto mapToDto(Statement statement) {
        EnrollmentRequestMapper mapper = new EnrollmentRequestMapper();

        StatementDto statementDTO = new StatementDto();
        statementDTO.setId(statement.getId());
        statementDTO.setPoints(statement.getPoints());
        statementDTO.setEnrollment(statement.getEnrollment());
        statementDTO.setApplicationRequest(mapper.mapToDto(statement.getEnrollmentRequest()));

        return statementDTO;
    }

    public Statement mapToEntity(StatementDto statementDTO) {
        Statement statement = new Statement();
        EnrollmentRequestMapper mapper = new EnrollmentRequestMapper();

        statement.setPoints(statementDTO.getPoints());
        statement.setEnrollment(statementDTO.getEnrollment());
        statement.setEnrollmentRequest(mapper.mapToEntity(statementDTO.getApplicationRequest()));

        return statement;
    }

    public void mapToEntity(Statement statement, StatementDto statementDTO) {
        Integer points = statementDTO.getPoints();
        Character enrollment = statementDTO.getEnrollment();
        EnrollmentRequestMapper mapper = new EnrollmentRequestMapper();

        if (points != null)
            statement.setPoints(points);
        if (enrollment != null)
            statement.setEnrollment(enrollment);
        if (statementDTO.getApplicationRequest() != null)
            statement.setEnrollmentRequest(mapper.mapToEntity(statementDTO.getApplicationRequest()));
    }
}

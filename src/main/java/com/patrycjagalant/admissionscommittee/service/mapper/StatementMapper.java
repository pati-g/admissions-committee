package com.patrycjagalant.admissionscommittee.service.mapper;

import com.patrycjagalant.admissionscommittee.dto.StatementDTO;
import com.patrycjagalant.admissionscommittee.entity.Statement;

public class StatementMapper {


    public StatementDTO mapToDto(Statement statement) {
        ApplicationRequestMapper mapper = new ApplicationRequestMapper();

        StatementDTO statementDTO = new StatementDTO();
        statementDTO.setId(statement.getId());
        statementDTO.setPoints(statement.getPoints());
        statementDTO.setEnrollment(statement.getEnrollment());
        statementDTO.setApplicationRequest(mapper.mapToDto(statement.getApplicationRequest()));

        return statementDTO;
    }

    public Statement mapToEntity(StatementDTO statementDTO) {
        Statement statement = new Statement();
        ApplicationRequestMapper mapper = new ApplicationRequestMapper();

        statement.setPoints(statementDTO.getPoints());
        statement.setEnrollment(statementDTO.getEnrollment());
        statement.setApplicationRequest(mapper.mapToEntity(statementDTO.getApplicationRequest()));

        return statement;
    }

    public void mapToEntity(Statement statement, StatementDTO statementDTO) {
        Integer points = statementDTO.getPoints();
        Character enrollment = statementDTO.getEnrollment();
        ApplicationRequestMapper mapper = new ApplicationRequestMapper();

        if (points != null)
            statement.setPoints(points);
        if (enrollment != null)
            statement.setEnrollment(enrollment);
        if (statementDTO.getApplicationRequest() != null)
            statement.setApplicationRequest(mapper.mapToEntity(statementDTO.getApplicationRequest()));
    }
}

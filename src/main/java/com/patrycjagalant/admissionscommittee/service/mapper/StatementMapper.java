package com.patrycjagalant.admissionscommittee.service.mapper;

import com.patrycjagalant.admissionscommittee.dto.StatementDTO;
import com.patrycjagalant.admissionscommittee.entity.Statement;

public class StatementMapper {
    private StatementMapper() {
    }

    public static StatementDTO mapToDto(Statement statement) {
        StatementDTO statementDTO = new StatementDTO();
        statementDTO.setId(statement.getId());
        statementDTO.setPoints(statement.getPoints());
        statementDTO.setEnrollment(statement.getEnrollment());
        statementDTO.setApplicationRequest(ApplicationRequestMapper.mapToDto(statement.getApplicationRequest()));

        return statementDTO;
    }

    public static Statement mapToEntity(StatementDTO statementDTO) {
        Statement statement = new Statement();

        statement.setPoints(statementDTO.getPoints());
        statement.setEnrollment(statementDTO.getEnrollment());
        statement.setApplicationRequest(ApplicationRequestMapper.mapToEntity(statementDTO.getApplicationRequest()));

        return statement;
    }

    public static void mapToEntity(Statement statement, StatementDTO statementDTO) {
        Integer points = statementDTO.getPoints();
        Character enrollment = statementDTO.getEnrollment();

        if (points != null)
            statement.setPoints(points);
        if (enrollment != null)
            statement.setEnrollment(enrollment);
        if (statementDTO.getApplicationRequest() != null)
            statement.setApplicationRequest(ApplicationRequestMapper.mapToEntity(statementDTO.getApplicationRequest()));
    }
}

package com.patrycjagalant.admissionscommittee.service.mapper;

import com.patrycjagalant.admissionscommittee.dto.ScoreDto;
import com.patrycjagalant.admissionscommittee.entity.Score;

import java.util.List;
import java.util.stream.Collectors;

public class ScoreMapper {

    public ScoreDto mapToDto(Score score) {
        if (score == null) {
            return null;
        }
        ApplicantMapper mapper = new ApplicantMapper();
        ScoreDto scoreDto = ScoreDto.builder()
                .id(score.getId())
                .applicant(mapper.mapToDtoWithoutRelations(score.getApplicant()))
                .subjectName(score.getSubjectName()).build();
        if (score.getResult() != null) {
            scoreDto.setResult(score.getResult());
        }
        return scoreDto;
    }

    public List<ScoreDto> mapToDto(List<Score> scores) {
        ScoreMapper mapper = new ScoreMapper();
        return scores.stream().map(mapper::mapToDto).collect(Collectors.toList());
    }

    public Score mapToEntity(ScoreDto scoreDTO) {
        ApplicantMapper mapper = new ApplicantMapper();
        return Score.builder()
                .applicant(mapper.mapToEntity(scoreDTO.getApplicant()))
                .result(scoreDTO.getResult())
                .subjectName(scoreDTO.getSubjectName()).build();
    }

    public void mapToEntity(Score score, ScoreDto scoreDTO) {
        Integer result = scoreDTO.getResult();
        String subject = scoreDTO.getSubjectName();
        ApplicantMapper mapper = new ApplicantMapper();

        if (scoreDTO.getApplicant() != null)
            score.setApplicant(mapper.mapToEntity(scoreDTO.getApplicant()));
        if (result != null)
            score.setResult(result);
        if (subject != null && !subject.isEmpty())
            score.setSubjectName(subject);
    }
}

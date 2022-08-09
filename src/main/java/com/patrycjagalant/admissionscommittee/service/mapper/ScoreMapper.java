package com.patrycjagalant.admissionscommittee.service.mapper;

import com.patrycjagalant.admissionscommittee.dto.ScoreDto;
import com.patrycjagalant.admissionscommittee.entity.Score;
import java.util.Set;
import java.util.stream.Collectors;

public class ScoreMapper {

    public ScoreDto mapToDto(Score score) {
        if (score == null) {
            return null;
        }
        ApplicantMapper mapper = new ApplicantMapper();
        return ScoreDto.builder()
                .id(score.getId())
                .applicant(mapper.mapToDto(score.getApplicant()))
                .result(score.getResult())
                .subjectName(score.getSubjectName()).build();
    }

    public Set<ScoreDto> mapToDto(Set<Score> scores) {
        ScoreMapper mapper = new ScoreMapper();
        return scores.stream().map(mapper::mapToDto).collect(Collectors.toSet());
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

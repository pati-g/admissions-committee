package com.patrycjagalant.admissionscommittee.service.mapper;

import com.patrycjagalant.admissionscommittee.dto.ScoreDTO;
import com.patrycjagalant.admissionscommittee.entity.Score;

public class ScoreMapper {

    public ScoreDTO mapToDto(Score score) {
        ScoreDTO scoreDTO = new ScoreDTO();
        ApplicantMapper mapper = new ApplicantMapper();
        scoreDTO.setId(score.getId());
        scoreDTO.setGradeOrScore(score.getGradeOrScore());
        scoreDTO.setApplicant(mapper.mapToDto(score.getApplicant()));
        scoreDTO.setResult(score.getResult());
        scoreDTO.setSubjectName(score.getSubjectName());

        return scoreDTO;
    }

    public Score mapToEntity(ScoreDTO scoreDTO) {
        Score score = new Score();
        ApplicantMapper mapper = new ApplicantMapper();

        score.setGradeOrScore(scoreDTO.getGradeOrScore());
        score.setApplicant(mapper.mapToEntity(scoreDTO.getApplicant()));
        score.setResult(scoreDTO.getResult());
        score.setSubjectName(scoreDTO.getSubjectName());

        return score;
    }

    public void mapToEntity(Score score, ScoreDTO scoreDTO) {
        Character gradeOrScore = scoreDTO.getGradeOrScore();
        String result = scoreDTO.getResult();
        String subject = scoreDTO.getSubjectName();
        ApplicantMapper mapper = new ApplicantMapper();

        if (scoreDTO.getApplicant() != null)
            score.setApplicant(mapper.mapToEntity(scoreDTO.getApplicant()));
        if (gradeOrScore != null)
            score.setGradeOrScore(gradeOrScore);
        if (result != null && !result.isEmpty())
            score.setResult(result);
        if (subject != null && !subject.isEmpty())
            score.setSubjectName(subject);
    }
}

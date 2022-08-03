package com.patrycjagalant.admissionscommittee.service.mapper;

import com.patrycjagalant.admissionscommittee.dto.ScoreDTO;
import com.patrycjagalant.admissionscommittee.entity.Score;

import javax.transaction.Transactional;

public class ScoreMapper {
    private ScoreMapper(){}

    @Transactional
    public static ScoreDTO mapToDto(Score score) {
        ScoreDTO scoreDTO = new ScoreDTO();
        
        scoreDTO.setGradeOrScore(score.getGradeOrScore());
        scoreDTO.setApplicant(ApplicantMapper.mapToDto(score.getApplicant()));
        scoreDTO.setResult(score.getResult());
        scoreDTO.setSubjectName(score.getSubjectName());

        return scoreDTO;
    }
    @Transactional
    public static Score mapToEntity(ScoreDTO scoreDTO) {
        Score score = new Score();

        score.setGradeOrScore(scoreDTO.getGradeOrScore());
        score.setApplicant(ApplicantMapper.mapToEntity(scoreDTO.getApplicant()));
        score.setResult(scoreDTO.getResult());
        score.setSubjectName(scoreDTO.getSubjectName());

        return score;
    }
    @Transactional
    public static void mapToEntity(Score score, ScoreDTO scoreDTO) {
        Character gradeOrScore = scoreDTO.getGradeOrScore();
        String result = scoreDTO.getResult();
        String subject = scoreDTO.getSubjectName();

        if (scoreDTO.getApplicant() != null)
            score.setApplicant(ApplicantMapper.mapToEntity(scoreDTO.getApplicant()));
        if (gradeOrScore != null)
            score.setGradeOrScore(gradeOrScore);
        if (result != null && !result.isEmpty())
            score.setResult(result);
        if (subject != null && !subject.isEmpty())
            score.setSubjectName(subject);
    }
}

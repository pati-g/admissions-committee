package com.patrycjagalant.admissionscommittee.service.mapper;

import com.patrycjagalant.admissionscommittee.dto.ScoreDto;
import com.patrycjagalant.admissionscommittee.entity.Score;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ScoreMapper {

    public ScoreDto mapToDto(Score score) {
        log.debug("Score entity before mapping: {}", score);
        if (score == null) {
            log.warn("Score is null!");
            return null;
        }
        return ScoreDto.builder()
                .id(score.getId())
                .applicantId(score.getApplicant().getId())
                .result(score.getResult())
                .subjectName(score.getSubjectName()).build();
    }

    public List<ScoreDto> mapToDto(List<Score> scores) {
        log.debug("Mapping List<Score>");
        return scores.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public Score mapToEntity(ScoreDto scoreDTO) {
        log.debug("Score DTO before mapping: {}", scoreDTO);
        return Score.builder()
                .result(scoreDTO.getResult())
                .subjectName(scoreDTO.getSubjectName()).build();
    }

    public void mapToEntity(Score score, ScoreDto scoreDTO) {
        log.debug("Score entity: {} and DTO: {} before mapping", score, scoreDTO);
        Integer result = scoreDTO.getResult();
        String subject = scoreDTO.getSubjectName();
        if (result != null)
            score.setResult(result);
        if (subject != null && !subject.isEmpty())
            score.setSubjectName(subject);
    }
}

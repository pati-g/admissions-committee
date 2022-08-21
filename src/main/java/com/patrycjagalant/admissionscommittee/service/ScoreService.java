package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.ScoreDto;
import com.patrycjagalant.admissionscommittee.entity.Applicant;
import com.patrycjagalant.admissionscommittee.entity.Score;
import com.patrycjagalant.admissionscommittee.exceptions.ScoreAlreadyInListException;
import com.patrycjagalant.admissionscommittee.repository.ApplicantRepository;
import com.patrycjagalant.admissionscommittee.repository.ScoreRepository;
import com.patrycjagalant.admissionscommittee.service.mapper.ScoreMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScoreService {

    private final ScoreRepository scoreRepository;
    private final ScoreMapper mapper;
    private final ApplicantRepository applicantRepository;

    public void addNewScore(ScoreDto scoreDTO) throws ScoreAlreadyInListException {
        Score newScore = mapper.mapToEntity(scoreDTO);
        setApplicantToScoreEntity(scoreDTO, newScore);
        scoreRepository.save(newScore);
    }

    private void setApplicantToScoreEntity(ScoreDto scoreDTO, Score newScore) throws ScoreAlreadyInListException {
        Applicant applicant = applicantRepository.findById(scoreDTO.getApplicantId()).orElseThrow();
        List<String> applicantScores = applicant.getScores().stream()
                .map(Score::getSubjectName)
                .collect(Collectors.toList());
        if (applicantScores.contains(newScore.getSubjectName())) {
            throw new ScoreAlreadyInListException();
        }
        newScore.setApplicant(applicant);
    }

    public List<ScoreDto> getAllForApplicantId(Long applicantID) {
        List<Score> scores = scoreRepository.findByApplicantId(applicantID);
        return scores.stream().map(mapper::mapToDto).collect(Collectors.toList());
    }

    @Transactional
    public void editScore(ScoreDto scoreDTO, Long id) throws ScoreAlreadyInListException {
        Score score = scoreRepository.getReferenceById(id);
        mapper.mapToEntity(score, scoreDTO);
        setApplicantToScoreEntity(scoreDTO, score);
        scoreRepository.save(score);
    }

    public void deleteScore(Long id) {
        scoreRepository.deleteById(id);
    }
}

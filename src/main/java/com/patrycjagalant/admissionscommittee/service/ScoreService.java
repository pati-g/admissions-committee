package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.ScoreDTO;
import com.patrycjagalant.admissionscommittee.entity.Score;
import com.patrycjagalant.admissionscommittee.service.mapper.ScoreMapper;
import com.patrycjagalant.admissionscommittee.repository.ScoreRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class ScoreService {

    private final ScoreRepository scoreRepository;
    private final ScoreMapper mapper;
    public ScoreService(ScoreRepository scoreRepository, ScoreMapper mapper) {
        this.scoreRepository = scoreRepository;
        this.mapper = mapper;
    }

    // Create a new score
    public void addNewScore(ScoreDTO scoreDTO) {
        Score newScore = mapper.mapToEntity(scoreDTO);
        scoreRepository.save(newScore);
    }

    // Add many scores at once
    public void addListOfScores(List<ScoreDTO> scoreDTOS) {
        List<Score> scores = new ArrayList<>();
        for (ScoreDTO scoreDTO: scoreDTOS) {
            scores.add(mapper.mapToEntity(scoreDTO));
        }
        scoreRepository.saveAll(scores);
    }

    // Read a specific score
    public ScoreDTO getScoreById(Long id) {
        Score score = scoreRepository.getReferenceById(id);
        return mapper.mapToDto(score);
    }

    // Read all scores for specific applicant
    public List<ScoreDTO> getScoresForApplicant(Long applicantID) {
        List<Score> scores = scoreRepository.findByApplicantId(applicantID);
        List<ScoreDTO> scoreDTOS = new ArrayList<>();
        for (Score score: scores) {
            scoreDTOS.add(mapper.mapToDto(score));
        }
        return scoreDTOS;
    }

    // Read all scores?

    // Edit(update) score
    @Transactional
    public void updateScore(ScoreDTO scoreDTO, Long id) {
        Score score = scoreRepository.getReferenceById(id);
        mapper.mapToEntity(score, scoreDTO);
        scoreRepository.save(score);
    }
    // Delete a score
    public void deleteScore(Long id) {
        scoreRepository.deleteById(id);
    }
}

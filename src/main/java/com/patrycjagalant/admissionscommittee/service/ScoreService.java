package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.ScoreDto;
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
    public void addNewScore(ScoreDto scoreDTO) {
        Score newScore = mapper.mapToEntity(scoreDTO);
        scoreRepository.save(newScore);
    }

    // Add many scores at once
    public void addListOfScores(List<ScoreDto> scoreDtos) {
        List<Score> scores = new ArrayList<>();
        for (ScoreDto scoreDTO: scoreDtos) {
            scores.add(mapper.mapToEntity(scoreDTO));
        }
        scoreRepository.saveAll(scores);
    }

    // Read a specific score
    public ScoreDto getScoreById(Long id) {
        Score score = scoreRepository.getReferenceById(id);
        return mapper.mapToDto(score);
    }

    // Read all scores for specific applicant
    public List<ScoreDto> getScoresForApplicant(Long applicantID) {
        List<Score> scores = scoreRepository.findByApplicantId(applicantID);
        List<ScoreDto> scoreDtos = new ArrayList<>();
        for (Score score: scores) {
            scoreDtos.add(mapper.mapToDto(score));
        }
        return scoreDtos;
    }

    // Read all scores?

    // Edit(update) score
    @Transactional
    public void updateScore(ScoreDto scoreDTO, Long id) {
        Score score = scoreRepository.getReferenceById(id);
        mapper.mapToEntity(score, scoreDTO);
        scoreRepository.save(score);
    }
    // Delete a score
    public void deleteScore(Long id) {
        scoreRepository.deleteById(id);
    }
}

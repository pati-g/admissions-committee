package com.patrycjagalant.admissionscommittee.repository;
import com.patrycjagalant.admissionscommittee.entity.Score;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ScoreRepositoryTests {

    @Autowired
    private ScoreRepository scoreRepository;

    @Test
    void findByApplicantIdReturnsCorrectListOfScores() {
        List<Score> scores = scoreRepository.findByApplicantId(22L);
//        Score physics = new Score(null, null, "Physics", 70);
        List<String> scoreNames = List.of("Polish", "Physics", "Maths");
        Condition<Score> correctName = new Condition<>(score->
                scoreNames.contains(score.getSubjectName()), "correctName");
        scores.forEach(score->assertThat(score).has(correctName));
    }

    @Test
    void findByApplicantIdReturnsEmptySetIfWrong() {
        List<Score> scores = scoreRepository.findByApplicantId(0L);
        assertThat(scores).isEmpty();
    }

}

package com.patrycjagalant.admissionscommittee.repository;

import com.patrycjagalant.admissionscommittee.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;
    private String email;
    private String username;

    @BeforeEach
    void setUp(){
        email = "nr_six@test.com";
        username = "six";
    }
    @Test
    void findByEmailReturnsCorrectEntity() {
        User user = userRepository.findByEmail(email);
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getUsername()).isEqualTo("six");
        assertThat(user.getId()).isEqualTo(600L);
    }

    @Test
    void whenFindByUsernameAndCompareToCopyThenTrue() {
        User user = userRepository.findByUsername(username);
        assertThat(user.getId()).isEqualTo(600L);
        assertThat(user.getEmail()).isEqualTo(email);
    }

    @Test
    void whenFindByNonExistingUsernameThenFalse() {
        User user = userRepository.findByUsername("dummy_user");
        assertThat(user).isNull();
    }

    @Test
    void whenFindByNonExistingEmailThenFalse() {
        User user = userRepository.findByEmail(null);
        assertThat(user).isNull();
    }
}

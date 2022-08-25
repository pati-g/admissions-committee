package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.UserDto;
import com.patrycjagalant.admissionscommittee.entity.User;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchUserException;
import com.patrycjagalant.admissionscommittee.exceptions.UserAlreadyExistException;
import com.patrycjagalant.admissionscommittee.repository.UserRepository;
import com.patrycjagalant.admissionscommittee.service.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static com.patrycjagalant.admissionscommittee.utils.Constants.COULD_NOT_FIND_USER_WITH_ID;
import static com.patrycjagalant.admissionscommittee.utils.Constants.COULD_NOT_FIND_USER_WITH_USERNAME;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = UserService.class)
class UserServiceTests {

    @Mock
    private UserRepository userRepository;
    private UserService userService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    private UserDto userDto;
    private User user;

    @BeforeEach
    void setUp() {
        this.userService = new UserService(userRepository, passwordEncoder, userMapper);
        userDto = UserDto.builder().username("myusername")
                .email("onetwo@three.com").password("password").matchingPassword("password").build();
        user = User.builder().id(10L).username("myusername")
                .email("onetwo@three.com").password("password").isEnabled(true).build();
    }

    @Test
    void whenRegisteringWithCorrectData_ThenSuccessful() throws UserAlreadyExistException {
        Mockito.when(passwordEncoder.encode("password")).thenReturn("password");
        Mockito.when(userRepository.save(any())).thenReturn(user);

        assertThat(userService.registerUser(userDto)).isPositive();
    }

    @Test
    void whenTryingToRegisterUsernameThatExists_ThenExceptionIsThrown() {
        userDto.setUsername("one");
        Mockito.when(userRepository.findByUsername("one"))
                .thenReturn(Optional.ofNullable(User.builder().username("one").build()));
        assertThatExceptionOfType(UserAlreadyExistException.class)
                .isThrownBy(()->userService.registerUser(userDto)).withMessage("Username: "
                + userDto.getUsername() + " is taken");
    }

    @Test
    void whenChangeBlockedStatus_ThenReturnUserStatusIsChanged() {
        Mockito.when(userRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.ofNullable(user));
        userService.changeBlockedStatus(userDto.getUsername());
        assertThat(user.isBlocked()).isTrue();

        userService.changeBlockedStatus(userDto.getUsername());
        assertThat(user.isBlocked()).isFalse();
    }

    @Test
    void whenTryingToFindUserByWrongId_ThenExceptionIsThrown() {
        Long id = 69L;
        Mockito.when(userRepository.findById(id))
                .thenReturn(Optional.empty());
        assertThatExceptionOfType(NoSuchUserException.class)
                .isThrownBy(()->userService.findById(id)).withMessage(COULD_NOT_FIND_USER_WITH_ID + id);
    }

    @Test
    void whenFindingUserById_ThenSuccess() {
        Long id = 10L;
        Mockito.when(userRepository.findById(id))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(userMapper.mapToDto(user)).thenReturn(userDto);

        assertThat(userService.findById(id)).isInstanceOf(UserDto.class);
    }

    @Test
    void whenTryingToFindUserByWrongUsername_ThenExceptionIsThrown() {
        String username = "myUsername";
        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());
        assertThatExceptionOfType(NoSuchUserException.class)
                .isThrownBy(()->userService.findByUsername(username))
                .withMessage(COULD_NOT_FIND_USER_WITH_USERNAME + username);
    }

    @Test
    void whenFindingUserByUsername_ThenSuccess() {
        String username = "myusername";

        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(userMapper.mapToDto(user)).thenReturn(userDto);

        assertThat(userService.findByUsername(username)).isInstanceOf(UserDto.class);
    }
}

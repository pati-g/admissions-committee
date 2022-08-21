package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.UserDto;
import com.patrycjagalant.admissionscommittee.dto.other.UserDtoForEditing;
import com.patrycjagalant.admissionscommittee.entity.User;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchUserException;
import com.patrycjagalant.admissionscommittee.exceptions.UserAlreadyExistException;
import com.patrycjagalant.admissionscommittee.repository.UserRepository;
import com.patrycjagalant.admissionscommittee.service.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
//@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        super();
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.isBlank()) {
            throw new UsernameNotFoundException("Please enter username to log in");
        }
        User user;
        if (username.contains("@")) {
            user = this.userRepository.findByEmail(username);
        } else {
            user = this.userRepository.findByUsername(username);
        }
        if (user == null) {
            throw new UsernameNotFoundException("Can't find user: " + username);
        }
        return user;
    }

    public Long registerUser(UserDto userDto) throws UserAlreadyExistException {
        if (usernameExists(userDto.getUsername())) {
            throw new UserAlreadyExistException("Username: "
                    + userDto.getUsername() + " is taken");
        }
        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            throw new UserAlreadyExistException("User with email: "
                    + userDto.getEmail() + " already exists");
        }
        String password = userDto.getPassword();
        User newuser = User.builder()
                .email(userDto.getEmail())
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(password))
                .role(userDto.getRole())
                .isEnabled(true)
                .build();
        return userRepository.save(newuser).getId();
    }

    @Transactional
    public boolean changeBlockedStatus(String username) {
        User user = userRepository.findByUsername(username);
        user.setBlocked(!user.isBlocked());
        userRepository.save(user);
        return user.isBlocked();
    }

    private boolean usernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null)
            return null;
        return mapper.mapToDto(user);
    }

    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null)
            return null;
        return mapper.mapToDto(user);
    }

    @Transactional
    public void editUser(UserDtoForEditing userDto) throws UserAlreadyExistException, NoSuchUserException {
        String password = userDto.getPassword();
        if (password != null && !password.isBlank()) {
            userDto.setPassword(passwordEncoder.encode(password));
        }
        User user = userRepository.findByUsername(userDto.getUsername());
        if (user == null) {
            throw new NoSuchUserException();
        }
        if (!user.getEmail().equalsIgnoreCase(userDto.getEmail()) && userRepository.findByEmail(userDto.getEmail()) != null) {
            throw new UserAlreadyExistException("User with email: "
                    + userDto.getEmail() + " already exists");
        }
        mapper.mapToEntity(userDto, user);
        userRepository.save(user);
    }
}

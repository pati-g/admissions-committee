package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.UserDto;
import com.patrycjagalant.admissionscommittee.entity.User;
import com.patrycjagalant.admissionscommittee.exceptions.UserAlreadyExistException;
import com.patrycjagalant.admissionscommittee.repository.UserRepository;
import com.patrycjagalant.admissionscommittee.service.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        super();
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username);
        if (user == null) {
            user = this.userRepository.findByEmail(username);
            if (user == null) {
                throw new UsernameNotFoundException("Can't find user: " + username);
            }
        }
        return user;
    }

    public Long registerUser(UserDto userDto) throws UserAlreadyExistException {
        if (emailExists(userDto.getEmail())) {
            throw new UserAlreadyExistException("Account with that email address: "
                    + userDto.getEmail() + " already exists");
        }

        if (usernameExists(userDto.getUsername())) {
            throw new UserAlreadyExistException("Username: "
                    + userDto.getEmail() + " is taken");
        }

        String password = userDto.getPassword();
        String passwordEncoded = passwordEncoder.encode(password);
        User user = User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(passwordEncoded)
                .role(userDto.getRole()).build();
        return userRepository.save(user).getId();
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    private boolean usernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return null;
        UserMapper mapper = new UserMapper();
        return mapper.mapToDTO(user);
    }

    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null)
            return null;
        UserMapper mapper = new UserMapper();
        return mapper.mapToDTO(user);
    }
}

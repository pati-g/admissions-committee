package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.UserDto;
import com.patrycjagalant.admissionscommittee.entity.User;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchApplicantException;
import com.patrycjagalant.admissionscommittee.exceptions.UserAlreadyExistException;
import com.patrycjagalant.admissionscommittee.repository.UserRepository;
import com.patrycjagalant.admissionscommittee.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

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
        if (usernameExists(userDto.getUsername())) {
            throw new UserAlreadyExistException("Username: "
                    + userDto.getEmail() + " is taken");
        }
        String password = userDto.getPassword();
        String passwordEncoded = passwordEncoder.encode(password);
        User user = mapper.mapToEntity(userDto, passwordEncoded);
        return userRepository.save(user).getId();
    }

    @Transactional
    public boolean changeBlockedStatus(Long id) {
        User user = userRepository.getReferenceById(id);
        user.setBlocked(!user.isBlocked());
        userRepository.save(user);
        return user.isBlocked();
    }

    private boolean usernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return null;
        return mapper.mapToDto(user);
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
    public void editUser(UserDto userDto) throws NoSuchApplicantException {
        String password = userDto.getPassword();
        if (password != null) {
            userDto.setPassword(passwordEncoder.encode(password));
        }
        User user = userRepository.findByUsername(userDto.getUsername());
        if (user == null) {
            throw new NoSuchApplicantException();
        }
        mapper.mapToEntity(userDto, user);
        userRepository.save(user);
    }
}

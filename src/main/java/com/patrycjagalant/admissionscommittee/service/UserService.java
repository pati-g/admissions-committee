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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(email);
        if (user == null)
            throw new UsernameNotFoundException("Can't find user with e-mail: " + email);
        return user;
    }

    public Long registerUser(UserDto userDto) throws UserAlreadyExistException {
        if (emailExists(userDto.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: "
                    + userDto.getEmail());
        }

            String password = userDto.getPassword();
            String passwordEncoded = passwordEncoder.encode(password);
            User user = new User();
            user.setEmail(userDto.getEmail());
            user.setPassword(passwordEncoded);
            user.setRole(userDto.getRole());
            return userRepository.save(user).getId();
    }
    private boolean emailExists(String email) { return userRepository.findByEmail(email) != null; }

    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return null;
        UserMapper mapper = new UserMapper();
        return mapper.mapToDTO(user);
    }
}

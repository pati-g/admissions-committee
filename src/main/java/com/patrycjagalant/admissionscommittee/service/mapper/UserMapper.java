package com.patrycjagalant.admissionscommittee.service.mapper;

import com.patrycjagalant.admissionscommittee.annotations.Mapper;
import com.patrycjagalant.admissionscommittee.dto.UserDto;
import com.patrycjagalant.admissionscommittee.dto.other.UserDtoForEditing;
import com.patrycjagalant.admissionscommittee.entity.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Mapper
public class UserMapper {

    public UserDto mapToDto(User user) {
        log.debug("User entity before mapping: {}", user);
        if (user == null) {
            log.warn("User is null!");
            return null;
        }
        return UserDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .id(user.getId())
                .role(user.getRole())
                .isBlocked(user.isBlocked())
                .isEnabled(user.isEnabled()).build();
    }

    public User mapToEntity(UserDto userDto) {
        log.debug("User DTO before mapping: {}", userDto);
        return User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .role(userDto.getRole()).build();
    }

    public User mapToEntity(UserDtoForEditing userDto, User user) {
        log.debug("User entity: {} and DTO: {} before mapping", user, userDto);
        String email = userDto.getEmail();
        String password = userDto.getPassword();
        if (email != null && !email.isBlank()) {
            user.setEmail(email);
        }
        if (password != null && !password.isBlank()) {
            user.setPassword(password);
        }
        return user;
    }
}

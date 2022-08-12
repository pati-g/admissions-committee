package com.patrycjagalant.admissionscommittee.service.mapper;

import com.patrycjagalant.admissionscommittee.dto.UserDto;
import com.patrycjagalant.admissionscommittee.entity.User;

public class UserMapper {

    public UserDto mapToDTO(User user) {
        if (user == null) {
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
}

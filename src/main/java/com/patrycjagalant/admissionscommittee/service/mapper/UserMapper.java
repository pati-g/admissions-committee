package com.patrycjagalant.admissionscommittee.service.mapper;

import com.patrycjagalant.admissionscommittee.dto.UserDto;
import com.patrycjagalant.admissionscommittee.entity.User;

public class UserMapper {

    public UserDto mapToDTO (User user) {
        if (user == null)
            return null;
        UserDto userDTO = new UserDto();
        userDTO.setEmail(user.getEmail());
        userDTO.setId(user.getId());
        userDTO.setRole(user.getRole());
        userDTO.setBlocked(user.isBlocked());
        userDTO.setEnabled(user.isEnabled());
        return userDTO;
    }
}

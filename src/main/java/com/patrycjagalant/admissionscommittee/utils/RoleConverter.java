package com.patrycjagalant.admissionscommittee.utils;

import com.patrycjagalant.admissionscommittee.entity.Role;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, String> {

    @Override
    public String convertToDatabaseColumn(Role role) {
        if (role == null) {
            return null;
        }
        return role.getRoleName();
    }

    @Override
    public Role convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return Stream.of(Role.values())
                .filter(c -> c.getRoleName().equals(code.toUpperCase()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

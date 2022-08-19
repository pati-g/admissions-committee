package com.patrycjagalant.admissionscommittee.utils.converters;

import com.patrycjagalant.admissionscommittee.entity.Status;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class StatusConverter implements AttributeConverter<Status, String> {
    @Override
    public String convertToDatabaseColumn(Status status) {
        if (status == null) {
            return null;
        }
        return status.getStatusName();
    }

    @Override
    public Status convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }
        return Stream.of(Status.values())
                .filter(c -> c.getStatusName().equals(s.toUpperCase()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

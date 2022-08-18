package com.patrycjagalant.admissionscommittee.utils.validators;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Component
public class FileValidator {
    public static final long MAXSIZE = 5242880;

    public boolean validate(MultipartFile file) {
        return !file.isEmpty() && isSupportedContentType(Objects.requireNonNull(file.getContentType())) && file.getSize() <= MAXSIZE;
    }

    private boolean isSupportedContentType(String contentType) {
        return contentType.equalsIgnoreCase("image/png")
                || contentType.equalsIgnoreCase("image/jpg")
                || contentType.equalsIgnoreCase("image/jpeg")
                || contentType.equalsIgnoreCase("pdf");
    }
}

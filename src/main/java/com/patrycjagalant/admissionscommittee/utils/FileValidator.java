package com.patrycjagalant.admissionscommittee.utils;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Component
public class FileValidator implements Validator {
    public static final long MAXSIZE = 5242880;

    @Override
    public boolean supports(Class<?> clazz) {
        return MultipartFile.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MultipartFile file = (MultipartFile) target;
        if (file.isEmpty()) {
            errors.rejectValue("file", "upload.file.required");
        } else if (!isSupportedContentType(Objects.requireNonNull(file.getContentType()))) {
            errors.rejectValue("file", "upload.invalid.file.type");
        } else if (file.getSize() > MAXSIZE) {
            errors.rejectValue("file", "upload.exceeded.file.size");
        }
    }

    private boolean isSupportedContentType(String contentType) {
        return contentType.equalsIgnoreCase("image/png")
                || contentType.equalsIgnoreCase("image/jpg")
                || contentType.equalsIgnoreCase("image/jpeg")
                || contentType.equalsIgnoreCase("pdf");
    }
}

package com.patrycjagalant.admissionscommittee.utils.validators;

import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
/**
 * The {@code FileValidator} class is a utility class for validating file input.
 * It provides a basic method to validate file input of type {@link MultipartFile},
 * a static final field {@link #MAXSIZE} that holds the byte value of 5MB
 * and a helper method {@link #isSupportedContentType(String)}
 * to verify if the file is either an image or a PDF.
 * @author  Patrycja Galant
 * @see MultipartFile
 */
@UtilityClass
public class FileValidator {
    /**
     * A constant holding the maximum allowed size for the validated file, which is 5 MB.
     */
    public static final long MAXSIZE = 5242880;
    /**
     * @return true if the validated file is not empty and complies with the constraints' rules
     */
    public boolean validate(MultipartFile file) {
        return !file.isEmpty()
                && isSupportedContentType(Objects.requireNonNull(file.getContentType()))
                && file.getSize() <= MAXSIZE;
    }
    /**
     * @return true if the validated file is an image or a PDF
     */
    private boolean isSupportedContentType(String contentType) {
        return contentType.equalsIgnoreCase("image/png")
                || contentType.equalsIgnoreCase("image/jpg")
                || contentType.equalsIgnoreCase("image/jpeg")
                || contentType.equalsIgnoreCase("application/pdf");
    }
}

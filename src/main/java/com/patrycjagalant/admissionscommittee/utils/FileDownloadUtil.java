package com.patrycjagalant.admissionscommittee.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileDownloadUtil {
    @Value("${app.upload.dir:${user.home}}")
    public String uploadPathString;
    private Path foundFile;

    public Resource getFileAsResource(String fileCode) throws IOException {
        Path dirPath = Paths.get(uploadPathString);

        try (Stream<Path> files = Files.list(dirPath)) {
            files.forEach(file -> {
                if (file.getFileName().toString().startsWith(fileCode)) {
                    foundFile = file;
                }
            });
        }
        if (foundFile != null) {
            return new UrlResource(foundFile.toUri());
        }

        return null;
    }
}

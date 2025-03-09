package com.ppu.ppu.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class FileUtil {
    @Value("${STATIC_FILE_PATH}")
    private String filePath;

    public FileUtil(){}

    public String generateFileName(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uniqueName = UUID.randomUUID().toString();
        return uniqueName + extension;
    }

    public Optional<String> storeFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }

        String originalFilename = file.getOriginalFilename();
        String storeFileName = generateFileName(originalFilename);

        file.transferTo(new File(filePath + storeFileName));
        return Optional.of("/static/" + storeFileName);
    }
}

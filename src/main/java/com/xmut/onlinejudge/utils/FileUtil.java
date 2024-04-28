package com.xmut.onlinejudge.utils;

import com.xmut.onlinejudge.base.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

public class FileUtil {

    public static Result<String> upload(MultipartFile file, String uploadDir, String uriPrefix) {
        Result<String> result = new Result<>();
        if (file.isEmpty()) {
            result.error("Invalid file content");
            return result;
        }
        if (file.getSize() > 2 * 1024 * 1024) {
            result.error("Picture is too large");
            return result;
        }
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        if (!Arrays.asList(".gif", ".jpg", ".jpeg", ".bmp", ".png").contains(suffix)) {
            result.error("Unsupported file format");
            return result;
        }
        String name = UUID.randomUUID().toString().replace("-", "") + suffix;
        try {
            byte[] bytes = file.getBytes();
            Path uploadPath = Paths.get(uploadDir, name);
            Files.write(uploadPath, bytes);
            result.success(uriPrefix + "/" + name, "Success");
        } catch (IOException e) {
            e.printStackTrace();
            result.error("Failed to upload");
        }
        return result;
    }
}

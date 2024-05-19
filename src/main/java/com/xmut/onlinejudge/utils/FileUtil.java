package com.xmut.onlinejudge.utils;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmut.onlinejudge.base.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

@Component
public class FileUtil {

    private final String AVATAR_URI_PREFIX = "/public/avatar"; // 头像URI前缀
    @Value("${files.upload.avatar.path}")
    private String AVATAR_UPLOAD_DIR;

    public Result<String> uploadAvatar(MultipartFile file) {
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
            Path uploadPath = Paths.get(AVATAR_UPLOAD_DIR, name);
            Files.write(uploadPath, bytes);
            result.success(AVATAR_URI_PREFIX + "/" + name, "Success");
        } catch (IOException e) {
            e.printStackTrace();
            result.error("Failed to uploadAvatar");
        }
        return result;
    }

    public JSONObject processZip(MultipartFile file, boolean spj, String dir) throws IOException, NoSuchAlgorithmException {
        if (file.isEmpty()) {
            throw new RuntimeException("Uploaded file is empty");
        }
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        String fileName = UUID.randomUUID().toString().replace("-", "") + ".zip";
        File dest = new File(new File(dir).getAbsolutePath(), fileName);
        // Transfer the file to the destination path
        file.transferTo(dest);

        try (ZipFile zipFile = new ZipFile(dest)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            List<String> nameList = new ArrayList<>();
            while (entries.hasMoreElements()) {
                nameList.add(entries.nextElement().getName());
            }

            List<String> testCaseList = filterNameList(nameList, spj, "");
            if (testCaseList.isEmpty()) {
                throw new RuntimeException("Empty file");
            }

            String testCaseId = fileName.substring(0, fileName.lastIndexOf('.'));
            File testCaseDir = new File(dir, testCaseId);
            if (!testCaseDir.exists()) {
                testCaseDir.mkdirs();
            }

            Map<String, Integer> sizeCache = new HashMap<>();
            Map<String, String> md5Cache = new HashMap<>();
            MessageDigest md = MessageDigest.getInstance("MD5");

            for (String item : testCaseList) {
                File fileItem = new File(testCaseDir, item);
                try (InputStream in = zipFile.getInputStream(zipFile.getEntry(item));
                     ByteArrayOutputStream bos = new ByteArrayOutputStream();
                     OutputStream out = new FileOutputStream(fileItem)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        bos.write(buffer, 0, bytesRead);
                    }
                    byte[] content = bos.toByteArray();
                    content = new String(content).replace("\r\n", "\n").getBytes();
                    sizeCache.put(item, content.length);
                    if (item.endsWith(".out")) {
                        md5Cache.put(item, toHex(md.digest(content)));
                    }
                    out.write(content);
                }
            }

            Map<String, Object> testCaseInfo = new HashMap<>();
            testCaseInfo.put("spj", spj);
            testCaseInfo.put("test_cases", new HashMap<>());

            List<Map<String, Object>> info = new ArrayList<>();
            if (spj) {
                for (int i = 0; i < testCaseList.size(); i++) {
                    String item = testCaseList.get(i);
                    Map<String, Object> data = new HashMap<>();
                    data.put("input_name", item);
                    data.put("input_size", sizeCache.get(item));
                    info.add(data);
                    ((Map<String, Object>) testCaseInfo.get("test_cases")).put(String.valueOf(i + 1), data);
                }
            } else {
                for (int i = 0; i < testCaseList.size(); i += 2) {
                    String inputName = testCaseList.get(i);
                    String outputName = testCaseList.get(i + 1);
                    Map<String, Object> data = new HashMap<>();
                    data.put("stripped_output_md5", md5Cache.get(outputName));
                    data.put("input_size", sizeCache.get(inputName));
                    data.put("output_size", sizeCache.get(outputName));
                    data.put("input_name", inputName);
                    data.put("output_name", outputName);
                    info.add(data);
                    ((Map<String, Object>) testCaseInfo.get("test_cases")).put(String.valueOf((i / 2) + 1), data);
                }
            }
            ObjectMapper mapper = new ObjectMapper();
            try (Writer writer = new FileWriter(new File(testCaseDir, "info"))) {
                mapper.writerWithDefaultPrettyPrinter().writeValue(writer, testCaseInfo);
            }

            JSONObject result = new JSONObject();
            result.put("info", info);
            result.put("id", testCaseId);
            return result;
        } finally {
            if (!dest.delete()) {
                System.err.println("Failed to delete the zip file: " + dest.getAbsolutePath());
            }
        }
    }

    private List<String> filterNameList(List<String> nameList, boolean spj, String dir) {
        List<String> ret = new ArrayList<>();
        int prefix = 1;
        while (true) {
            String inName = prefix + ".in";
            String outName = prefix + ".out";
            if (spj) {
                if (nameList.contains(dir + inName)) {
                    ret.add(inName);
                    prefix++;
                } else {
                    break;
                }
            } else {
                if (nameList.contains(dir + inName) && nameList.contains(dir + outName)) {
                    ret.add(inName);
                    ret.add(outName);
                    prefix++;
                } else {
                    break;
                }
            }
        }
        return ret;
    }

    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public Path createTestCaseZip(String testCaseId, String dir) throws IOException {
        File testDir = new File(new File(dir).getAbsolutePath(), testCaseId);
        if (!testDir.exists() || !testDir.isDirectory()) {
            throw new FileNotFoundException("Test case directory does not exist");
        }

        Path zipFilePath = Paths.get(dir, "test_cases.zip");
        try (ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(zipFilePath.toFile().toPath()))) {
            File[] files = testDir.listFiles();
            if (files != null) {
                for (File fileToZip : files) {
                    zipOut.putNextEntry(new ZipEntry(fileToZip.getName()));
                    Files.copy(fileToZip.toPath(), zipOut);
                    zipOut.closeEntry();
                }
            }
        }
        return zipFilePath;
    }
}

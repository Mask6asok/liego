package com.mask.liego.tools;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ImageUpload {
    public static String imageUpload(MultipartFile file) {
//        Map<String, Object> m = new HashMap<>();
//        System.out.println(file);
        String originalFilename = file.getOriginalFilename();
//        System.out.println(originalFilename);
        String fileName = UUID.randomUUID() + ".jpeg";
        String path = "/picture/";
        File uploadFile = new File(path + fileName);
        System.out.println(uploadFile);
        try {
            file.transferTo(uploadFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        m.put("url", path + fileName);
        return path + fileName;
    }
}

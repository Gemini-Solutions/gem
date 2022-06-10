package com.gemini.gembook.controller;

import com.gemini.gembook.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletContext;
import java.io.ByteArrayOutputStream;

/**
 * Created by ankur on 30/09/20
 */
@Controller("file")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ServletContext servletContext;

    private final Logger logger = LoggerFactory.getLogger(FileController.class);

    @GetMapping
    public ResponseEntity<ByteArrayResource> getFile(@RequestParam String fileName) {
        try {
            ByteArrayOutputStream downloadInputStream = fileStorageService.loadFileAsResource(fileName);

            ByteArrayResource byteArrayResource = new ByteArrayResource(downloadInputStream.toByteArray());
            return ResponseEntity.ok()
                    .contentType(contentType(fileName))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(byteArrayResource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    private MediaType contentType(String filename) {
        String[] fileArrSplit = filename.split("\\.");
        String fileExtension = fileArrSplit[fileArrSplit.length - 1];
        switch (fileExtension) {
            case "txt":
                return MediaType.TEXT_PLAIN;
            case "png":
                return MediaType.IMAGE_PNG;
            case "pdf":
                return MediaType.APPLICATION_PDF;
            case "jpg":
            case "jfif":
            case "jpeg":
            case "pjpeg":
            case "pjp":
                return MediaType.IMAGE_JPEG;
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
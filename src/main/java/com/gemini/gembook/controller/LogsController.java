package com.gemini.gembook.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/logs")
public class LogsController{

    @GetMapping
    public ResponseEntity<Object> getLogs(
            @RequestParam(value = "lineCount", required = false) String count,
            @RequestParam(value = "fullPath") String absolutePath
    ) {
        int defaultCount = 10;
        if (!isNotNullOrBlank(absolutePath))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File Path is required");

        if (isNotNullOrBlank(count)) {
            try {
                defaultCount = Integer.parseInt(count);
            } catch (NumberFormatException e) {
                defaultCount = 10;
            }
        }

        FileSystemResource resource = new FileSystemResource(absolutePath);
        File file = resource.getFile();
        if (file.exists()) {
            List<JsonNode> logList = new ArrayList<>();
            try {
                ObjectMapper mapper = new ObjectMapper();
                StringBuilder builder = new StringBuilder();
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
                int lines = 0;
                long length = file.length();
                length--;
                randomAccessFile.seek(length);
                for (long seek = length; seek >= 0; --seek) {
                    randomAccessFile.seek(seek);
                    char c = (char) randomAccessFile.read();
                    builder.append(c);
                    if (c == '\n' && builder.length() > 1) {
                        builder = builder.reverse();
                        String string = builder.toString().replace("\n", "");
                        logList.add(mapper.readTree(string));
                        lines++;
                        builder = null;
                        builder = new StringBuilder();
                        if (lines == defaultCount)
                            break;
                    }
                }
                if (builder.length() > 0) {                        //to handle the last line edge case
                    builder = builder.reverse();
                    logList.add(mapper.readTree(builder.toString()));
                }
                builder = null;
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.OK).body(logList);
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File not found");
    }

    protected boolean isNotNullOrBlank(String s) {
        return (s != null && s.length() > 0);   //returns false if string is null/blank
    }
}

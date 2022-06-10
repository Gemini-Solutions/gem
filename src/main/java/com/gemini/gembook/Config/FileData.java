package com.gemini.gembook.Config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created on 20/08/20
 *
 * Configuration for Uploaded file data
 */

@ConfigurationProperties(prefix = "file.upload")
@Configuration("fileData")
@Getter
@Setter
public class FileData {
    private String directory;
}

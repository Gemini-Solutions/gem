package com.gemini.gembook.Config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created on 20/08/20
 *
 * Used in retrieving data from mis api
 * Reference in controller.UserController
 */

@ConfigurationProperties(prefix = "mis.data")
@Configuration("misData")
@Getter
@Setter
public class MisData {
    private String url;
    private String token;
    private String key;
}

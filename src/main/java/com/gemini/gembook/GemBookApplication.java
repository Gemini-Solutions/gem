package com.gemini.gembook;

import com.gemini.gembook.Config.FileData;
import com.gemini.gembook.Config.MisData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Application startup class
 */

@EnableConfigurationProperties({FileData.class, MisData.class})
@SpringBootApplication
@EnableScheduling
public class GemBookApplication {
	
	/**
	 * Main method to start GemBook application
	 * @param args command line arguments, if any
	 */
	public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(GemBookApplication.class, args);
        context.getBean(StartUpInit.class).init();
	}
}

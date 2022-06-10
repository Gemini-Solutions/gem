package com.gemini.gembook.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

/**
 * This class setup Swagger-UI documentation of all REST APIs in the project.
 */
@Configuration
@EnableSwagger2
@EnableWebMvc
public class SwaggerConfig implements WebMvcConfigurer {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                //.globalOperationParameters(globalParameterList())
                .select().apis(RequestHandlerSelectors.basePackage("com.gemini.gembook.controller"))
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry
                .addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    //To generate a Global Parameter in all Swagger Requests
//    private List<Parameter> globalParameterList() {
//        Parameter authTokenHeader =
//                new ParameterBuilder()
//                        .name("x-auth-token") // name of the header
//                        .modelRef(new ModelRef("string")) // data-type of the header
//                        .required(true) // required/optional
//                        .parameterType("header") // for query-param, this value can be 'query'
//                        .description("Auth Token from Google SignIn")
//                        .build();
//
//        return Collections.singletonList(authTokenHeader);
//    }

    /**
     * Enabling CORS
     * @param registry
     */
    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "PUT", "POST", "DELETE", "PATCH")
                .allowedHeaders("*");
    }
}

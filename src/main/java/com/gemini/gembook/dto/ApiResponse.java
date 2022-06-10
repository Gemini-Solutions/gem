package com.gemini.gembook.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * This class wraps the  different fields available for the response from the server.
 */
@Getter
@Setter
public class ApiResponse {
    @JsonIgnore
    private HttpStatus status;
    private String message;
    private Object object;

    public ApiResponse(HttpStatus status, Object object) {
        this.message = status.name();
        this.status = status;
        this.object = object;
    }
}

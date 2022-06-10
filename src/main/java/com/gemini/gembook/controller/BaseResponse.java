package com.gemini.gembook.controller;

import org.springframework.http.HttpStatus;

/**
 * This class wraps the  different fields available for the response from the server.
 */
public class BaseResponse {
	String message;
	HttpStatus status;
	Object object;
	
	public BaseResponse(String message, HttpStatus status, Object object) {
		this.message = message;
		this.status = status;
		this.object = object;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
	
	
}

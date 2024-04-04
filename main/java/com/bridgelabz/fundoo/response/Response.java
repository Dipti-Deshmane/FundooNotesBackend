package com.bridgelabz.fundoo.response;

import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
public class Response {
	private String message;
	private long code;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public long getCode() {
		return code;
	}
	public void setCode(long code) {
		this.code = code;
	}
}

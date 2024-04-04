package com.bridgelabz.fundoo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDTO {
    public String message;
    public Object data;
    
    public ResponseDTO(String message, Object data) {
		super();
		this.message = message;
		this.data = data;
	}
    
	public ResponseDTO() {
		super();
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getCode() {
		return data;
	}
	public void setCode(Object data) {
		this.data = data;
	}

}

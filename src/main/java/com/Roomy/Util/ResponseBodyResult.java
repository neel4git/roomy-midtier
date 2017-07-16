package com.Roomy.Util;

import java.util.List;

public class ResponseBodyResult {

	
	private int statusCode;
	private  String statusMessage;
	private List result;
	private String jwtToken;
	
	
	public ResponseBodyResult() {
	
	}
	
	public ResponseBodyResult(int statusCode,String statusMessage,String jwtToken,List result){
		
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
		this.jwtToken = jwtToken;
		this.result = result;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	
	public List getResult() {
		return result;
	}

	public void setResult(List result) {
		this.result = result;
	}

	public String getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}
	
}

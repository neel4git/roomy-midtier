package com.Roomy.domain;

public class Response {

	public Response(int statusCode, Object object, String jwtToken, Object result) {
		this.statusCode = statusCode;
		this.statusMessage = object;
		this.jwtToken = jwtToken;
		this.result = result;
	}

	public Response(String status, Object object, String jwtToken, Object result) {
		this.status = status;
		this.statusMessage = object;
		this.jwtToken = jwtToken;
		this.result = result;
	}

	public Response(String status, Object result) {
		this.status = status;
		this.result = result;
	}

	private int statusCode;
	private Object statusMessage;
	private Object result;
	private String jwtToken;
	private String status;

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public Object getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setStatusMessage(Object statusMessage) {
		this.statusMessage = statusMessage;
	}

}

package com.Roomy.Response.Domain;

public class MetaDataHoteResponse {
	private String statusCode;
	private String FailureMessage;

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getFailureMessage() {
		return FailureMessage;
	}

	public void setFailureMessage(String failureMessage) {
		FailureMessage = failureMessage;
	}

}

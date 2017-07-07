package com.Roomy.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenJsonObject {
	private String aud;
	private String sub;
	@JsonProperty
	private SourceKeyRing sourceKeyRing;
	private String iss;
	private long iat;
	public String getAud() {
		return aud;
	}
	public void setAud(String aud) {
		this.aud = aud;
	}
	public String getSub() {
		return sub;
	}
	public void setSub(String sub) {
		this.sub = sub;
	}
	public SourceKeyRing getSourceKeyRing() {
		return sourceKeyRing;
	}
	public void setSourceKeyRing(SourceKeyRing sourceKeyRing) {
		this.sourceKeyRing = sourceKeyRing;
	}
	public String getIss() {
		return iss;
	}
	public void setIss(String iss) {
		this.iss = iss;
	}
	public long getIat() {
		return iat;
	}
	public void setIat(long iat) {
		this.iat = iat;
	}

	

}

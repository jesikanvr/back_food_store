package com.store.security;

public class JWTAuthResonseDTO {

	private String AccessToken;
	private String TokenType = "Bearer";

	public JWTAuthResonseDTO(String accessToken) {
		super();
		AccessToken = accessToken;
	}

	public JWTAuthResonseDTO(String accessToken, String tokenType) {
		super();
		AccessToken = accessToken;
		TokenType = tokenType;
	}

	public String getAccessToken() {
		return AccessToken;
	}

	public void setAccessToken(String accessToken) {
		AccessToken = accessToken;
	}

	public String getTokenType() {
		return TokenType;
	}

	public void setTokenType(String tokenType) {
		TokenType = tokenType;
	}

}

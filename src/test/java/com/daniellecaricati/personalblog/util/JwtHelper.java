package com.daniellecaricati.personalblog.util;

import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.daniellecaricati.personalblog.model.UserLogin;

public class JwtHelper {
	
	private JwtHelper() {}
	
	public static String getToken(TestRestTemplate testRestTemplate, String email, String password) {
		UserLogin login = TestBuilder.createUserLogin(email, password);
		HttpEntity<UserLogin> request = new HttpEntity<>(login);
		
		ResponseEntity<UserLogin> response = testRestTemplate
				.exchange("/users/login", HttpMethod.POST, request, UserLogin.class);
		
		UserLogin body = response.getBody();
		if (body != null && body.getToken() != null) {
			return body.getToken();
		}
		throw new RuntimeException("Login failed: " + email);
	}
	
	public static <T> HttpEntity<T> createTokenRequisition(T body, String token) {
		HttpHeaders headers = new HttpHeaders();
		String cleanToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		headers.setBearerAuth(cleanToken);
		return new HttpEntity<>(body, headers);
	}
	
	public static HttpEntity<Void> createTokenRequisition(String token) {
		return createTokenRequisition(null, token);
	}
}

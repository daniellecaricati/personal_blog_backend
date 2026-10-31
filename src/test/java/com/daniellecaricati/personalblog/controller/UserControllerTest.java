package com.daniellecaricati.personalblog.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.daniellecaricati.personalblog.model.User;
import com.daniellecaricati.personalblog.repository.UserRepository;
import com.daniellecaricati.personalblog.service.UserService;
import com.daniellecaricati.personalblog.util.JwtHelper;
import com.daniellecaricati.personalblog.util.TestBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class UserControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	private static final String BASE_URL = "/users";
	private static final String USERNAME_ADMIN = "root@root.com";
	private static final String PASSWORD_ADMIN = "rootroot";
	
	private String token;
	
	@BeforeAll()
	void start() {
		userRepository.deleteAll();
		userService.createUser(TestBuilder.createUser(null, "Root", USERNAME_ADMIN, PASSWORD_ADMIN));
		token =JwtHelper.getToken(testRestTemplate, USERNAME_ADMIN, PASSWORD_ADMIN);
		
	}
	
	@Test
	@DisplayName("01 - Should create a new user successfully.")
	void shouldCreateUser() {
		//Given
		User user = TestBuilder.createUser(null, "Danielle Caricati",  "danielle_caricati@email.com", "12345678");
		
		//When
		HttpEntity<User> requisition = new HttpEntity<>(user);
		ResponseEntity<User> response = testRestTemplate.exchange(BASE_URL + "/create", HttpMethod.POST, requisition, User.class);
		
		//Then
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
	}
	
	@Test
	@DisplayName("02 - Should not allow duplicate user.")
	void shouldNotAllowDuplicateUser() {
		
		//Given
		User user = TestBuilder.createUser(null, "Danielle Caricati", "danielle_caricati@email.com", "12345678"); 
		userService.createUser(user);
		
		//When
		HttpEntity<User> requisition = new HttpEntity<>(user);
		ResponseEntity<User> response = testRestTemplate.exchange(BASE_URL + "/create", HttpMethod.POST, requisition, User.class);
		
		//Then
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNull(response.getBody());
		
	}
	
	@Test
	@DisplayName(" 03- Should update user successfully.")
	void shouldUpdateUser() {
		//Given
		User user = TestBuilder.createUser(null, "Julian Andrew", "ju_andrew@email.com", "12345678");
		Optional<User> created = userService.createUser(user);
		
		User userUpdate = TestBuilder.createUser(created.get().getId(), "Julian Dartford", "ju_dart@email.com", "12345678");
		
		//When
		HttpEntity<User> requisition = JwtHelper.createTokenRequisition(userUpdate, token);
		ResponseEntity<User> response = testRestTemplate.exchange(BASE_URL + "/update", HttpMethod.PUT, requisition, User.class);
		
		//Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
	}
	
	@Test
	@DisplayName(" 04 - Should list all users successfully.")
	void shouldListAllUsers() {
		//Given
		userService.createUser(TestBuilder.createUser(null, "Adriana Mendes", "adriana_mendes@email.com", "12345678"));
		userService.createUser(TestBuilder.createUser(null,  "Carl Viny", "carl_viny@email.com", "12345678"));
		
		//When
		HttpEntity<Void> requisition = JwtHelper.createTokenRequisition(token);
		ResponseEntity<User[]> response = testRestTemplate.exchange(BASE_URL + "/all", HttpMethod.GET, requisition, User[].class);
		
		//Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
				
	}
}

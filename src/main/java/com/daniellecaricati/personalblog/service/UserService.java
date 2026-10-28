package com.daniellecaricati.personalblog.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.daniellecaricati.personalblog.model.User;
import com.daniellecaricati.personalblog.model.UserLogin;
import com.daniellecaricati.personalblog.repository.UserRepository;
import com.daniellecaricati.personalblog.security.JwtService;

@Service
public class UserService {

		@Autowired
		private UserRepository userRepository;
		
		@Autowired
		private JwtService jwtService;
		
		@Autowired
		private AuthenticationManager authenticationManager;
		
		@Autowired
		private PasswordEncoder passwordEncoder;
		
		public List<User> getAll() {
			return userRepository.findAll();
		}
		
		public Optional<User> getById(Long id) {
			return userRepository.findById(id);
		}
		
		public Optional<User> createUser(User user) {
			if(userRepository.findByUser(user.getUser()).isPresent()) {
				return Optional.empty();
			}
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setId(null);
			
			return Optional.of(userRepository.save(user));
			
		}
		
		public Optional<User> updateUser(User user) {
			if (!userRepository.findById(user.getId()).isPresent()) {
				return Optional.empty();
			}
			Optional<User> existingUser = userRepository.findByUser(user.getUser());
			if(existingUser.isPresent() && !existingUser.get().getId().equals(user.getId())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists.", null);
			}
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			return Optional.of(userRepository.save(user));
		}
		
		public Optional<UserLogin> authenticateUser(Optional<UserLogin> userLogin) {
			if(!userLogin.isPresent()) {
				return Optional.empty();
			}
			UserLogin login = userLogin.get();
			try {
				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getUser(), login.getPassword()));
				return userRepository.findByUser(login.getUser()) .map(user -> buildResponseLogin(login, user));
				
			}catch (Exception e) {
				return Optional.empty();
			}
		}
		
		private UserLogin buildResponseLogin(UserLogin userLogin, User user) {
			userLogin.setId(user.getId());
			userLogin.setName(user.getName());
			userLogin.setPhoto(user.getPhoto());
			userLogin.setPassword("");
			userLogin.setToken(createToken(user.getUser()));
			return userLogin;
		}
		
		private String createToken(String user) {
			return "Bearer " + jwtService.generateToken(user);		}
}

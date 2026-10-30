package com.daniellecaricati.personalblog.util;

import com.daniellecaricati.personalblog.model.User;
import com.daniellecaricati.personalblog.model.UserLogin;

public class TestBuilder {
	
	public static User createUser(Long id, String name, String username, String password) {
		User newUser = new User();
		newUser.setId(id);
		newUser.setName(name);
		newUser.setUsername(username);
		newUser.setPassword(password);
		newUser.setPhoto("-");
		return newUser;
	}
	
	public static UserLogin createUserLogin(String username, String password) {
		UserLogin userLogin = new UserLogin();
		userLogin.setUsername(username);
		userLogin.setPassword(password);
		return userLogin;
	}
}

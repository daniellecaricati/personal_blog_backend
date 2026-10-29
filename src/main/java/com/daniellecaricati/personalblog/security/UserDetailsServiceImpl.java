package com.daniellecaricati.personalblog.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.daniellecaricati.personalblog.model.User;
import com.daniellecaricati.personalblog.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {
		
		if(username == null || username.trim().isEmpty()) {
			throw new UsernameNotFoundException("Username (email) must not be empty.");
		}
		
		Optional<User> user = userRepository.findByUsername(username);
		
		if (user.isPresent()) {
			return new UserDetailsImpl(user.get());
		} else {
			throw new UsernameNotFoundException("Username not found: " + username);
		}
	}
}

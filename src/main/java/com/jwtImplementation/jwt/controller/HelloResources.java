package com.jwtImplementation.jwt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jwtImplementation.jwt.model.AuthenticationRequest;
import com.jwtImplementation.jwt.model.AuthenticationResponse;
import com.jwtImplementation.jwt.model.JwtUtil;

@RestController
public class HelloResources {
	
	@Autowired
	AuthenticationManager authenticationManger;

	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	JwtUtil jwtUtil;
	
	private final Logger log = LoggerFactory.getLogger(HelloResources.class);
	
	@GetMapping(value = "/hello")
	private String getGreeting() {
		return "Hello nitin";
	}

	@PostMapping(value="/authenticate")
	public ResponseEntity<?> getAuthenticateToken(@RequestBody AuthenticationRequest 
			authenticationRequest) throws Exception{
	
		try {
		authenticationManger.authenticate(new UsernamePasswordAuthenticationToken(
				authenticationRequest.getUserName(),authenticationRequest.getPassword()));
		
		
		
		}catch(Exception e) {
			throw new Exception("Invalid User Name or Password");
		}
		
		final UserDetails userDetails =userDetailsService.loadUserByUsername(authenticationRequest.getUserName());
		final String jwt = jwtUtil.generateToken(userDetails);
		
		
		log.info("jwt is "+jwt);
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
}

package com.crow.ssecurity.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.crow.ssecurity.dto.SigninRequest;
import com.crow.ssecurity.dto.SignupRequest;
import com.crow.ssecurity.exception.UserNotFoundException;
import com.crow.ssecurity.service.AppUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class LoginController {
	
	@Autowired
	AppUserService userService;
		
	@GetMapping("/")
	public String he() {
		return "He";
	}
	
	@GetMapping("/hello")
	public String hello() {
		return "Hello";
	}
	
	@PostMapping("/account")
	public ResponseEntity<String> create(@RequestBody SignupRequest sr,BindingResult bindingResult) throws IllegalArgumentException{
		return new ResponseEntity<>(userService.create(sr,bindingResult),HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@RequestBody SigninRequest signinRequest) throws UserNotFoundException {
		return new ResponseEntity<>(userService.login(signinRequest),HttpStatus.OK);
	}
	

	
}

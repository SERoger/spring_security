package com.crow.ssecurity.service;

import java.util.Map;
import org.springframework.validation.BindingResult;
import com.crow.ssecurity.dto.SigninRequest;
import com.crow.ssecurity.dto.SignupRequest;
import com.crow.ssecurity.exception.UserNotFoundException;

public interface AppUserService {
		public String create(SignupRequest signupRequest,BindingResult result) throws IllegalArgumentException;
		public Map<String, Object> login(SigninRequest signinRequest) throws UserNotFoundException;	
		
}

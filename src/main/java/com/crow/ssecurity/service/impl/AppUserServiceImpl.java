package com.crow.ssecurity.service.impl;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import com.crow.ssecurity.dto.SigninRequest;
import com.crow.ssecurity.dto.SignupRequest;
import com.crow.ssecurity.entity.AppUser;
import com.crow.ssecurity.exception.UserNotFoundException;
import com.crow.ssecurity.repository.AppRepository;
import com.crow.ssecurity.service.AppUserService;
import com.nimbusds.jose.jwk.source.ImmutableSecret;

@Service
public class AppUserServiceImpl implements AppUserService{
	
	@Autowired
	AppRepository appRepository;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserServiceImpl userServiceImpl;
	
	@Value("${security.jwt.issuer}")
	String issuer;
	
	@Value("${security.jwt.secret.key}")
	String secretKey;

	@Override
	public String create(SignupRequest signupRequest,BindingResult result) throws IllegalArgumentException{
		if(result.hasErrors()) {
			var errorList=result.getAllErrors();
			var errorMap=new HashMap<String,String>();
			for(int i=0;i<errorList.size();i++) {
				var error=errorList.get(i);
				errorMap.put(error.getCode(), error.getDefaultMessage());
			}
			throw new IllegalArgumentException(errorMap.toString());
		}
		//verify account availability of a user by username, and email
		AppUser user=new AppUser();
		user.setFirstName(signupRequest.getFirstName());
		user.setLastName(signupRequest.getLastName());
		user.setEmail(signupRequest.getEmail());
		user.setUserName(signupRequest.getUserName());
		user.setRole("USER");
		BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(signupRequest.getPassword()));
		appRepository.save(user);
		return "Created";
	}

//VERIFYING PASSWORD AND USERNAME MANUALLY
//	@Override
//	public Map<String, Object> login(SigninRequest signinRequest) throws UserNotFoundException {
//		Optional<AppUser> appUserOptional=appRepository.findByUserName(signinRequest.getUserName());
//		if(appUserOptional.isPresent()) {
//			BCryptPasswordEncoder bpe=new BCryptPasswordEncoder();
//			boolean isMatch=bpe.matches(signinRequest.getPassword(), appUserOptional.get().getPassword());
//			if(appUserOptional.get().getUserName().equals(signinRequest.getUserName()) && isMatch) {
//				String jwtToken=createJwtToken(appUserOptional.get());
//				Map<String, Object> response=new HashMap<String,Object>();
//				response.put("token", jwtToken);
//				response.put("user", appUserOptional.get());
//				return response;
//			}else {
//				throw new UserNotFoundException("UserName or Password wrong!");
//			}
//		}
//		throw new UserNotFoundException("User not found!");
//	}
//	
//VERIFYING PASSWORD AND USERNAME with AuthenticationManager
	@Override
	public Map<String, Object> login(SigninRequest signinRequest) throws UserNotFoundException {
		Optional<AppUser> appUserOptional=appRepository.findByUserName(signinRequest.getUserName());
		if(appUserOptional.isPresent()) {
			try {
				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getUserName(),signinRequest.getPassword()));
				String jwtToken=createJwtToken(appUserOptional.get());
				Map<String, Object> response=new HashMap<String,Object>();
				response.put("token", jwtToken);
				response.put("user", appUserOptional.get());
				return response;
			}catch(Exception ex) {
				throw new UserNotFoundException("UserName or Password wrong!");
			}
		}
		throw new UserNotFoundException("User not found!");
	}
	
	
	private String createJwtToken(AppUser user) {
		Instant now=Instant.now();
		JwtClaimsSet claimsSet=JwtClaimsSet.builder().issuer(issuer)
				.issuedAt(now).expiresAt(now.plusSeconds(24*3600))
				.subject(user.getUserName())
				.claim("role", user.getRole()).build();
		
		NimbusJwtEncoder encoder=new NimbusJwtEncoder(new ImmutableSecret<>(secretKey.getBytes()));
		JwtEncoderParameters params=JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(),claimsSet);
		return encoder.encode(params).getTokenValue();
	}

}

package com.crow.ssecurity.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.crow.ssecurity.entity.AppUser;
import com.crow.ssecurity.repository.AppRepository;

@Service
public class UserServiceImpl implements UserDetailsService{
	@Autowired
	AppRepository appRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser user=appRepository.findByUserName(username).get();
		if(user!=null) {
			UserDetails currentUser=User.withUsername(username).password(user.getPassword()).roles(user.getRole()).build();	
			return currentUser;
			}
		throw new UsernameNotFoundException("Username not found");
	}

}

package com.crow.ssecurity.dto;

import jakarta.validation.constraints.NotEmpty;

public class SigninRequest {
	@NotEmpty
	private String userName;
	@NotEmpty
	private String password;
	
	public SigninRequest() {
		super();
	}

	public SigninRequest(@NotEmpty String userName, @NotEmpty String password) {
		super();
		this.userName = userName;
		this.password = password;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}

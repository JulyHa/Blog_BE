package com.example.back_end.model;

import lombok.Data;

@Data
public class RegisterForm {
	private String username;
	private String email;
	private String pass;
	private String rePass;
}

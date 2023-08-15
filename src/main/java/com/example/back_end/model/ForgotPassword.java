package com.example.back_end.model;

import lombok.Data;

@Data
public class ForgotPassword {
	private String username;
	private String email;
}

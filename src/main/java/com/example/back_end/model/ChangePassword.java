package com.example.back_end.model;

import lombok.Data;

@Data
public class ChangePassword {
	private String currentPass;
	private String newPass;
	private String confirmPass;
}

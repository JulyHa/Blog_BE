package com.example.back_end.controller;

import com.example.back_end.model.LoginForm;
import com.example.back_end.model.User;
import com.example.back_end.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/login")
public class LoginController {
	@Autowired
	private UserService userService;

	@PostMapping
	public ResponseEntity<?> login(@RequestBody LoginForm loginForm) {
		User usernameLogin = userService.findUserByUsername(loginForm.getLoginInput());
		if (loginForm.getPassword().equals("") || loginForm.getLoginInput().equals("")) {
			return new ResponseEntity<>("All fields can not be blank", HttpStatus.NOT_FOUND);
		}
		if (usernameLogin != null) {
			if (!usernameLogin.isStatus()) {
				return new ResponseEntity<>("Account blocked", HttpStatus.NOT_FOUND);
			} else if (!usernameLogin.getPassword().equals(loginForm.getPassword())) {
				return new ResponseEntity<>("Wrong password", HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<>(usernameLogin, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>("Account not exist", HttpStatus.NOT_FOUND);
	}

}

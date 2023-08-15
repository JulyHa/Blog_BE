package com.example.back_end.controller;

import com.example.back_end.model.ChangePassword;
import com.example.back_end.model.ForgotPassword;
import com.example.back_end.model.User;
import com.example.back_end.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/users")
@PropertySource("classpath:application.properties")
public class UserController {
	@Autowired
	private UserService userService;

	@Value("${upload.path}")
	private String link;

	@Value("${display.path}")
	private String displayLink;

	@GetMapping("/{id}")
	public ResponseEntity<User> findUserById(@PathVariable Long id) {
		Optional<User> user = userService.findById(id);
		if (user.isPresent()) {
			return new ResponseEntity<>(user.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<User> updateUser(@RequestPart(value = "file", required = false)MultipartFile file,
										   @RequestPart("user") User user,
										   @PathVariable Long id) {
		Optional<User> userUpdate = userService.findById(id);
		if (!userUpdate.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		if (file != null) {
			String fileName = file.getOriginalFilename();
			try {
				FileCopyUtils.copy(file.getBytes(), new File(link + fileName));
			} catch (Exception e) {
				e.printStackTrace();
			}
			userUpdate.get().setAvatar(displayLink + fileName);
		}
		userUpdate.get().setPhoneNumber(user.getPhoneNumber());
		userUpdate.get().setAddress(user.getAddress());
		userUpdate.get().setName(user.getName());
		return new ResponseEntity<>(userService.save(userUpdate.get()), HttpStatus.OK);
	}

	@PutMapping("/change-password/{id}")
	public ResponseEntity<?> changePassword(@PathVariable Long id,
											   @RequestBody ChangePassword changePassword) {
		User user = userService.findById(id).get();

		if (changePassword.getNewPass().equals("") || changePassword.getConfirmPass().equals("")) {
			return new ResponseEntity<>("All fields can not be blank", HttpStatus.NOT_FOUND);
		}

		if (changePassword.getNewPass().equals(user.getPassword())) {

			return new ResponseEntity<>("New password can not same current password",HttpStatus.NOT_FOUND);

		} else if (!changePassword.getConfirmPass().equals(changePassword.getNewPass())) {

			return new ResponseEntity<>("Wrong re-password",HttpStatus.NOT_FOUND);

		}
		user.setPassword(changePassword.getNewPass());
		userService.save(user);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@RequestBody ForgotPassword forgotPassword) {
		User confirmUsername = userService.findUserByUsername(forgotPassword.getUsername());
		User confirmEmail = userService.findUserByEmail(forgotPassword.getEmail());
		if (forgotPassword.getUsername().equals("") || forgotPassword.getEmail().equals("")) {
			return new ResponseEntity<>("All fields can not be blank", HttpStatus.NOT_FOUND);
		}
		if (confirmUsername == null) {
			return new ResponseEntity<>("Username not exist", HttpStatus.NOT_FOUND);
		}
		if (confirmUsername.equals(confirmEmail)) {
			return new ResponseEntity<>(confirmEmail, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Wrong email",HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/change-password/{id}")
	public ResponseEntity<?> changePassAfterForgot(@PathVariable Long id,
													  @RequestBody ChangePassword changePassword) {
		Optional<User> user = userService.findById(id);
		if (changePassword.getNewPass().equals("") || changePassword.getConfirmPass().equals("")) {
			return new ResponseEntity<>("All fields can not be blank", HttpStatus.NOT_FOUND);
		}
		if (!user.isPresent()) {
			return new ResponseEntity<>("Username not exist",HttpStatus.NOT_FOUND);
		}
		if (changePassword.getNewPass().equals(changePassword.getConfirmPass())) {
			user.get().setPassword(changePassword.getNewPass());
			userService.save(user.get());
			return new ResponseEntity<>(user.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Wrong confirm password", HttpStatus.NOT_FOUND);
		}
	}
}

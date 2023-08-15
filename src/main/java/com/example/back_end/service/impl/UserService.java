package com.example.back_end.service.impl;

import com.example.back_end.model.User;
import com.example.back_end.repository.IUserRepository;
import com.example.back_end.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserService implements IUserService {
	@Autowired
	private IUserRepository userRepository;

	@Override
	public Page<User> findAll(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	@Override
	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public void deleteById(Long id) {
		userRepository.deleteById(id);
	}

	@Override
	public Page<User> findUserByUsernameContaining(Pageable pageable, String username) {
		return userRepository.findUserByUsernameContaining(pageable, username);
	}

	@Override
	public User findUserByUsername(String username) {
		return userRepository.findUserByUsername(username);
	}

	@Override
	public Page<User> findNormalUsers(Pageable pageable) {
		return userRepository.findUserByRole_Id(pageable, 1L);
	}

	@Override
	public ResponseEntity<User> setStatus(Long id) {
		Optional<User> user = userRepository.findById(id);
		if (!user.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		if (user.get().isStatus()) {
			user.get().setStatus(false);
		} else {
			user.get().setStatus(true);
		}
		userRepository.save(user.get());
		return new ResponseEntity<>(user.get(), HttpStatus.OK);
	}

	@Override
	public User findUserByEmail(String email) {
		return userRepository.findUserByEmail(email);
	}
}

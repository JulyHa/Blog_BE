package com.example.back_end.repository;

import com.example.back_end.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User,Long> {
	Page<User> findUserByUsernameContaining(Pageable pageable, String username);
	User findUserByUsername(String username);
	Page<User> findUserByRole_Id(Pageable pageable, Long id);
	User findUserByEmail(String email);
}

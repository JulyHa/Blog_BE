package com.example.back_end.repository;

import com.example.back_end.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByBlogId(Pageable pageable, Long id);

}

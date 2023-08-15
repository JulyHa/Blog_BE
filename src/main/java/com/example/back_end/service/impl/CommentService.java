package com.example.back_end.service.impl;

import com.example.back_end.model.Blog;
import com.example.back_end.model.Comment;
import com.example.back_end.repository.ICommentRepository;
import com.example.back_end.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class CommentService implements ICommentService {
    @Autowired
    ICommentRepository commentRepository;

    @Override
    public Page<Comment> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public Page<Comment> findAllByBlogId(Pageable pageable, Long blogId) {
        return commentRepository.findAllByBlogId(pageable, blogId);
    }

}

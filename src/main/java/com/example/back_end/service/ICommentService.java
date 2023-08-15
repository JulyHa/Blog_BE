package com.example.back_end.service;

import com.example.back_end.model.Blog;
import com.example.back_end.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ICommentService extends ICOREService<Comment, Long>{
    Page<Comment> findAllByBlogId(Pageable pageable, Long id);

}

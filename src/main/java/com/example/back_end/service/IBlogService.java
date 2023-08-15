package com.example.back_end.service;

import com.example.back_end.model.Blog;
import com.example.back_end.model.Comment;
import com.example.back_end.model.User;
import com.example.back_end.model.Label;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


public interface IBlogService extends ICOREService<Blog, Long> {
    Page<Blog> findAllByUserIdAndStatusIsTrue(Long userId, Pageable pageable);

    Page<Blog> findAllPublicBlogs(Pageable pageable);

    Page<Blog> findAllPublicBlogsByUserId(Long userId, Pageable pageable);
    Page<Blog>findAllByTitleContainingOrTitleContaining(String string1, String string2, Pageable pageable);
    Page<Blog>findBlogsByLabelId(Long labelId, Pageable pageable
    );
    Page<Blog>findAllByPrivacyIsTrueAndStatusIsTrueOrderByIdDesc(Pageable pageable);
    int setLabelBlog(Long labelId,Long blogId);
    void changePrivacy(Long id);
    void resetLabelBlog(Long blogId);
    Page<Blog> searchOnHomePage(String string, Pageable pageable);
    ResponseEntity<Blog> setStatus(Long id);
    Long countComment(Long id);
    Page<Blog> findAllByUserIdAndStatusIsTrueOrderByIdDesc(Long userId, Pageable pageable);
    Page<Blog>findAllByTitleContainingOrDescriptionContainingAndUserId(String title, String description, Long user_id, Pageable pageable);


}

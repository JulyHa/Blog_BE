package com.example.back_end.service.impl;

import com.example.back_end.model.Blog;
import com.example.back_end.model.User;
import com.example.back_end.repository.IBlogRepository;
import com.example.back_end.service.IBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogService implements IBlogService {
    @Autowired
    IBlogRepository blogRepository;

    @Override
    public Page<Blog> findAll(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Optional<Blog> findById(Long id) {
        return blogRepository.findById(id);
    }

    @Override
    public Blog save(Blog blog) {
        return blogRepository.save(blog);
    }

    @Override
    public void deleteById(Long id) {
        Blog blog = findById(id).get();
        blog.setStatus(false);
        blogRepository.save(blog);
    }

    @Override
    public Page<Blog> findAllByUserIdAndStatusIsTrue(Long userId, Pageable pageable) {
        return blogRepository.findAllByUserIdAndStatusIsTrue(userId, pageable);
    }

    @Override
    public Page<Blog> findAllPublicBlogs(Pageable pageable) {
        return blogRepository.findAllByPrivacyIsTrueAndStatusIsTrueOrderByCreatedDateDesc(pageable);
    }

    @Override
    public Page<Blog> findAllPublicBlogsByUserId(Long userId, Pageable pageable) {
        return blogRepository.findAllByPrivacyIsTrueAndStatusIsTrueAndUserId(userId,pageable);
    }

    @Override
    public Page<Blog> findAllByTitleContainingOrTitleContaining(String string1, String string2, Pageable pageable) {
        return blogRepository.findAllByTitleContainingOrTitleContaining(string1,string2,pageable);
    }

    @Override
    public ResponseEntity<Blog> setStatus(Long id) {
        Optional<Blog> blog = blogRepository.findById(id);
        if (!blog.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (blog.get().isStatus()) {
            blog.get().setStatus(false);
        } else {
            blog.get().setStatus(true);
        }
        blogRepository.save(blog.get());
        return new ResponseEntity<>(blog.get(), HttpStatus.OK);
    }

    @Override
    public Long countComment(Long id) {
        return blogRepository.countAllCommentByBlogId(id);
    }


    @Override
    public Page<Blog> findBlogsByLabelId(Long labelId, Pageable pageable) {
        return blogRepository.findBlogsByLabelId(labelId,pageable);
    }

    @Override
    public Page<Blog> findAllByPrivacyIsTrueAndStatusIsTrueOrderByIdDesc(Pageable pageable) {
        return blogRepository.findAllByPrivacyIsTrueAndStatusIsTrueOrderByIdDesc(pageable);
    }

    @Override
    public int setLabelBlog(Long labelId, Long blogId) {
        return blogRepository.setLabelBlog(labelId,blogId);
    }

    @Override
    public void changePrivacy(Long id) {
        Blog blog = findById(id).get();
        blog.setPrivacy(!blog.isPrivacy());
        blogRepository.save(blog);
    }

    @Override
    public void resetLabelBlog(Long blogId) {
        blogRepository.resetLabelBlog(blogId);
    }

    @Override
    public Page<Blog> searchOnHomePage(String string, Pageable pageable) {
        return blogRepository.searchOnHomePage(string,pageable);
    }

    @Override
    public Page<Blog> findAllByUserIdAndStatusIsTrueOrderByIdDesc(Long userId, Pageable pageable) {
        return blogRepository.findAllByUserIdAndStatusIsTrueOrderByIdDesc(userId,pageable);
    }

    @Override
    public Page<Blog> findAllByTitleContainingOrDescriptionContainingAndUserId(String title, String description, Long user_id, Pageable pageable) {
        return blogRepository.findAllByTitleContainingOrDescriptionContainingAndUserId(title,description,user_id,pageable);
    }

}

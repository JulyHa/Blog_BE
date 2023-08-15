package com.example.back_end.controller;

import com.example.back_end.model.Comment;
import com.example.back_end.service.impl.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/{blogId}")
    public ResponseEntity<Page<Comment>> findAllCommentByBlogId(@PageableDefault(size = 5)Pageable pageable,
                                                              @PathVariable Long blogId) {
        return new ResponseEntity<>(commentService.findAllByBlogId(Pageable.unpaged(), blogId), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    public ResponseEntity<Comment> saveComment(@RequestBody Comment comment) {
        comment.setDate(LocalDate.now());
        return new ResponseEntity<>(commentService.save(comment), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment (@RequestBody Comment comment, @PathVariable Long id) {
        Optional<Comment> commentUpdate = commentService.findById(id);
        if (commentUpdate.isPresent()) {
            commentUpdate.get().setDate(LocalDate.now());
            commentUpdate.get().setContent(comment.getContent());
            commentService.save(commentUpdate.get());
            return new ResponseEntity<>(commentUpdate.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Comment> deleteComment (@PathVariable Long id) {
        Optional<Comment> comment = commentService.findById(id);
        if (comment.isPresent()) {
            commentService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

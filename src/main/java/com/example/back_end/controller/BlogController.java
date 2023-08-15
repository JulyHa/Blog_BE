package com.example.back_end.controller;

import com.example.back_end.model.Blog;
import com.example.back_end.model.Comment;
import com.example.back_end.model.Label;
import com.example.back_end.model.User;
import com.example.back_end.service.IBlogService;
import com.example.back_end.service.ICommentService;
import com.example.back_end.service.ILabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@CrossOrigin("*")
@RequestMapping
@PropertySource("classpath:application.properties")
public class BlogController {
    @Autowired
    IBlogService blogService;
    @Autowired
    ILabelService labelService;

    @Value("${upload.path}")
    private String link;

    @Value("${display.path}")
    private String displayLink;

    @GetMapping("/users/{userId}/blogs")
    public ResponseEntity<Page<Blog>> findAllByUserIdAndStatusIsTrue(@PathVariable Long userId,

                                                                     Pageable pageable) {
        return new ResponseEntity<>(blogService.findAllByUserIdAndStatusIsTrueOrderByIdDesc(userId, pageable), HttpStatus.OK);
    }

    @GetMapping("/blogs/{blogId}")
    public ResponseEntity<Blog> findBlogByUserId(@PathVariable(value = "blogId") Long blogId) {
        return new ResponseEntity<>(blogService.findById(blogId).get(), HttpStatus.OK);
    }

    @GetMapping("/blogs")
    public ResponseEntity<Page<Blog>> findAll(@PageableDefault(size = 5) Pageable pageable) {
        Page<Blog> blogs = blogService.findAll(pageable);
        return new ResponseEntity<>(blogs, HttpStatus.OK);
    }

    @GetMapping("/blogs/public")
    public ResponseEntity<Page<Blog>> findPublicBlogs(@PageableDefault(size = 5) Pageable pageable) {
        return new ResponseEntity<>(blogService.findAllByPrivacyIsTrueAndStatusIsTrueOrderByIdDesc(pageable), HttpStatus.OK);
    }

    @GetMapping("/blogs/latestBlog")
    public ResponseEntity<Page<Blog>> findLatestBlogs(@PageableDefault(size = 4) Pageable pageable) {
        return new ResponseEntity<>(blogService.findAllPublicBlogs(pageable), HttpStatus.OK);
    }

    @GetMapping("/users/{id}/blogs/public")
    public ResponseEntity<Page<Blog>> findAllPublicBlogsByUserId(@PathVariable Long id, Pageable pageable) {
        return new ResponseEntity<>(blogService.findAllPublicBlogsByUserId(id, Pageable.unpaged()), HttpStatus.OK);
    }

    @GetMapping("/labels/{labelId}/blogs")
    public ResponseEntity<Page<Blog>> findAllPublicBlogsByLabelId(@PathVariable Long labelId, @PageableDefault(size = 5) Pageable pageable) {
        Page<Blog> blogs = blogService.findBlogsByLabelId(labelId, Pageable.unpaged());
        return new ResponseEntity<>(blogs, HttpStatus.OK);
    }

    @GetMapping("/blogs/searchHomePage")
    public ResponseEntity<Page<Blog>> searchOnHomePage(@RequestParam(value = "q") String q, Pageable pageable) {
        Page<Blog> blogs = blogService.searchOnHomePage(q,pageable);
        return new ResponseEntity<>(blogs, HttpStatus.OK);
    }

    @PostMapping("/blogs")
    @Transactional
    public ResponseEntity<Blog> create(@RequestPart(value = "file", required = false)
                                       MultipartFile file,
                                       @RequestPart("blog") Blog blog) {
        if (file != null) {
            String fileName = file.getOriginalFilename();
            try {
                FileCopyUtils.copy(file.getBytes(), new File((link + fileName)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            blog.setImage(displayLink + fileName);
        } else {
            blog.setImage(displayLink + "default.jpg");
        }

        if (blog.getDescription().isEmpty()) {
            blog.setDescription(blog.getContent().substring(0, 50) + "...");
        }

        blog.setCreatedDate(LocalDate.now());

        blog.setStatus(true);
        Blog latestBlog = null;

        try{
            latestBlog = blogService.save(blog);

        }catch (Exception e){
            System.out.println(e);
        }
        Long blogId = latestBlog.getId();

        Pattern pattern = Pattern.compile("#[a-z0-9_]+");
        Matcher matcher = pattern.matcher(latestBlog.getContent());
        Set<String> labelName = new HashSet<>();
        while (matcher.find()) {
            labelName.add(matcher.group());
        }

        for (String s : labelName) {
            Label label = new Label(s);
            Long labelId = labelService.save(label).getId();
            blogService.setLabelBlog(labelId, blogId);
        }

        return new ResponseEntity<>(latestBlog, HttpStatus.CREATED);
    }

    @PutMapping("/blogs/{id}")
    public ResponseEntity<Blog> update(@PathVariable Long id, @RequestPart(value = "file", required = false)
    MultipartFile file, @RequestPart("blog") Blog blog) {
        Blog blogUpdate = blogService.findById(id).get();

        if (file != null) {
            String fileName = file.getOriginalFilename();
            try {
                FileCopyUtils.copy(file.getBytes(), new File(link + fileName));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            blog.setImage(displayLink + fileName);
        } else {
            blog.setImage(blogUpdate.getImage());
        }

        blog.setId(blogUpdate.getId());

        blog.setCreatedDate(blogUpdate.getCreatedDate());

        if (blog.getDescription() == null) {
            blog.setDescription(blog.getContent().substring(0, 100) + "...");
        }

        blog.setStatus(true);

        blogService.resetLabelBlog(blog.getId());

        Pattern pattern = Pattern.compile("#[a-z0-9_]+");
        Matcher matcher = pattern.matcher(blog.getContent());
        Set<String> labelName = new HashSet<>();
        while (matcher.find()) {
            labelName.add(matcher.group());
        }

        for (String s : labelName) {
            Label label = new Label(s);
            Long labelId = labelService.save(label).getId();
            blogService.setLabelBlog(labelId, blog.getId());
        }

        return new ResponseEntity<>(blogService.save(blog), HttpStatus.OK);
    }

    @GetMapping("/blogs/search")
    public ResponseEntity<Page<Blog>> search(@RequestParam(value = "q") String q, Pageable pageable) {
        Page<Blog> pages = blogService.findAllByTitleContainingOrTitleContaining("%" + q + "%", "%" + q + "%", pageable);
        System.out.println("test");
        return new ResponseEntity<>(pages, HttpStatus.OK);
    }

    @DeleteMapping("/blogs/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        blogService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/blogs/set/{id}")
    public ResponseEntity<Blog> setStatus(@PathVariable Long id) {
        return blogService.setStatus(id);
    }

    @PutMapping("/blogs/{id}/privacy")
    public ResponseEntity<?> changePrivacy(@PathVariable Long id) {
        blogService.changePrivacy(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("blogs/count/{id}")
    public ResponseEntity<Long> countAllCommentByBlogId(@PathVariable Long id) {
        Long count = blogService.countComment(id);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

}

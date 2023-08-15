package com.example.back_end.controller;

import com.example.back_end.model.Label;
import com.example.back_end.service.impl.BlogService;
import com.example.back_end.service.impl.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("labels")
public class LabelController {
    @Autowired
    private LabelService labelService;

    @Autowired
    private BlogService blogService;

    @GetMapping
    public ResponseEntity<Page<Label>> findAllLabel(Pageable pageable) {
        return new ResponseEntity<>(labelService.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<Page<Label>> getAllLabels() {
        return new ResponseEntity<>(labelService.findAll(Pageable.unpaged()), HttpStatus.OK);
    }



    @GetMapping("/{id}")
    public ResponseEntity<Label> findOneLabel(@PathVariable Long id) {
        if (labelService.findById(id).isPresent()) {
            return new ResponseEntity<>(labelService.findById(id).get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Label> createLabel(@RequestBody Label label) {
        if (labelService.findByName(label.getName()) == null) {
            return new ResponseEntity<>(labelService.save(label), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Label> updateLabel (@RequestBody Label label, @PathVariable Long id) {
        if (labelService.findById(id).isPresent()) {
            return new ResponseEntity<>(labelService.save(label), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Label> deleteLabel (@PathVariable Long id) {
        Optional<Label> label = labelService.findById(id);
        if (label.isPresent()) {
            labelService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/blog/{blogId}")
    public ResponseEntity<Page<Label>> findAllLabelByBlogId(Pageable pageable, @PathVariable Long blogId) {
        if (blogService.findById(blogId).isPresent()) {
            return new ResponseEntity<>(labelService.findAllLabelByBlogId(Pageable.unpaged(), blogId), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

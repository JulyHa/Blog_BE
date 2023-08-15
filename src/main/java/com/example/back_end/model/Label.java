package com.example.back_end.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Label {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    @ManyToMany(targetEntity = Blog.class)
    @JoinTable(
            name = "label_blogs",
            joinColumns = {
                    @JoinColumn(name = "label_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "blogs_id")
            }
    )
    private Set<Blog> blogs = new HashSet<>();

    public Label (String name) {
        this.name = name;
    }

    public Label(String name, Set<Blog> blogs) {
        this.name = name;
        this.blogs = blogs;
    }

    public Label() {
    }
}

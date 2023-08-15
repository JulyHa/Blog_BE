package com.example.back_end.service;

import com.example.back_end.model.Label;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ILabelService extends ICOREService<Label, Long> {
    Label findByName(String name);

    Page<Label> findAllLabelByBlogId(Pageable pageable, Long id);

    Label findLabelsByName(String name);


}

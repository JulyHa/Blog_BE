package com.example.back_end.repository;

import com.example.back_end.model.Label;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;


@Repository
public interface ILabelRepository extends JpaRepository<Label, Long> {
    Label findByName(String name);

    @Query(value = "select * from label lb join label_blogs lbs on lb.id = lbs.label_id join blog b on lbs.blogs_id = b.id where b.id = ?1", nativeQuery = true )
    Page<Label> findAllLabelByBlogId(Long id,Pageable pageable);

    Label findLabelsByName(String name);
}

package com.example.back_end.repository;

import com.example.back_end.model.Blog;
import com.example.back_end.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface IBlogRepository extends JpaRepository<Blog, Long> {
    Page<Blog> findAllByUserIdAndStatusIsTrue(Long userId, Pageable pageable);

    Page<Blog> findAllByPrivacyIsTrueAndStatusIsTrueOrderByCreatedDateDesc(Pageable pageable);

    Page<Blog> findAllByPrivacyIsTrueAndStatusIsTrueOrderByIdDesc(Pageable pageable);

    Page<Blog> findAllByPrivacyIsTrueAndStatusIsTrueAndUserId(Long userId, Pageable pageable);

    @Query(value = "select * from Blog where (title like ?1 or content like ?2)", nativeQuery = true)
    Page<Blog> findAllByTitleContainingOrTitleContaining(String string1, String string2, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from blog right join label_blogs" +
            " on blog.id = label_blogs.blogs_id left join label on label_blogs.label_id = label.id " +
            "where label.id = ?1  order by blog.created_date desc")
    Page<Blog> findBlogsByLabelId(Long id, Pageable pageable);

    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO case_study_md_4.label_blogs (label_id, blogs_id) VALUES (?1,?2)")
    int setLabelBlog(Long labelId, Long blogId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM case_study_md_4.label_blogs WHERE label_blogs.blogs_id = ?1")
    void resetLabelBlog(Long blogId);

    @Query(value = "select b from Blog b where b.privacy = true and b.status = true and (b.title like :searchText or b.description like :searchText)")
    Page<Blog> searchOnHomePage(@Param("searchText") String string, Pageable pageable);
    @Query(value = "select count(c) from Comment c join Blog b on c.blog.id = b.id where c.blog.id = ?1")
    Long countAllCommentByBlogId(Long id);

//    Page<Blog> findAllByTitleContainsOrContentContaining(String s1, String s2, Pageable pageable);

    Page<Blog> findAllByUserIdAndStatusIsTrueOrderByIdDesc(Long userId, Pageable pageable);
    Page<Blog>findAllByTitleContainingOrDescriptionContainingAndUserId(String title, String description, Long user_id, Pageable pageable);


}

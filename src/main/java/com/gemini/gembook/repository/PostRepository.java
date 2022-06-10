package com.gemini.gembook.repository;

import com.gemini.gembook.model.Post;
import io.swagger.models.auth.In;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface providing database access functionality to manage post made by user.
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Integer>{
    List<Post> findPostByUserUserId(String userId);
    List<Post> findPostByPostTypePostTypeId(int postTypeId);



}

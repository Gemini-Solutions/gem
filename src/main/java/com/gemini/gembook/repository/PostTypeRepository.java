package com.gemini.gembook.repository;

import com.gemini.gembook.model.PostType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface providing database access functionality to manage types of a post.
 */
@Repository
public interface PostTypeRepository extends JpaRepository<PostType, Integer> {
}

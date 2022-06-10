package com.gemini.gembook.repository;

import com.gemini.gembook.model.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface providing database access functionality to manage comments on a post.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
}

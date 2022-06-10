package com.gemini.gembook.repository;

import com.gemini.gembook.model.CommentLike;
import com.gemini.gembook.model.CommentLikeIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface providing database access functionality to manage likes on a comment.
 */
@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, CommentLikeIdentity>{
	
	/**
	 * Get likes on a comment identified by commentId and userId.
	 * @param commentId
	 * @param userId
	 * @return CommentLike object
	 */
	@Query(
            value = "select * from comment_likes\n"+
            		"where comment_id =?1 and user_id =?2",
            nativeQuery = true
    )
	Optional<CommentLike> findById(int commentId, String userId);

}

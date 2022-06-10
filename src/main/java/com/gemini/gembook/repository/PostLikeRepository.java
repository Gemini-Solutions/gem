package com.gemini.gembook.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gemini.gembook.model.PostLike;
import com.gemini.gembook.model.PostLikeIdentity;
import com.gemini.gembook.model.Post;

/**
 * Repository interface providing database access functionality to manage likes on a post.
 */
@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeIdentity>{

	/**
	 * Retrieves likes information regarding a post.
	 * @param post
	 * @return List containing Likes
	 */
	@Query(
            value = "select * from likes\n"+
            		"where post_id = ?1 ",
            nativeQuery = true
    )
	List<PostLike> findByLikeIdentityPost(Post post);

	/**
	 * Retrieves likes information related with a post and a particular user.
	 * @param postId
	 * @param userId
	 * @return Like object
	 */
	@Query(
            value = "select * from likes\n"+
            		"where post_id =?1 and user_id =?2",
            nativeQuery = true
    )
	Optional<PostLike> findById(int postId, String userId);

	/**
	 * Updates an existing like information related with a post and a particular user.
	 * @param postId
	 * @param userId
	 * @param likeFlag
	 * @param likeTime
	 * @return 1 if updated, 0 if not updated
	 */
	@Modifying
	@Transactional
	@Query(
			value = "update likes\n" +
					"set like_flag = ?3," +
					"like_time = ?4\n " +
					"where post_id = ?1\n" +
					"and user_id = ?2",
           nativeQuery = true
	)
	int updateLike(int postId, String userId,String likeFlag,long likeTime);
}

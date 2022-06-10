package com.gemini.gembook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gemini.gembook.model.GemFiles;

/**
 * Repository interface providing database access functionality to manage photos uploaded on a post.
 */
@Repository
public interface PhotoRepository extends JpaRepository<GemFiles, Integer> {
	
	/**
	 * Deletes photos related with a post.
	 * @param postId
	 * @return 1 if deleted, 0 if not deleted
	 */
	@Modifying
	@Transactional
	@Query(	
			value = "Delete from files\n" +
					"where post_id = ?1",
           nativeQuery = true
	)	
	int deleteFiles(int postId);

	@Query(
			value="select * from files where post_id = ?1",
			nativeQuery = true
	)
	List<GemFiles> getFileByPostId(int postId);
}

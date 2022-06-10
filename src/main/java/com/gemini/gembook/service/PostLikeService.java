package com.gemini.gembook.service;

import com.gemini.gembook.dto.ApiResponse;
import com.gemini.gembook.model.PostLike;
import com.gemini.gembook.model.PostLikeIdentity;
import com.gemini.gembook.repository.PostLikeRepository;
import com.gemini.gembook.repository.PostRepository;
import com.gemini.gembook.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Service class to server requests for users' likes made on a Post.
 */
@Service
public class PostLikeService {

    private final Logger logger = LoggerFactory.getLogger(PostLikeService.class);
    private final PostLikeRepository postLikeRepo;
    private final UsersRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public PostLikeService(PostLikeRepository postLikeRepo, PostRepository postRepository,
                           UsersRepository userRepository) {
        this.postLikeRepo = postLikeRepo;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    /**
     * Retrieves all users' likes information related with a post identified by given postId
     *
     * @param postId
     * @return List containing user Likes' details
     */
    public ApiResponse getLikesForPost(int postId) {
        try {
            return postRepository.findById(postId).map(post ->
                    new ApiResponse(HttpStatus.OK, postLikeRepo.findByLikeIdentityPost(post))
            ).orElse(new ApiResponse(HttpStatus.BAD_REQUEST, "No like exists for the post"));
        } catch (Exception e) {
            logger.error("Exception in getLikesForPost : {}", e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch likes for this post");
        }
    }


    /**
     * Saves details about Like made by an user on a post.
     *
     * @param like
     * @return Like object
     */
    public ApiResponse saveLikeForPost(int postId, String userId, int likeFlag) {
        try {
            return postRepository.findById(postId).map(post -> {
                return userRepository.findById(userId).map(user -> {
                    {
                        PostLikeIdentity likeIdentity = new PostLikeIdentity();
                        likeIdentity.setPost(post);
                        likeIdentity.setUser(user);
                        PostLike commentLike = new PostLike();
                        commentLike.setLikeIdentity(likeIdentity);
                        commentLike.setLikeFlag(likeFlag);
                        return new ApiResponse(HttpStatus.CREATED, postLikeRepo.save(commentLike));
                    }
                }).orElse(new ApiResponse(HttpStatus.BAD_REQUEST, "User doesn't exists"));
            }).orElse(new ApiResponse(HttpStatus.BAD_REQUEST, "Post doesn't exists"));
        } catch (Exception e) {
            logger.error("Exception in saveLike : {}", e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Like operation failed");
        }
    }

    /**
     * Updates information about an existing Like updated by a user on a post.
     *
     * @param postId
     * @param userId
     * @return true on success
     */
    public ApiResponse removeLikeForPost(int postId, String userId) {
        try {
            return postLikeRepo.findById(postId, userId).map(postLike -> {
                    postLikeRepo.delete(postLike);
                    return new ApiResponse(HttpStatus.OK, "Removed");
            }).orElse(new ApiResponse(HttpStatus.BAD_REQUEST, "No like exists for this pair of user/post"));
        } catch (Exception e) {
            logger.error("Exception in removeLikeForPost : {}", e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Operation failed");
        }
    }
}

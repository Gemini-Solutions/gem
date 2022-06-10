package com.gemini.gembook.service;

import com.gemini.gembook.dto.ApiResponse;
import com.gemini.gembook.model.CommentLike;
import com.gemini.gembook.model.CommentLikeIdentity;
import com.gemini.gembook.repository.CommentLikeRepository;
import com.gemini.gembook.repository.CommentRepository;
import com.gemini.gembook.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Service class to serve requests for users' likes made on a comment.
 */
@Service
public class CommentLikeService {

    private final Logger logger = LoggerFactory.getLogger(CommentLikeService.class);
    private final UsersRepository userRepository;
    private final CommentRepository commentReository;
    private final CommentLikeRepository commentLikeRepository;

    @Autowired
    public CommentLikeService(UsersRepository userRepository,
                              CommentLikeRepository commentLikeRepository, CommentRepository commentReository) {
        this.userRepository = userRepository;
        this.commentReository = commentReository;
        this.commentLikeRepository = commentLikeRepository;
    }

    /**
     * Retrieves information about likes made on a comment.
     *
     * @param commentId
     * @param userId
     * @return CommentLike object
     */
    public ApiResponse getCommentLike(int commentId, String userId) {
        try {
            return commentLikeRepository.findById(commentId, userId).map(commentLike ->
                    new ApiResponse(HttpStatus.OK, commentLike)
            ).orElse(new ApiResponse(HttpStatus.BAD_REQUEST, "No like exists for this pair of user/comment"));
        } catch (Exception e) {
            logger.error("Exception in getCommentLike : {}", e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch user like for this comment");
        }
    }

    /**
     * Saves an user's like information related with a comment.
     *
     * @param commentLike
     * @return CommentLike object
     */
    public ApiResponse saveCommentLike(int commentId, String userId, int likeFlag) {
        try {
            return commentReository.findById(commentId).map(comment -> {
                return userRepository.findById(userId).map(user -> {
                    {
                        CommentLikeIdentity likeIdentity = new CommentLikeIdentity();
                        likeIdentity.setComment(comment);
                        likeIdentity.setUser(user);
                        CommentLike commentLike = new CommentLike();
                        commentLike.setCommentIdentity(likeIdentity);
                        commentLike.setCommentLikeFlag(likeFlag);
                        return new ApiResponse(HttpStatus.CREATED, commentLikeRepository.save(commentLike));
                    }
                }).orElse(new ApiResponse(HttpStatus.BAD_REQUEST, "User doesn't exists"));
            }).orElse(new ApiResponse(HttpStatus.BAD_REQUEST, "Comment doesn't exists"));
        } catch (Exception e) {
            logger.error("Exception in saveCommentLike : {}", e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Like operation failed");
        }
    }

    /**
     * Update an user's like information related with a comment.
     *
     * @param commentId
     * @param userId
     * @return
     */
    public ApiResponse removeCommentLike(int commentId, String userId) {
        try {
            return commentLikeRepository.findById(commentId, userId).map(commentLike ->
                    {
                        commentLikeRepository.delete(commentLike);
                        return new ApiResponse(HttpStatus.OK, "Removed");
                    }
            ).orElse(new ApiResponse(HttpStatus.BAD_REQUEST, "No like exists for this pair of user/comment"));
        } catch (Exception e) {
            logger.error("Exception in removeCommentLike : {}", e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Operation failed");
        }
    }
}

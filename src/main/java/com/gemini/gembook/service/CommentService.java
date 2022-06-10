package com.gemini.gembook.service;

import com.gemini.gembook.dto.ApiResponse;
import com.gemini.gembook.dto.CommentDetails;
import com.gemini.gembook.model.Comment;
import com.gemini.gembook.repository.CommentRepository;
import com.gemini.gembook.repository.PostRepository;
import com.gemini.gembook.repository.UsersRepository;
import com.gemini.gembook.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

/**
 * Service class to serve requests for users' comments made on a Post.
 */
@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PostRepository postRepository;

    private final Logger logger = LoggerFactory.getLogger(CommentService.class);

    /**
     * Saves an user's comment made on a Post.
     *
     * @param details : {@link CommentDetails}
     * @return Comment object
     */
    public ApiResponse addComment(CommentDetails details) {
        details.setUserId(details.getUserId().toLowerCase(Locale.ROOT));
        try {
            return usersRepository.findById(details.getUserId()).map(user -> {
                return postRepository.findById(details.getPostId()).map(post -> {
                    Comment newComment = new Comment(details.getCommentContent());
                    newComment.setUser(user);
                    newComment.setPost(post);
                    return new ApiResponse(HttpStatus.CREATED, commentRepository.save(newComment));
                }).orElse(new ApiResponse(HttpStatus.BAD_REQUEST, "Post doesn't exists"));
            }).orElse(new ApiResponse(HttpStatus.BAD_REQUEST, "User doesn't exists"));
        } catch (Exception e) {
            logger.error("Exception in addComment() : {}", e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to add comment");
        }
    }

    public ApiResponse findByPostId(int pageNumber, int postId) {
        try {
            Pageable pageable = PageRequest.of(pageNumber, Constants.PAGINATED_COMMENT_COUNT, Sort.by("comment_time").descending());
//            List<Comment> commentList = commentRepository.findAllByPost(postId, pageable);
            return new ApiResponse(HttpStatus.OK, pageable);
        } catch (Exception e) {
            logger.error("Exception in findByCommentId() : {}", e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch Comment");
        }
    }

    /**
     * Retrieves Comment by commentId.
     *
     * @param commentId
     * @return Comment object
     */
    public ApiResponse findByCommentId(int commentId) {
        try {
            return commentRepository.findById(commentId).map(comment ->
                    new ApiResponse(HttpStatus.OK, comment))
                    .orElse(new ApiResponse(HttpStatus.NOT_ACCEPTABLE, "No comment with comment id: " + commentId + " exists"));
        } catch (Exception e) {
            logger.error("Exception in findByCommentId() : {}", e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch Comment");
        }
    }

    /**
     * Deletes a comment identified with commentId.
     *
     * @param commentId
     * @return true on success
     */
    public ApiResponse deleteComment(int commentId) {
        try {
            return commentRepository.findById(commentId).map(comment -> {
                commentRepository.deleteById(commentId);
                return new ApiResponse(HttpStatus.OK, comment);
            }).orElse(new ApiResponse(HttpStatus.NOT_ACCEPTABLE, "No comment with comment id: " + commentId + " exists"));
        } catch (Exception e) {
            logger.error("Exception in findByCommentId() : {}", e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch Comment");
        }
    }
}

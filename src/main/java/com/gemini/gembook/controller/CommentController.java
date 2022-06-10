package com.gemini.gembook.controller;

import com.gemini.gembook.dto.ApiResponse;
import com.gemini.gembook.dto.CommentDetails;
import com.gemini.gembook.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for the Comments REST API.
 */
@RestController
@RequestMapping(value = "/api/post/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    private final Logger logger = LoggerFactory.getLogger(CommentController.class);

    /**
     * Retrieves the comment by comment Id.
     *
     * @param commentId
     * @return comment
     */
    @GetMapping(value = "/byId")
    public ResponseEntity<ApiResponse> getCommentById(@RequestParam(value = "commentId") int commentId) {
        ApiResponse response = commentService.findByCommentId(commentId);
        logger.info("ResponseMessage for getCommentId() is : {}", response.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * @return Latest Paginated Comments in descending order of their time
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getCommentsByPost(@RequestParam("pageNum") int pageNumber,
                                                         @RequestParam("postId") int postId) {
        if (pageNumber < 0)
            pageNumber = 0;
        ApiResponse response = commentService.findByPostId(pageNumber,postId);
        logger.info("ResponseMessage for getPosts() is : {}", response.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * Adds the user comment to a post identified by postId.
     *
     * @param commentDetails {@link CommentDetails}
     * @return inserted comment
     */
    @PostMapping
    public ResponseEntity<ApiResponse> addComment(@Validated @RequestBody CommentDetails commentDetails) {
        ApiResponse response = commentService.addComment(commentDetails);
        logger.info("ResponseMessage for addComment() is : {}", response.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * Delete the comment by Id.
     *
     * @param commentId
     * @return true if comment is deleted
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteComment(@RequestParam(value = "commentId") int commentId) {
        ApiResponse response = commentService.deleteComment(commentId);
        logger.info("ResponseMessage for deleteComment() is : {}", response.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

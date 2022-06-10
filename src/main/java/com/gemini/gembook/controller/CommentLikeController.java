package com.gemini.gembook.controller;

import com.gemini.gembook.dto.ApiResponse;
import com.gemini.gembook.service.CommentLikeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

/**
 * Controller class for the Comment Like REST API
 */
@RestController
@RequestMapping(value = "/api/post/comment/like")
public class CommentLikeController {

    @Autowired
    private CommentLikeService commentLikeService;

    private final Logger logger = LoggerFactory.getLogger(CommentLikeController.class);

    /**
     * Save a user's like on a comment or update it if existing.
     *
     * @param commentId
     * @param userId
     * @param likeFlag
     * @return status as Created if saved/updated successfully.
     */
    @PostMapping
    public ResponseEntity saveLikeForComment(@RequestParam(value = "commentId") int commentId,
                                          @RequestParam(value = "userId") String userId,
                                          @RequestParam(value = "commentLikeFlag", defaultValue = "0") int likeFlag) {

        if (likeFlag > 1 || likeFlag < 0)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Wrong \"like\" flag");

        ApiResponse response = commentLikeService.saveCommentLike(commentId, userId.toLowerCase(Locale.ROOT), likeFlag);
        logger.info("ResponseMessage for saveLikeForComment() is : {}", response.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * Save a user's like on a comment or update it if existing.
     *
     * @param commentId
     * @param userId
     * @return status as Created if saved/updated successfully.
     */
    @DeleteMapping
    public ResponseEntity removeLikeForComment(@RequestParam(value = "commentId") int commentId,
                                            @RequestParam(value = "userId") String userId){

        ApiResponse response = commentLikeService.removeCommentLike(commentId, userId);
        logger.info("ResponseMessage for removeLikeForComment() is : {}", response.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

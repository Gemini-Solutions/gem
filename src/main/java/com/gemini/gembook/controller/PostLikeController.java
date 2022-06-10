package com.gemini.gembook.controller;

import com.gemini.gembook.dto.ApiResponse;
import com.gemini.gembook.service.PostLikeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

/**
 * Controller class for REST API of Like feature.
 */
@RestController
@RequestMapping(value = "/api/post/like")
public class PostLikeController {

    @Autowired
    private PostLikeService likeService;

    private final Logger logger = LoggerFactory.getLogger(PostLikeController.class);

    /**
     * retrieves users' Like information for a Post.
     *
     * @param postId
     * @return List containing users' Like information.
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getLikesForPost(@RequestParam(value = "postId") int postId) {
        ApiResponse response = likeService.getLikesForPost(postId);
        logger.info("ResponseMessage for getLikesForPost() is : {}", response.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * Save a user's like on a post or update it if existing.
     *
     * @param postId
     * @param userId
     * @param likeFlag
     * @return status as Created if saved/updated successfully.
     */
    @PostMapping
    public ResponseEntity saveLikeForPost(@RequestParam(value = "postId") int postId,
                                   @RequestParam(value = "userId") String userId,
                                   @RequestParam(value = "likeFlag", defaultValue = "0") int likeFlag) {
        if (likeFlag > 1 || likeFlag < 0)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Wrong \"like\" flag");

        ApiResponse response = likeService.saveLikeForPost(postId, userId.toLowerCase(Locale.ROOT), likeFlag);
        logger.info("ResponseMessage for saveLikeForPost() is : {}", response.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * Save a user's like on a post or update it if existing.
     *
     * @param postId
     * @param userId
     * @return status as Created if saved/updated successfully.
     */
    @DeleteMapping
    public ResponseEntity removeLikeForPost(@RequestParam(value = "postId") int postId,
                                         @RequestParam(value = "userId") String userId) {

        ApiResponse response = likeService.removeLikeForPost(postId, userId.toLowerCase(Locale.ROOT));
        logger.info("ResponseMessage for removeLikeForPost() is : {}", response.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

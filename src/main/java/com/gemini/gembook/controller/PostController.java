package com.gemini.gembook.controller;

import com.gemini.gembook.dto.ApiResponse;
import com.gemini.gembook.dto.CommentDetails;
import com.gemini.gembook.dto.PostDetails;
import com.gemini.gembook.dto.UpdatePostDetails;
import com.gemini.gembook.service.PostService;
import com.gemini.gembook.service.PostTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Locale;

/**
 * Controller class for REST API of POST feature.
 */
@RestController
@RequestMapping(value = "/api/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private PostTypeService postTypeService;

    private final Logger logger = LoggerFactory.getLogger(PostController.class);

    /**
     * @return Latest Paginated Post in descending order of it's time, 10 at a time
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getPosts(@RequestParam("pageNum") int pageNumber) {
        if (pageNumber < 0)
            pageNumber = 0;
        ApiResponse response = postService.getRecentPosts(pageNumber);
        logger.info("ResponseMessage for getPosts() is : {}", response.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * @return list of postType
     */
    @GetMapping(value = "/postTypes")
    public ResponseEntity<ApiResponse> getPostType() {
        ApiResponse response = postTypeService.getAllPostType();
        logger.info("ResponseMessage for getPostType() is : {}", response.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * @param userId
     * @return all post by particular user
     */
    @GetMapping(value = "/byUserId")
    public ResponseEntity<ApiResponse> getPostsByUser(@RequestParam(value = "userId") String userId) {
        ApiResponse response = postService.getPostByUserId(userId.toLowerCase(Locale.ROOT));
        logger.info("ResponseMessage for getPostsByUser() is : {}", response.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * @param postId
     * @return complete post
     */
    @GetMapping(value = "/byId")
    public ResponseEntity<ApiResponse> getPostById(@RequestParam(value = "postId") int postId) {
        ApiResponse response = postService.findPostById(postId);
        logger.info("ResponseMessage for getPostById() is : {}", response.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * @param postDetails
     * @param files
     * @return inserted post
     */
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> addPost(@ModelAttribute PostDetails postDetails,
                                               @RequestPart(required = false) MultipartFile[] files) {
        ApiResponse response = postService.addPost(postDetails.getPostTypeId(),
                postDetails.getUserId().toLowerCase(Locale.ROOT), postDetails.getPostContent(), files);
        logger.info("ResponseMessage for addPost() is : {}", response.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * @param postData
     * @return ResponseEntity
     */
    @PutMapping
    public ResponseEntity<ApiResponse> updatePost(UpdatePostDetails postData) {
        ApiResponse response = postService.updatePost(postData.getPostId(), postData.getPostContent());
        logger.info("ResponseMessage for updatePost() is : {}", response.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * @param postId
     * @return ResponseEntity
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse> deletePost(@RequestParam(value = "postId") int postId) {
        ApiResponse response = postService.deletePost(postId);
        logger.info("ResponseMessage for deletePost() is : {}", response.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/byPostTypeId")
    public ResponseEntity<ApiResponse> getPostByPostTypeId(@RequestParam(value = "postTypeId")
                                                           int postTypeId){
        ApiResponse response=postService.postByPostTypeId(postTypeId);
        logger.info("ResponseMessage for getPostByPostId() is : {}", response.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @RequestMapping(method = RequestMethod.POST,
            value = "/addPostType",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> addPostType(@RequestParam(value = "postTypeName")
                                                   String postTypeName,
                                                   @RequestPart(required = true) MultipartFile file) {
        ApiResponse response = postTypeService.addPostType(postTypeName,file);
        logger.info("ResponseMessage for addPostType() is : {}", response.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @RequestMapping(method = RequestMethod.PUT,
            value = "/addPostType",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> updatePostType(@RequestParam(value = "postTypeId")
                                                           int postTypeId,
                                                      @RequestParam(value = "postTypeName")
                                                              String postTypeName,
                                                   @RequestPart(required = true) MultipartFile file) {
        ApiResponse response = postTypeService.updatePostType(postTypeId, postTypeName, file);
        logger.info("ResponseMessage for updatePostType() is : {}", response.getMessage());

        return ResponseEntity.status(response.getStatus()).body(response);
    }


}
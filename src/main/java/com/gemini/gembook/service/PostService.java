package com.gemini.gembook.service;

import com.gemini.gembook.dto.ApiResponse;
import com.gemini.gembook.model.GemFiles;
import com.gemini.gembook.model.Post;
import com.gemini.gembook.model.PostType;
import com.gemini.gembook.model.User;
import com.gemini.gembook.repository.PhotoRepository;
import com.gemini.gembook.repository.PostRepository;
import com.gemini.gembook.repository.PostTypeRepository;
import com.gemini.gembook.repository.UsersRepository;
import com.gemini.gembook.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class to serve various requests managing a post on the server.
 */
@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private PostTypeRepository postTypeRepository;

    @Autowired
    private FileStorageService fileStorageService;

    private final Logger logger = LoggerFactory.getLogger(PostService.class);

    /**
     * @return 10 latest Post
     */
    public ApiResponse getRecentPosts(int pageNumber) {
        try {
            Pageable pageable = PageRequest.of(pageNumber, Constants.PAGINATED_POST_COUNT, Sort.by("postTime").descending());
            Page<Post> pagedData = postRepository.findAll(pageable);
            return new ApiResponse(HttpStatus.OK, pagedData.toList());
        } catch (Exception e) {
            logger.error("postRepository.getRecentPosts() throws an exception: {}", e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch Posts");
        }
    }

    /**
     * @param postId id for post
     * @return Post
     */
    public ApiResponse findPostById(int postId) {
        try {
            return postRepository.findById(postId).map(post ->
                    new ApiResponse(HttpStatus.OK, post)
            ).orElse(new ApiResponse(HttpStatus.NOT_ACCEPTABLE, "Post doesn't exists"));
        } catch (Exception e) {
            logger.error("Exception in findPostById() : {}", e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch Post");
        }
    }

    /**
     *
     * @param fileName
     * @return
     */

    public boolean contentTypeVideo(String fileName) {
        String[] fileArrSplit = fileName.split("\\.");
        String fileExtension = fileArrSplit[fileArrSplit.length - 1];
        switch (fileExtension) {
            case "mp4":
            case "mov":
            case "wmv":
            case "flv":
            case "avi":
            case "avchd":
            case "webm":
            case "mkv":
                return true;
            default:
                return false;
        }
    }

    /**
     * @param userId
     * @param postContent
     * @param files
     * @return inserted Post
     */
    public ApiResponse addPost(int postTypeId, String userId, String postContent,
                               MultipartFile[] files) {
        try {
            if (files != null) {
                for (MultipartFile file : files) {
                    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                    if (contentTypeVideo(fileName)) {
                        logger.error("Invalid Content Type : Video Type");
                        return new ApiResponse(HttpStatus.BAD_REQUEST,
                                "Required Image Sending Video, Currently Video Post are not acceptable");
                    }
                }
            }
            Optional<User> user = userRepository.findById(userId);
            if (!user.isPresent())
                return new ApiResponse(HttpStatus.NOT_ACCEPTABLE, "User doesn't exists");

            Optional<PostType> postType = postTypeRepository.findById(postTypeId);
            if (!postType.isPresent())
                return new ApiResponse(HttpStatus.NOT_ACCEPTABLE, "PostType doesn't exists");

            Post post = new Post(postContent);
            post.setUser(user.get());
            post.setPostType(postType.get());
            post = postRepository.save(post);
            if (files != null) {
                post.setGemFiles(saveAllFiles(post, files));
            }

            return new ApiResponse(HttpStatus.CREATED, post);
        } catch (Exception e) {
            logger.error("Exception in addPost() : {}", e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to add Post");
        }
    }

    /**
     * @param postId
     * @param postContent
     * @return ApiResponse
     */
    public ApiResponse updatePost(int postId, String postContent) {
        try {
            return postRepository.findById(postId).map(post -> {
                post.setPostContent(postContent);
                return new ApiResponse(HttpStatus.OK, postRepository.save(post));
            }).orElse(new ApiResponse(HttpStatus.BAD_REQUEST, "Post doesn't exists"));
        } catch (Exception e) {
            logger.error("Exception in updatePost() : {} ", e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update post!");
        }
    }

    /**
     * @param postId
     * @return true if post is deleted
     */
    public ApiResponse deletePost(int postId) {
        try {
            return postRepository.findById(postId).map(post -> {
                fileStorageService.deleteFiles(post.getGemFiles());
                postRepository.delete(post);
                return new ApiResponse(HttpStatus.OK, "Post deleted");
            }).orElse(new ApiResponse(HttpStatus.BAD_REQUEST, "Post doesn't exists"));
        } catch (Exception e) {
            logger.error("Exception in deletePost() : {}", e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete post");
        }
    }

    /**
     * @param userId
     * @return list of post by user
     */
    public ApiResponse getPostByUserId(String userId) {
        try {
            return userRepository.findById(userId).map(user -> {
				List<Post> postList= postRepository.findPostByUserUserId(userId);
                return new ApiResponse(HttpStatus.OK, postList);
            }).orElse(new ApiResponse(HttpStatus.NOT_ACCEPTABLE, "User doesn't exists"));
        } catch (Exception e) {
            logger.error("postRepository.getPostByUserName() throws an exception: {}", e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch posts");
        }
    }

    private GemFiles saveFile(Post post, MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);
        GemFiles gemFiles = new GemFiles(post, fileName, file.getContentType());
        gemFiles = photoRepository.save(gemFiles);
        return gemFiles;
    }

    private List<GemFiles> saveAllFiles(Post post, MultipartFile[] files) {
        if (files != null && files.length > 0) {
            logger.info("inside ");
            return Arrays.stream(files)
                    .map(file -> saveFile(post, file))
                    .collect(Collectors.toList());
        } else return null;
    }

    public ApiResponse postByPostTypeId(int postTypeId) {
        try{
        List<Post> postList=postRepository.findPostByPostTypePostTypeId(postTypeId);
        return new ApiResponse(HttpStatus.OK,postList);
        }catch(Exception e){
            logger.error("postByPostTypeId() throws an exception",e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,"failed to fetch posts");
        }
    }




}
package com.gemini.gembook.service;

import com.gemini.gembook.dto.ApiResponse;
import com.gemini.gembook.model.PostType;
import com.gemini.gembook.repository.PostTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service class to serve requests related with post's types on the server.
 */
@Service
public class PostTypeService {

    @Autowired
    private PostTypeRepository postTypeRepository;

    @Autowired
    private FileStorageService fileStorageService;


    private final Logger logger = LoggerFactory.getLogger(PostTypeService.class);

    /**
     * Retrieves all post types stored in database.
     *
     * @return List containing all postTypes
     */
    public ApiResponse getAllPostType() {
        try {
            return new ApiResponse(HttpStatus.OK, postTypeRepository.findAll());
        } catch (Exception e) {
            logger.error("postTypeRepository.findAll(); throws an exception: {}", e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch PostTypes");
        }
    }

    public ApiResponse addPostType(String postTypeName, MultipartFile file) {
        PostType postType=new PostType();
        postType.setPostType(postTypeName);
        postType.setIconFile(saveFile(file));
        try{
            return new ApiResponse(HttpStatus.OK,postTypeRepository.save(postType));
        } catch (Exception e){
            logger.error("postTypeRepository.addPostType() throws an exception: {}",e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,"Unable to add PostType");
        }

    }

    private String saveFile(MultipartFile file) {
        return fileStorageService.storeFile(file);
    }

    public ApiResponse updatePostType(int postTypeId, String postTypeName, MultipartFile file) {

        PostType postType = postTypeRepository.findById(postTypeId).orElse(null);
        if(postType == null) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, "PostType Id not found!");
        }
        if(!postTypeName.trim().isEmpty()) {
            postType.setPostType(postTypeName);
        }
        postType.setIconFile(saveFile(file));
        try{
            return new ApiResponse(HttpStatus.OK,postTypeRepository.save(postType));
        } catch (Exception e){
            logger.error("postTypeRepository.updatePostType() thrown an exception: {}",e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,"Unable to update PostType");
        }
    }
}

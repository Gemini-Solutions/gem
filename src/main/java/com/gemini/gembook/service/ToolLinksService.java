package com.gemini.gembook.service;

import com.gemini.gembook.dto.ApiResponse;
import com.gemini.gembook.model.GemFiles;
import com.gemini.gembook.model.Post;
import com.gemini.gembook.model.ToolLinks;
import com.gemini.gembook.repository.ToolLinksRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ToolLinksService {

    private final Logger logger = LoggerFactory.getLogger(ToolLinksService.class);

    @Autowired
    private ToolLinksRepository toolLinksRepository;

    @Autowired
    private FileStorageService fileStorageService;


    public ApiResponse getAllToolLinks() {
        try {
            return new ApiResponse(HttpStatus.OK, toolLinksRepository.findAll());
        } catch (Exception e) {
            logger.error("Exception while fetching ToolLinks : {}", e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch links");
        }
    }

    public ApiResponse getToolLinkById(String toolName) {
        try {
            return toolLinksRepository.findById(toolName).map(toolLink ->
                    new ApiResponse(HttpStatus.OK, toolLink)
            ).orElse(new ApiResponse(HttpStatus.NOT_ACCEPTABLE, "No ToolLink available with name : " + toolName));
        } catch (Exception e) {
            logger.error("Exception while fetching ToolLink {} : {}", toolName, e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch ToolLink for : " + toolName);
        }
    }

    public ApiResponse addToolsLink(String toolName, String toolLink, MultipartFile file) {
        try {
            ToolLinks tool = new ToolLinks();
            tool.setToolName(toolName);
            tool.setToolLink(toolLink);
            tool.setIconFileName(saveFile(file));
            toolLinksRepository.save(tool);
            return new ApiResponse(HttpStatus.OK,tool);
        }catch (Exception e){
            logger.error("Exception in addToolsLink() : {}",e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,"Failed to add Tool");

        }

    }

    private String saveFile(MultipartFile file) {
        return fileStorageService.storeFile(file);
    }

    public ApiResponse updateToolsLink(String toolName, String toolLink, MultipartFile file) {

        ToolLinks toolLinks = toolLinksRepository.findById(toolName).orElse(null);
        if(toolLinks == null){
            return new ApiResponse(HttpStatus.BAD_GATEWAY, "Tool Name doesn't exist");
        }
        if(!toolLink.trim().isEmpty()){
            toolLinks.setToolLink(toolLink);
        }
        toolLinks.setIconFileName(saveFile(file));
        try{
            return new ApiResponse(HttpStatus.OK,toolLinksRepository.save(toolLinks));
        } catch (Exception e){
            logger.error("postTypeRepository.updateToolLink() thrown an exception: {}",e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,"Unable to update toolLink");
        }

    }
}

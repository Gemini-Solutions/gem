package com.gemini.gembook.controller;

import com.gemini.gembook.dto.ApiResponse;
import com.gemini.gembook.dto.PostDetails;
import com.gemini.gembook.model.ToolLinks;
import com.gemini.gembook.service.ToolLinksService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/toolLinks")
public class ToolLinksController {

    private final Logger logger = LoggerFactory.getLogger(ToolLinksController.class);

    @Autowired
    private ToolLinksService toolLinksService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllToolLinks() {
        ApiResponse response = toolLinksService.getAllToolLinks();
        logger.info("ResponseMessage for getAllToolLinks() is : {}",response.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping(value = "/{toolName}")
    public ResponseEntity<ApiResponse> getToolLinkByName(@PathVariable String toolName) {
        ApiResponse response = toolLinksService.getToolLinkById(toolName);
        logger.info("ResponseMessage for getToolLinkByName() is : {}",response.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> addTool(@ModelAttribute ToolLinks toolLinks,
                                               @RequestPart(required = true) MultipartFile file) {
        ApiResponse response=toolLinksService.addToolsLink(toolLinks.getToolName(),
                toolLinks.getToolLink(), file);
        logger.info("ResponseMessage for addTool() is : {}", response.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @RequestMapping(method = RequestMethod.PUT,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> updateTool(@RequestParam(value = "toolName")
                                                           String toolName,
                                               @RequestParam(value = "toolLink")
                                                           String toolLink,
                                               @RequestPart MultipartFile file) {
        ApiResponse response=toolLinksService.updateToolsLink(toolName, toolLink, file);
        logger.info("ResponseMessage for updateTool() is : {}", response.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}

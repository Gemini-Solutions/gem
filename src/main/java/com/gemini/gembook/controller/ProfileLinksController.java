package com.gemini.gembook.controller;

import com.gemini.gembook.dto.UpdateProfileLink;
import com.gemini.gembook.model.ProfileLinks;
import com.gemini.gembook.service.EmployeeProfileService;
import com.gemini.gembook.service.ProfileLinksService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/links")
public class ProfileLinksController {

    @Autowired
    ProfileLinksService profileLinksService;

    @Autowired
    EmployeeProfileService employeeProfileService;

    private final Logger logger = LoggerFactory.getLogger(ProfileLinksController.class);

    @PostMapping
    public BaseResponse addProfileLinks(@RequestParam(value = "employeeId") int employeeId,
                                        @Valid @RequestBody ProfileLinks profileLinks) {
        if (employeeProfileService.getProfileById(employeeId) == null) {
            logger.warn("Id does not exist");
            return new BaseResponse("id not exist", HttpStatus.BAD_REQUEST, null);
        }
        return profileLinksService.addProfileLink(employeeId, profileLinks);
    }

    @GetMapping
    public BaseResponse getLinksByEmpId(@RequestParam(value = "employeeId") int employeeId) {
        if (employeeProfileService.getProfileById(employeeId) == null) {
            logger.warn("Id does not exist");
            return new BaseResponse("EmployeeId not exist", HttpStatus.BAD_REQUEST, null);
        }
        List<ProfileLinks> profileLinks = profileLinksService.getLinksByEmpId(employeeId);
        if(profileLinks == null){
            logger.error("Unable to fetch links");
            return new BaseResponse("Unable to fetch links",HttpStatus.INTERNAL_SERVER_ERROR,null);
        }
        if(profileLinks.isEmpty()){
            return new BaseResponse("No Links Found",HttpStatus.OK,profileLinks);
        }
        logger.info("links fetched");
        return new BaseResponse("Links fetched", HttpStatus.OK, profileLinks);


    }

    @PutMapping
    public BaseResponse updateLink(@RequestParam(value = "employeeId") int employeeId,
                                   @RequestParam(value = "linkName") String linkName,
                                   @Valid @RequestBody UpdateProfileLink profileLinks) {
        if (employeeProfileService.getProfileById(employeeId) == null) {
            return new BaseResponse("EmployeeId not exist", HttpStatus.BAD_REQUEST, null);
        }

        ProfileLinks updatedLink = profileLinksService.updateProfileLink(employeeId, linkName, profileLinks);
        if(updatedLink == null){
            return new BaseResponse("LinkName not valid",HttpStatus.BAD_REQUEST,null);
        }
        return new BaseResponse("Updated successfully", HttpStatus.OK, updatedLink);


    }

    @DeleteMapping
    public BaseResponse DeleteProfileLinks(@RequestParam(value = "employeeId") int employeeId,
                                      @RequestParam(value = "linkName") String linkName){
        if(employeeProfileService.getProfileById(employeeId)== null){
            logger.warn("EmployeeId does not exist");
            return new BaseResponse("EmployeeId not exist",HttpStatus.BAD_REQUEST,null);
        }
        if(profileLinksService.deleteLink(employeeId,linkName)){
            return new BaseResponse("Deleted successfully",HttpStatus.OK,true);
        }
        logger.error("Unable tp delete link");
        return new BaseResponse("Unable to Delete",HttpStatus.INTERNAL_SERVER_ERROR,false);

    }
}

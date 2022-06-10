package com.gemini.gembook.service;

import com.gemini.gembook.controller.BaseResponse;
import com.gemini.gembook.dto.UpdateProfileLink;
import com.gemini.gembook.model.Education;
import com.gemini.gembook.model.EducationId;
import com.gemini.gembook.model.ProfileLinks;
import com.gemini.gembook.model.ProfileLinksId;
import com.gemini.gembook.repository.EmployeeProfileRepo;
import com.gemini.gembook.repository.ProfileLinksRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileLinksService {

    @Autowired
    ProfileLinksRepo profileLinksRepo;

    @Autowired
    EmployeeProfileRepo employeeProfileRepo;

    private final Logger logger = LoggerFactory.getLogger(ProfileLinksRepo.class);

    public BaseResponse addProfileLink(int employeeId, ProfileLinks profileLinks) {
        try {
            return employeeProfileRepo.findById(employeeId).map(employeeProfile -> {
                profileLinks.setEmployeeId(employeeProfile);
                profileLinksRepo.save(profileLinks);
                return new BaseResponse("Links added", HttpStatus.CREATED, profileLinks);
            }).orElse(new BaseResponse("id is wrong", HttpStatus.BAD_REQUEST, null));
        } catch (Exception e) {
            logger.error("Error while adding links : {}", e.getMessage());
            return new BaseResponse("exception", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }


    }

    public List<ProfileLinks> getLinksByEmpId(int employeeId) {
        List<ProfileLinks> links = null;
        try {
            links = profileLinksRepo.getLinksByEmpId(employeeId);
        } catch (Exception e) {
            logger.error("An Exception occured in getLinksByEmpId() : {}", e.getMessage());
        }
        return links;

    }

    public ProfileLinks updateProfileLink(int employeeId, String linkName, UpdateProfileLink profileLinks) {
        try {
            ProfileLinksId linkId = new ProfileLinksId(employeeId, linkName);
            ProfileLinks link = profileLinksRepo.findById(linkId).orElse(null);
            if(link == null){
                logger.error("LinkName not valid");
                return null;
            }
            link.setLinkUrl(profileLinks.getLinkUrl());
            return profileLinksRepo.save(link);

        } catch (Exception e) {
            logger.error("Exception in updateProfileLink() : {} ", e.getMessage());
        }
        return null;
    }

    public boolean deleteLink(int employeeId, String linkName) {
        ProfileLinksId linksId=new ProfileLinksId(employeeId,linkName);
        try{

            ProfileLinks links=profileLinksRepo.findById(linksId).orElse(null);
            profileLinksRepo.delete(links);

        }catch (Exception e){
            logger.error("Exception in deleteLink() : {}",e.getMessage());
        }
        ProfileLinks link=profileLinksRepo.findById(linksId).orElse(null);

        return link == null;
    }
}

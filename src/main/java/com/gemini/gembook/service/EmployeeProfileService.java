package com.gemini.gembook.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.gemini.gembook.dto.UpdateProfileDetails;
import com.gemini.gembook.model.EmployeeManagerDetails;
import com.gemini.gembook.model.EmployeeProfile;
import com.gemini.gembook.projections.EmployeeProfileProjection;
import com.gemini.gembook.repository.EmployeeProfileRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeProfileService {

    @Autowired
    EmployeeProfileRepo employeeProfileRepo;

    private final Logger logger = LoggerFactory.getLogger(EmployeeProfileService.class);
/*
    public EmployeeProfile addProfile(EmployeeProfile profile) {
        EmployeeProfile employeeProfile=null;
        LocalDate localDate = LocalDate.of(2020, 5, 01);
        profile.setJoiningDate(localDate);
        try{

            LocalDate date = LocalDate.now();
            LocalDate joiningDate=profile.getJoiningDate();


            Period age = Period.between(joiningDate,date);
            int years = age.getYears();
            int months = age.getMonths();
            int duration=(years*12)+months;


            employeeProfile=employeeProfileRepo.save(new EmployeeProfile(profile.getName(),profile.getEmail(),
                    profile.getJoiningDate(),profile.getDesignation(),profile.getBirthday(),profile.getContactNo(),
                    profile.getTeamName(),profile.getHobbies(),profile.getSkills(),
                    duration, profile.getExpBeforeGeminiInMonths(),profile.getAboutMe(),profile.getGemTalkDelivered(),profile.getTrainingsDone(),
                    profile.getAchievements(),
                    profile.getReportingManager()));
            employeeProfile.setReportees(employeeProfileRepo.getAllReportees(employeeProfile.getName()));

        }catch (Exception e){
            logger.error("Exception in addProfile() : {} ",e.getMessage());
        }
        return employeeProfile;


    }

 */

    public List<EmployeeProfile> getAllProfiles() {
        List<EmployeeProfile> profiles = null;
        try {
            profiles = employeeProfileRepo.findAll();
            for (EmployeeProfile employee : profiles) {
                // employee.setReportees(employeeProfileRepo.getAllReportees(employee.getEmail()));
                employee.setReportees(employeeProfileRepo.getAllReportees(employee.getName()));
            }
        } catch (Exception e) {
            logger.error("Exception in getAllProfiles() method : {}", e.getMessage());
        }
        return profiles;
    }

    public EmployeeProfile getProfileById(int employeeId) {
        EmployeeProfile profile = null;
        List<String> reportees;
        try {
            profile = employeeProfileRepo.findById(employeeId).orElse(null);
            reportees = employeeProfileRepo.getAllReportees(profile.getName());
            if (reportees.isEmpty()) {
                profile.setReportees(null);
            }
            profile.setReportees(reportees);


        } catch (Exception e) {
            logger.error("Exception in getProfileById() : {}", e.getMessage());
        }
        return profile;
    }

    public EmployeeProfile updateProfileDetails(
            UpdateProfileDetails
                    profileDetails, int employeeId
    ) {
        EmployeeProfile profile = getProfileById(employeeId);
        profile.setReportees(employeeProfileRepo.getAllReportees(profile.getName()));
        try {
            if (profileDetails.getSkills() != null) {

                profile.setSkills(profileDetails.getSkills());
            }
            if (profileDetails.getAchievements() != null) {
                profile.setAchievements(profileDetails.getAchievements());
            }
            if (profileDetails.getTrainingsDone() != null) {
                profile.setTrainingsDone(profileDetails.getTrainingsDone());
            }
            if (profileDetails.getGemTalkDelivered() != null) {
                profile.setGemTalkDelivered(profileDetails.getGemTalkDelivered());
            }
            if (profileDetails.getHobbies() != null) {
                profile.setHobbies(profileDetails.getHobbies());
            }
        } catch (Exception e) {
            logger.error("Exception in updateProfileDetails() : {}", e.getMessage());
        }

        return employeeProfileRepo.save(profile);

    }

    public boolean deleteProfile(int employeeId) {
        try {
            EmployeeProfile profile = employeeProfileRepo.findById(employeeId).orElse(null);
            if (profile == null) {
                return false;
            }
            employeeProfileRepo.delete(profile);
        } catch (Exception e) {
            logger.error("Exception in deleteProfile() : {}", e.getMessage());

        }
        return getProfileById(employeeId) == null;
    }

    public EmployeeManagerDetails getManagerDetails(int employeeId) {
        EmployeeProfileProjection managerDetails = null;
        try {
            managerDetails = employeeProfileRepo.getManagerByEmpId(employeeId);
        } catch (Exception e) {
            logger.error("Exception in getManagerDetails() : {}", e.getMessage());
        }
        return fetchManagerDetails(managerDetails);
    }

    private EmployeeManagerDetails fetchManagerDetails(final EmployeeProfileProjection managerDetails) {
        EmployeeManagerDetails resp = new EmployeeManagerDetails();
        String email = managerDetails.getEmail();
        if (email == null || email.isEmpty()) {
            resp.setEmail(managerDetails.getEmail());
            resp.setDesignation(managerDetails.getDesignation());
            resp.setName(managerDetails.getName());
        }
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url
                    = "https://empdirectorysvcapi.geminisolutions.com/employee-directory/user-by-email";

            //header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            //Email list
            List<String> emailList = new ArrayList<String>(0);
            emailList.add(email);
            //httpEnitity
            HttpEntity<Object> requestEntity = new HttpEntity<Object>(emailList, headers);
            ResponseEntity<JsonNode> response
                    = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
                    new ParameterizedTypeReference<JsonNode>() { });
            logger.info("response status from MIS api : {}", response.getStatusCode());
            resp.setEmail(managerDetails.getEmail());
            resp.setDesignation(managerDetails.getDesignation());
            resp.setName(managerDetails.getName());
            if (response.getBody() != null || response.getBody().size() > 0) {
                JsonNode empObj = response.getBody().get("object");
                if(empObj.isArray()) {
                    resp.setImageUrl(empObj.get(0).get("imagePath").asText());
                    resp.setLocation(empObj.get(0).get("location").asText());
                }
            }
        } catch (Exception e) {
            logger.error("Error fetching manager details from mis. {}", e.getMessage());
        }
        return resp;
    }


    public Integer getEmpIdByEmail(String email) {
        Integer empId = null;
        try {
            empId = employeeProfileRepo.EmpIdByEmail(email);
        } catch (Exception e) {
            logger.error("Exception in getEmpIdByEmail():{}", e.getMessage());
        }

        return empId;
    }
}

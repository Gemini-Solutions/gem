package com.gemini.gembook.service;

import com.gemini.gembook.controller.BaseResponse;
import com.gemini.gembook.dto.UpdateEducation;
import com.gemini.gembook.model.Education;
import com.gemini.gembook.model.EducationId;
import com.gemini.gembook.repository.EducationRepository;
import com.gemini.gembook.repository.EmployeeProfileRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EducationService {

    @Autowired
    EducationRepository educationRepository;

    @Autowired
    EmployeeProfileRepo employeeProfileRepo;

    private final Logger logger = LoggerFactory.getLogger(EducationService.class);

    public BaseResponse addEducation(int employeeId, Education education) {
        try {
            return employeeProfileRepo.findById(employeeId).map(employeeProfile -> {
                education.setEmployee(employeeProfile);
                educationRepository.save(education);
                return new BaseResponse("Education details added", HttpStatus.CREATED, education);
            }).orElse(new BaseResponse("EmployeeId does not exist", HttpStatus.BAD_REQUEST, null));
        } catch (Exception e) {
            logger.error("Error while adding education details : {}", e.getMessage());
            return new BaseResponse("exception", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    public List<Education> getEducationByEmpId(int employeeId) {
        List<Education> education = null;
        try {
            education = educationRepository.getEducationByEmpId(employeeId);
        } catch (Exception e) {
            logger.error("Exception in getEducationByEmpId: {}", e.getMessage());
        }
        return education;

    }


    public Education updateEducationDetails(int employeeId, String degree, UpdateEducation education) {
        Education details = null;
        try {
            EducationId educationId = new EducationId(employeeId, degree);
            details = educationRepository.findById(educationId).orElse(null);
            if(details == null){
                logger.error("Not a Valid Degree");
                return null;
            }
            if(education.getCollege()!=null && !education.getCollege().isEmpty())
            details.setCollege(education.getCollege());
            if(education.getFromYear()!=null && !education.getFromYear().isEmpty())
            details.setFromYear(education.getFromYear());
            if(education.getToYear()==null && !education.getToYear().isEmpty())
            details.setToYear(education.getToYear());
            return educationRepository.save(details);

        } catch (Exception e) {
            logger.error("exception in updateEducationDetails() : {}", e.getMessage());
        }
        return details;

    }



    public boolean deleteDetails(int employeeId, String degree) {
        EducationId educationId=new EducationId(employeeId,degree);
        try{
            Education details=educationRepository.findById(educationId).orElse(null);
            educationRepository.delete(details);
        }catch (Exception e){
            logger.error("Exception in deleteDetails() : {}",e.getMessage());
        }
        Education education=educationRepository.findById(educationId).orElse(null);
        return education == null;
    }
}

package com.gemini.gembook.controller;

import com.gemini.gembook.dto.UpdateEducation;
import com.gemini.gembook.model.Education;
import com.gemini.gembook.model.EmployeeProfile;
import com.gemini.gembook.service.EducationService;
import com.gemini.gembook.service.EmployeeProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value="/educationDetails")
public class EducationController {

    @Autowired
    EducationService educationService;

    @Autowired
    EmployeeProfileService employeeProfileService;

    private final Logger logger = LoggerFactory.getLogger(EducationController.class);

    @PostMapping
    public BaseResponse addEducationDetails(
            @RequestParam(value = "employeeId") int employeeId,
            @Valid @RequestBody Education education
    ) {

        return educationService.addEducation(employeeId,education);

    }

    @GetMapping
    public BaseResponse getDetailsByEmployeeId(@RequestParam(value="employeeId") int employeeId){
        if(employeeProfileService.getProfileById(employeeId)== null){
            logger.warn("EmployeeId does not exist");
            return new BaseResponse("EmployeeId not exist",HttpStatus.BAD_REQUEST,null);
        }
        List<Education> education=educationService.getEducationByEmpId(employeeId);
        if(education == null){
            logger.error("Unable to fetch Details");
            return new BaseResponse("Error occured",HttpStatus.INTERNAL_SERVER_ERROR,null);
        }
        if(education.isEmpty()){
            return new BaseResponse("No Details found",HttpStatus.OK,education);

        }
        return new BaseResponse("Details fetched successfully",HttpStatus.OK,education);

    }



    @PutMapping
    public BaseResponse updateDetails(@RequestParam(value = "employeeId") int employeeId,
                                      @RequestParam(value = "degree") String degree,
                                      @Valid @RequestBody UpdateEducation education){
        if(employeeProfileService.getProfileById(employeeId)== null){
            logger.warn("EmployeeId does not exist");
            return new BaseResponse("EmployeeId not exist",HttpStatus.BAD_REQUEST,null);
        }
        Education updatedDetails=educationService.updateEducationDetails(employeeId,degree,education);
        if(updatedDetails == null){
            logger.warn("No a valid degree");
            return new BaseResponse("Degree Entered is not valid",HttpStatus.BAD_REQUEST,null);
        }
        return new BaseResponse("Updated successfully",HttpStatus.OK,updatedDetails);
    }

    @DeleteMapping
    public BaseResponse DeleteDetails(@RequestParam(value = "employeeId") int employeeId,
                                      @RequestParam(value = "degree") String degree){
        if(employeeProfileService.getProfileById(employeeId)== null){
            logger.warn("EmployeeId does not exist");
            return new BaseResponse("EmployeeId not exist",HttpStatus.BAD_REQUEST,null);
        }
        if(educationService.deleteDetails(employeeId,degree)){
            return new BaseResponse("Deleted successfully",HttpStatus.OK,true);
        }
        logger.error("Unable to delete education details");
        return new BaseResponse("Unable to Delete",HttpStatus.INTERNAL_SERVER_ERROR,false);

        }

}

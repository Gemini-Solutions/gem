package com.gemini.gembook.controller;

import com.gemini.gembook.dto.UpdateProfileDetails;
import com.gemini.gembook.model.EmployeeManagerDetails;
import com.gemini.gembook.model.EmployeeProfile;
import com.gemini.gembook.model.User;
import com.gemini.gembook.projections.EmployeeProfileProjection;
import com.gemini.gembook.repository.EmployeeProfileRepo;
import com.gemini.gembook.repository.UsersRepository;
import com.gemini.gembook.service.EmployeeProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/profile")
public class EmployeeProfileController {

    @Autowired
    EmployeeProfileService employeeProfileService;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    EmployeeProfileRepo employeeProfileRepo;

    private final Logger logger = LoggerFactory.getLogger(EmployeeProfileController.class);

    @GetMapping
    public BaseResponse getAllProfileDetails(){
        List<EmployeeProfile> profiles= employeeProfileService.getAllProfiles();
        if(profiles == null){
            logger.warn("Unable to fetch employee profiles");
            return new BaseResponse("employee profiles not fetched", HttpStatus.INTERNAL_SERVER_ERROR,null);
        }
        if(profiles.isEmpty()){
            logger.info("No Profile Details found");
            return new BaseResponse("No employee details present",HttpStatus.NOT_FOUND,null);
        }
        logger.info("All Profile Details fetched");
        return new BaseResponse("profile details fetched successfully",HttpStatus.OK,profiles);
    }

    @GetMapping(value = "/employeeDetailsById")
    public BaseResponse getProfileByEmpId(@RequestParam(value = "employeeId") int employeeId ){
        EmployeeProfile profile=employeeProfileService.getProfileById(employeeId);
        if(profile == null){
            logger.info("Profile details not found");
            return new BaseResponse("Profile details not found",HttpStatus.NOT_FOUND,null);
        }
        else{
            logger.info("profile details fetched successfully");
            return new BaseResponse("profile details fetched successfully",HttpStatus.OK,profile);
        }

    }

    @PutMapping
    public BaseResponse updateProfile(@Valid @RequestBody UpdateProfileDetails profileDetails,
                                      @RequestParam(value="employeeId") int employeeId){
        //EmployeeProfile profile= employeeProfileService.getProfileById(employeeId);
        if(employeeProfileService.getProfileById(employeeId) == null){
            logger.warn("Employee Id {} does not exist",employeeId);
            return new BaseResponse("Employee Id does not exist",HttpStatus.BAD_REQUEST,null);
        }
       EmployeeProfile profile=employeeProfileService.updateProfileDetails(profileDetails,employeeId);
        if(profile == null){
            logger.error("Could not update employee profile");
            return new BaseResponse("Unable to update Profile",HttpStatus.INTERNAL_SERVER_ERROR,null);
        }
        logger.info("Employee Profile updated");
        return new BaseResponse("Profile updated successfully",HttpStatus.OK,profile);

    }

    @DeleteMapping
    public BaseResponse deleteProfile(@RequestParam(value = "employeeId") int employeeId){
        if(employeeProfileService.getProfileById(employeeId) == null){
            logger.warn("Employee Id {} does not exist",employeeId);
            return new BaseResponse("Employee Id does not exist",HttpStatus.BAD_REQUEST,null);
        }

        if(employeeProfileService.deleteProfile(employeeId)){
            logger.info("Profile deleted successfully");
            return new BaseResponse("Profile Details Deleted",HttpStatus.OK,true);
        }
        logger.warn("Profile not deleted");
        return new BaseResponse("Unable to delete Profile",HttpStatus.INTERNAL_SERVER_ERROR,false);

        }

        @GetMapping("/managerDetails")
        public BaseResponse getManagerDetailsByEmpId(@RequestParam(value = "employeeId") int employeeId){
            if(employeeProfileService.getProfileById(employeeId) == null){
                logger.warn("Employee Id {} does not exist",employeeId);
                return new BaseResponse("Employee Id does not exist",HttpStatus.BAD_REQUEST,null);
            }
            EmployeeManagerDetails manager=employeeProfileService.getManagerDetails(employeeId);
            if(manager == null){
                logger.warn("Manager details not found in database");
                return new BaseResponse("Manager Details not found",
                        HttpStatus.NOT_FOUND,null);
            }
            logger.info("Manager details retrieved successfully");
            return new BaseResponse("success",HttpStatus.OK,manager);

        }

        @GetMapping("/EmpIdByEmail")
        public BaseResponse getEmpIdByEmail(@RequestParam(value = "email") String email){
         Integer empId=employeeProfileService.getEmpIdByEmail(email);
        if(empId==null){
            User user=usersRepository.findById(email).orElse(null);
            if(user==null){
                return new BaseResponse("invalid,data does not exist in MIS for this emailId",HttpStatus.BAD_REQUEST,null);
            }
            //LocalDate localDate = LocalDate.of(2020, 5, 01);
            LocalDate localDate = user.getJoiningDate();
            LocalDate date = LocalDate.now();
            LocalDate dob = user.getDob();
            String strDob = String.valueOf(dob);
            Period age = Period.between(localDate,date);
            int years = age.getYears();
            int months = age.getMonths();
            int duration=(years*12)+months;
            EmployeeProfile profile=new EmployeeProfile(
                    user.getName(), user.getUserId().toLowerCase(Locale.ROOT), localDate, user.getDesignation(),
                    user.getContact(), user.getTeam(), user.getReportingManagerName(),
                    strDob, duration);
            employeeProfileRepo.save(profile);
            Integer id=employeeProfileService.getEmpIdByEmail(email);
            return new BaseResponse("User created for this email",HttpStatus.OK,id);
        }

        return new BaseResponse("EmpId retrieved successfully",HttpStatus.OK,empId);
        }

}

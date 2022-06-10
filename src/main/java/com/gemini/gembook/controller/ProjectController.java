package com.gemini.gembook.controller;

import com.gemini.gembook.model.Projects;
import com.gemini.gembook.service.EmployeeProfileService;
import com.gemini.gembook.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @Autowired
    EmployeeProfileService employeeProfileService;

    private final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    @PostMapping
    public BaseResponse addProjects(@RequestParam(value = "employeeId") int employeeId,
                                        @Valid @RequestBody Projects projects){

        return projectService.addProjects(employeeId,projects);
    }

    @GetMapping(value = "/projectsByEmployeeId")
    public BaseResponse getProjectsByEmpId(@RequestParam(value="employeeId") int employeeId){
        if(employeeProfileService.getProfileById(employeeId)== null){
            return new BaseResponse("EmployeeId not exist", HttpStatus.BAD_REQUEST,null);
        }
        List<Projects> projects=projectService.getProjectsByEmpId(employeeId);
        if(projects == null){
            logger.error("unable to fetch project details");
            return new BaseResponse("Error occured",HttpStatus.INTERNAL_SERVER_ERROR,null);
        }
        if(projects.isEmpty()){
            return new BaseResponse("No Details found",HttpStatus.OK,projects);
        }
        logger.info("Project details fetched");
        return new BaseResponse("Details fetched successfully",HttpStatus.OK,projects);

    }

    @PutMapping
    public BaseResponse updateProject(//@RequestParam(value = "employeeId") int employeeId,
                                          @RequestParam(value = "projectId") int projectId,
                                          @Valid @RequestBody Projects projects ){
        if(projectService.getProjectById(projectId)== null){
            return new BaseResponse("ProjectId not exist",HttpStatus.BAD_REQUEST,null);
        }
        Projects updatedProject= projectService.updateProjectDetails(projectId,projects);
        if(updatedProject == null){
            return new BaseResponse("Unable to update",HttpStatus.INTERNAL_SERVER_ERROR,null);
        }
        logger.info("Project details Updated");
        return new BaseResponse("Details Updated",HttpStatus.OK,updatedProject);



    }

    @DeleteMapping
    public BaseResponse deleteProject(@RequestParam(value = "projectId") int projectId){

        if(projectService.getProjectById(projectId)== null){
            logger.warn("Id does not exist");
            return new BaseResponse("Id does not exist",HttpStatus.BAD_REQUEST,null);
        }

        if(projectService.deleteProject(projectId)){
            logger.info("Details Deleted successfully");
            return new BaseResponse("success",HttpStatus.OK,true);

        }
        logger.error("Unable to delete");
        return new BaseResponse("error occured",HttpStatus.INTERNAL_SERVER_ERROR,false);
    }


}

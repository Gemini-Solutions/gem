package com.gemini.gembook.service;

import com.gemini.gembook.controller.BaseResponse;
import com.gemini.gembook.model.Certificate;
import com.gemini.gembook.model.Projects;
import com.gemini.gembook.repository.EmployeeProfileRepo;
import com.gemini.gembook.repository.ProjectsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    ProjectsRepository projectsRepository;

    @Autowired
    EmployeeProfileRepo employeeProfileRepo;

    private final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    public BaseResponse addProjects(int employeeId, Projects projects) {
        try {
            return employeeProfileRepo.findById(employeeId).map(employeeProfile -> {
                projects.setEmployee(employeeProfile);
                projectsRepository.save(projects);
                return new BaseResponse("details added", HttpStatus.CREATED, projects);
            }).orElse(new BaseResponse("id is wrong", HttpStatus.BAD_REQUEST, null));
        } catch (Exception e) {
            logger.error("Error while adding certificate details : {}", e.getMessage());
            return new BaseResponse("exception", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    public List<Projects> getProjectsByEmpId(int employeeId) {
        List<Projects> projects = null;
        try {
            projects = projectsRepository.getProjectsByEmpId(employeeId);
        } catch (Exception e) {
            logger.error("getProjectsByEmpId() throws an exception: {}", e.getMessage());
        }
        return projects;
    }

    public Projects getProjectById(int projectId) {
        Projects project=null;
        try {
            project = projectsRepository.findById(projectId).orElse(null);
        } catch (Exception e) {
            logger.error("Exception in getProjectById(): {}", e.getMessage());
        }
        return project;
    }

    public Projects updateProjectDetails(int projectId, Projects projects) {
        try{
            Projects  updatedProject = projectsRepository.findById(projectId).orElse(null);
            if(projects.getProjectName()!=null && !projects.getProjectName().isEmpty())
                updatedProject.setProjectName(projects.getProjectName());
            if(projects.getProjectDescription()!=null && !projects.getProjectDescription().isEmpty() )
                updatedProject.setProjectDescription(projects.getProjectDescription());
            if(projects.getProjectStatus()!=null && !projects.getProjectStatus().isEmpty())
                updatedProject.setProjectStatus(projects.getProjectStatus());
            return projectsRepository.save(updatedProject);

        } catch (Exception e) {
            logger.error("exception in updateProjectDetails() : {}", e.getMessage());
        }
        return null;
    }

    public boolean deleteProject(int projectId) {
        try{
            Projects project=getProjectById(projectId);
            projectsRepository.delete(project);}
        catch (Exception e){
            logger.error("Exception occured in deleteProject() : {}",e.getMessage() );
        }

        return getProjectById(projectId)==null;
    }
}


package com.gemini.gembook.dto;

import com.gemini.gembook.model.Certificate;
import com.gemini.gembook.model.EmployeeProfile;
import com.gemini.gembook.model.ProfileLinks;
import com.gemini.gembook.model.Projects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDetails {

    private int id;
    private String name;
    private String email;
    private String ImageUrl;
    private LocalDate joiningDate;
    private String designation;
    private String birthday;
    private String contactNo;
    private String teamName;
    private String hobbies;
    private String skills;
    private int expInGeminiInMonths;
    private int expBeforeGeminiInMonths;
    private String trainingsDone;
    private String achievements;
    private String gemTalkDelivered;
    private String aboutMe;
    private String reportingManager;
    private List<ProfileLinks> profileLinks;
    private List<Certificate> certificates;
    private List<Projects> projects;
    private List<String> reportees;

    public EmployeeDetails(Optional<EmployeeProfile> entity) {

        if (entity.isPresent()) {
            EmployeeProfile employee = entity.get();
            this.id = employee.getId();
            this.name = employee.getName();
            this.email = employee.getEmail();
            this.joiningDate = employee.getJoiningDate();
            this.designation = employee.getDesignation();
            this.birthday = employee.getBirthday();
            this.contactNo = employee.getContactNo();
            this.teamName = employee.getTeamName();
            this.hobbies = employee.getHobbies();
            this.skills = employee.getSkills();
            this.expInGeminiInMonths = employee.getExpInGeminiInMonths();
            this.expBeforeGeminiInMonths = employee.getExpBeforeGeminiInMonths();
            this.trainingsDone = employee.getTrainingsDone();
            this.achievements = employee.getAchievements();
            this.gemTalkDelivered = employee.getGemTalkDelivered();
            this.reportingManager = employee.getReportingManager();
            this.profileLinks = employee.getProfileLinks();
            this.certificates = employee.getCertificates();
            this.projects = employee.getProjects();
            this.reportees = employee.getReportees();
        }

    }
}

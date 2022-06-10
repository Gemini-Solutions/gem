package com.gemini.gembook.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="employee_profile")
public class EmployeeProfile {

    @ApiModelProperty(hidden = true)
    @Id
    @Column(name = "emp_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Email
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "joining_date")
    @ApiModelProperty(hidden = true)
    private LocalDate joiningDate;

    @Column(name = "designation")
    private String designation;

    @Column(name = "birthday")
    private String birthday;

    @Column(name = "contact_no", length = 10)
    private String contactNo;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "hobbies")
    private String hobbies;

    @Column(name = "skills")
    private String skills;

    @ApiModelProperty(hidden = true)
    @Column(name = "experience_gemini")
    private int expInGeminiInMonths;

    @Column(name = "prev_experience")
    private int expBeforeGeminiInMonths;

    @Column(name = "trainings_done")
    private String trainingsDone;

    @Column(name = "achievements")
    private String achievements;

    @Column(name = "gem_talk")
    private String gemTalkDelivered;

    @Column(name = "about")
    private String aboutMe;

    @Column(name = "manager")
    private String reportingManager;

    @ApiModelProperty(hidden = true)
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER,
            mappedBy = "employee")
    private List<Education> education;


    @ApiModelProperty(hidden = true)
    @OneToMany(cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "employeeId")
    private List<ProfileLinks> profileLinks;

    @ApiModelProperty(hidden = true)
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "employee")
    private List<Certificate> certificates;

    @ApiModelProperty(hidden = true)
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "employee")
    private List<Projects> projects;

    @ApiModelProperty(hidden = true)
    @Transient
    private List<String> reportees;

    public EmployeeProfile(String name, @Email String email, LocalDate joiningDate, String designation, String
            contactNo, String teamName,String reportingManager,
                           String birthday, int expInGeminiInMonths) {
        this.name = name;
        this.email = email;
        this.joiningDate = joiningDate;
        this.designation = designation;
        this.contactNo = contactNo;
        this.teamName = teamName;
        this.reportingManager = reportingManager;
        this.birthday=birthday;
        this.expInGeminiInMonths=expInGeminiInMonths;
    }

    public EmployeeProfile(String name, String email, LocalDate joiningDate, String designation,
                           String birthday, String contactNo, String teamName,
                           String hobbies, String skills, int expInGeminiInMonths,
                           int expBeforeGeminiInMonths, String aboutMe,
                           String gemTalkDelivered, String trainingsDone,
                           String achievements, String reportingManager) {
        this.name = name;
        this.email = email;
        this.joiningDate = joiningDate;
        this.designation = designation;
        this.birthday = birthday;
        this.contactNo = contactNo;
        this.teamName = teamName;
        this.hobbies = hobbies;
        this.skills = skills;
        this.expInGeminiInMonths = expInGeminiInMonths;
        this.expBeforeGeminiInMonths = expBeforeGeminiInMonths;
        this.aboutMe = aboutMe;
        this.gemTalkDelivered = gemTalkDelivered;
        this.trainingsDone = trainingsDone;
        this.achievements = achievements;
        this.reportingManager = reportingManager;
    }

}
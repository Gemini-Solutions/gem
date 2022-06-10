package com.gemini.gembook.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "project")
public class Projects {

    @ApiModelProperty(hidden = true)
    @Id
    @Column(name = "project_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int projectId;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "description")
    private String projectDescription;

    @Column(name = "status")
    private String projectStatus;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "emp_id")
    private EmployeeProfile employee;

}

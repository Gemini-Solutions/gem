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
@Table(name = "certificate")
public class Certificate {

    @ApiModelProperty(hidden = true)
    @Id
    @Column(name = "certificate_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int certificateId;

    @Column(name = "certificate_name")
    private String name;


    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "provider")
    private String provider;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @ManyToOne(fetch= FetchType.EAGER,optional = false)
    @JoinColumn(name="emp_id")
    private EmployeeProfile employee;

}

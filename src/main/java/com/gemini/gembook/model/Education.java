package com.gemini.gembook.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="education_details")
@IdClass(EducationId.class)
public class Education {

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @Id
    @ManyToOne(fetch= FetchType.EAGER,optional = false)
    @JoinColumn(name = "employee",referencedColumnName = "emp_id")
    private EmployeeProfile employee;

    @Id
    @Column(name="degree")
    private String degree;

    @Column(name="college_name")
    private String College;

    @Column(name = "from_year",length = 4)
    private String fromYear;

    @Column(name = "to_year",length = 4)
    private String toYear;

}

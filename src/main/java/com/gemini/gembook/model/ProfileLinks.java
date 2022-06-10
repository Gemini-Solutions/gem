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
@Table(name="profile_links")
@IdClass(ProfileLinksId.class)
public class ProfileLinks {

    @Id
    @Column(name = "link_name")
    private String linkName;

    @Column(name = "link_url")
    private String linkUrl;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @Id
    @ManyToOne(fetch= FetchType.LAZY,optional = false)
    @JoinColumn(name = "employeeId",referencedColumnName = "emp_id")
    private EmployeeProfile employeeId;

}

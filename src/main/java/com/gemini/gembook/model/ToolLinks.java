package com.gemini.gembook.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tool_links")
@Getter
@Setter
@NoArgsConstructor
public class ToolLinks {

    @Id
    @Column(name = "tool_name")
    private String toolName;

    @Column(name = "tool_link")
    private String toolLink;

    @Column(name = "icon_file_name")
    @ApiModelProperty(hidden = true)
    private String iconFileName;


}

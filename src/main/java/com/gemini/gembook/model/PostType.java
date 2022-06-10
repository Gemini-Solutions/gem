package com.gemini.gembook.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * Modal class to contain information regarding type of a post.
 */
@Entity
@Table(name = "post_type")
@Getter
@Setter
@NoArgsConstructor
public class PostType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_type_id")
    private int postTypeId;

    @Column(name = "post_type_name")
    private String postType;

    @Column(name = "icon_file")
    @ApiModelProperty(hidden = true)
    private String iconFile;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "postType")
    private Set<Post> posts;

    public PostType(int postTypeId) {
        this.postTypeId = postTypeId;
    }

    public PostType(String postType,String iconFile) {
        this.postType = postType;
        this.iconFile=iconFile;
    }
}

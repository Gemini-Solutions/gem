package com.gemini.gembook.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.List;

/**
 * Modal class to contain information regarding a User's details.
 */
@Entity
@Table(name = "gem_user")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @Email
    @Column(name = "user_id")
    private String userId;

    @Column(name = "name")
    private String name;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "team")
    private String team;

    @Column(name = "designation")
    private String designation;

    @Column(name = "contact_no")
    private String contact;

    @Column(name = "reporting_manager")
    private String reportingManagerName;

    @Column(name = "reporting_manager_email")
    private String reportingManagerEmail;

    @Column(name = "date_of_birth")
    private LocalDate dob;

    @Column(name = "date_of_joining")
    private LocalDate joiningDate;

    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            mappedBy = "user")
    private List<Post> posts;

    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            mappedBy = "likeIdentity.user")
    private List<PostLike> likes;

    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            mappedBy = "user")
    private List<Comment> comments;

    // causing stack overflow
    /*@JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            mappedBy = "commentIdentity.user")
    private List<CommentLike> commentLikes;*/

    public User(String userId) {
        this.userId = userId;
    }

    public User(String name, String photoUrl, String userId) {
        this.name = name;
        this.photoUrl = photoUrl;
        this.userId = userId;
    }

    public User(@Email String userId, String name, String photoUrl, String team,
                String designation, String contact, String reportingManagerName,
                String reportingManagerEmail, LocalDate dob, LocalDate joiningDate) {
        this.userId = userId;
        this.name = name;
        this.photoUrl = photoUrl;
        this.team = team;
        this.designation = designation;
        this.contact = contact;
        this.reportingManagerName = reportingManagerName;
        this.reportingManagerEmail = reportingManagerEmail;
        this.dob = dob;
        this.joiningDate = joiningDate;
    }
}

package com.gemini.gembook.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Modal class to contain information regarding likes on a Post.
 */
@Entity
@Table(name = "likes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostLike {

    @EmbeddedId
    private PostLikeIdentity likeIdentity;

    @Column(name = "like_flag")
    private int likeFlag;
}

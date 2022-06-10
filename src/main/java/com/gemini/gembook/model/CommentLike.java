package com.gemini.gembook.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Modal class to contain information regarding likes on a comment.
 */
@Entity
@Table(name = "comment_likes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentLike {

    @EmbeddedId
    private CommentLikeIdentity commentIdentity;

    @Column(name = "comment_like_flag")
    private int commentLikeFlag;
}

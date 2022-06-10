package com.gemini.gembook.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Modal class to contain information regarding userId and PostId related with the likes made on a Post.
 */
@Embeddable
@Table(name = "likes")
@Getter
@Setter
@NoArgsConstructor
public class PostLikeIdentity implements Serializable {
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Post post;
}

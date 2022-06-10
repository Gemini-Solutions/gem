package com.gemini.gembook.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Modal class to contain information regarding a File related with posts
 */
@Entity
@Table(name = "files")
@Getter
@Setter
@NoArgsConstructor
public class GemFiles {

    @Id
    @Column(name = "file_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int photoId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    public GemFiles(Post post, String fileName, String fileType) {
        this.post = post;
        this.fileName = fileName;
        this.fileType = fileType;
    }
}
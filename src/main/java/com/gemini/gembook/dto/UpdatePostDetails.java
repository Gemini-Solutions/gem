package com.gemini.gembook.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//import org.springframework.core.io.Resource;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePostDetails {
    private String postContent;
    private int postId;
   // private Resource resource;
}

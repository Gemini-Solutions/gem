package com.gemini.gembook.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDetails {
	private String postContent;
	private String userId;
	private int postTypeId;
}

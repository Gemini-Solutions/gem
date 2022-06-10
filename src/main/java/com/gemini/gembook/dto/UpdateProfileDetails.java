package com.gemini.gembook.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileDetails {

    private String skills;
    private String trainingsDone;
    private String achievements;
    private String gemTalkDelivered;
    private String hobbies;

}


package com.gemini.gembook.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(indexName = "gemini_employee",shards = 6)
public class EmpElasticModel {


    private int id;
    private String name;
    private String skills;
    private String achievements;

    private String teamName;

    private String designation;

    private String trainingsDone;

    private String hobbies;

    private String gemTalkDelivered;

    public EmpElasticModel(String name, String skills, String achievements) {
        this.name = name;
        this.skills = skills;
        this.achievements = achievements;
    }

}
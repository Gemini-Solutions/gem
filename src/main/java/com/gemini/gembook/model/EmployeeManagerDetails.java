package com.gemini.gembook.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeManagerDetails {
    String name;
    String email;
    String designation;
    String imageUrl;
    String location;
}

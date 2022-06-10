package com.gemini.gembook.model;

import lombok.Getter;

@Getter
public class MISEmpDetails {
    // Param Name Starting with Upper Case is as per the JSON we are getting from MIS API.
    // No need to change it to mixed case.
    private String EmployeeName;
    private String EmployeeCode;
    private String Email;
    private String Designation;
    private String DepartmentName;
    private String MobileNumber;
    private String ExtNo;
    private String Location;
    private String WsNo;
    private String Team;
    private String ReportingManager;
    private String ImagePath;
    private String ManagerEmail;
    private String ManagerCode;
    private String TerminateDate;
    private String DOB;
    private String JoiningDate;
    private String IsFresher;
    private String CandidateUId;
    private String EmployeeUId;

}

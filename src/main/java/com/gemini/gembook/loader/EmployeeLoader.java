package com.gemini.gembook.loader;

import com.gemini.gembook.model.EmployeeProfile;
import com.gemini.gembook.model.User;
import com.gemini.gembook.repository.EmployeeProfileRepo;
import com.gemini.gembook.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Set;

/**
 * This class fetches data from user table and store that data in employee profile table
 */

@Component
public class EmployeeLoader {

    @Autowired
    EmployeeProfileRepo employeeProfileRepo;

    @Autowired
    UsersRepository usersRepository;

    //@PostConstruct
    public void Loader(){

        Set<String> emails=employeeProfileRepo.getAllEmails();
        List<User> userList= usersRepository.findAll();
        for(User user:userList){
            if(!emails.contains(user.getUserId())){
                //LocalDate localDate = LocalDate.of(2020, 5, 01);
                LocalDate localDate = user.getJoiningDate();
                LocalDate date = LocalDate.now();
                LocalDate dob = user.getDob();
                String strDob = String.valueOf(dob);
                Period age = Period.between(localDate,date);
                int years = age.getYears();
                int months = age.getMonths();
                int duration=(years*12)+months;
                EmployeeProfile profile=new EmployeeProfile(
                        user.getName(), user.getUserId(), localDate, user.getDesignation(),
                        user.getContact(), user.getTeam(), user.getReportingManagerName(),
                        strDob, duration);

                employeeProfileRepo.save(profile);

            }
        }
    }
}

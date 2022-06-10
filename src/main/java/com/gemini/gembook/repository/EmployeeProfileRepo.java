package com.gemini.gembook.repository;

import com.gemini.gembook.model.EmployeeProfile;
import com.gemini.gembook.projections.EmployeeProfileProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface EmployeeProfileRepo extends JpaRepository<EmployeeProfile, Integer> {

    @Query(
            value="select name from employee_profile where manager=?1"
            , nativeQuery = true
    )
    List<String> getAllReportees(String manager);

    @Query(
            value = "select e.email as email,e.name as name ,e.designation as designation " +
                    "from employee_profile e where e.name=(select manager from employee_profile where emp_id=?1) limit 1",
            nativeQuery = true
    )
    EmployeeProfileProjection getManagerByEmpId(int empId);



    @Query(
            value ="select emp_id from employee_profile " +
                    "where email=?1",
            nativeQuery = true
    )
    Integer EmpIdByEmail(String email);

    @Query(
            value = "select email from employee_profile",
            nativeQuery = true
    )
    Set<String> getAllEmails();



}

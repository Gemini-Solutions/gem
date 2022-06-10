package com.gemini.gembook.repository;

import com.gemini.gembook.model.Certificate;
import com.gemini.gembook.model.Projects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectsRepository extends JpaRepository<Projects,Integer> {

    @Query(
            value="select * from project where emp_id = ?1"
            , nativeQuery = true
    )
    public List<Projects> getProjectsByEmpId(int empId);

}

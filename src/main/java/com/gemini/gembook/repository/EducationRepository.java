package com.gemini.gembook.repository;

import com.gemini.gembook.model.Education;
import com.gemini.gembook.model.EducationId;
import com.gemini.gembook.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationRepository extends JpaRepository<Education, EducationId> {

    @Query(
            value="select * from education_details  where employee = ?1"
            , nativeQuery = true
    )
    public List<Education> getEducationByEmpId(int empId);
}

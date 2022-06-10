package com.gemini.gembook.repository;

import com.gemini.gembook.model.Education;
import com.gemini.gembook.model.ProfileLinks;
import com.gemini.gembook.model.ProfileLinksId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileLinksRepo extends JpaRepository<ProfileLinks, ProfileLinksId> {

    @Query(
            value="select * from profile_links where employee_id = ?1"
            , nativeQuery = true
    )
    public List<ProfileLinks> getLinksByEmpId(int empId);

}

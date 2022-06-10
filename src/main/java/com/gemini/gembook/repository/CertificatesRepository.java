package com.gemini.gembook.repository;

import com.gemini.gembook.model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificatesRepository extends JpaRepository<Certificate,Integer> {
    @Query(
            value="select * from certificate where emp_id = ?1"
            , nativeQuery = true
    )
    public List<Certificate> getCertificateByEmpId(int empId);


}

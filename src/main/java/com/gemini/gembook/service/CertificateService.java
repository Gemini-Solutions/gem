package com.gemini.gembook.service;

import com.gemini.gembook.controller.BaseResponse;
import com.gemini.gembook.model.Certificate;
import com.gemini.gembook.repository.CertificatesRepository;
import com.gemini.gembook.repository.EmployeeProfileRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificateService {

    @Autowired
    CertificatesRepository certificatesRepository;

    @Autowired
    EmployeeProfileRepo employeeProfileRepo;

    private final Logger logger = LoggerFactory.getLogger(CertificateService.class);


    public BaseResponse addCertificates(int employeeId, Certificate certificates){
        try {
            return employeeProfileRepo.findById(employeeId).map(employeeProfile -> {
                certificates.setEmployee(employeeProfile);
                certificatesRepository.save(certificates);
                return new BaseResponse("details added", HttpStatus.CREATED, certificates);
            }).orElse(new BaseResponse("id is wrong", HttpStatus.BAD_REQUEST, null));
        } catch (Exception e) {
            logger.error("Error while adding certificate details : {}", e.getMessage());
            return new BaseResponse("exception", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    public List<Certificate> getCertificatesByEmpId(int employeeId) {

        List<Certificate> certificates = null;
        try {
            certificates = certificatesRepository.getCertificateByEmpId(employeeId);
        } catch (Exception e) {
            logger.error("getCertificateByEmpId() throws an exception: {}", e.getMessage());
        }
        return certificates;

    }

    public Certificate updateCertificateDetails( int certificateId, Certificate certificates) {
        try {

            Certificate updatedCertificate = certificatesRepository.findById(certificateId).orElse(null);
            if(certificates.getName()!=null && !certificates.getName().isEmpty())
                updatedCertificate.setName(certificates.getName());
            if(certificates.getIssueDate()!=null)
                updatedCertificate.setIssueDate(certificates.getIssueDate());
            if(certificates.getProvider()!=null && !certificates.getProvider().isEmpty())
                updatedCertificate.setProvider(certificates.getProvider());
            return certificatesRepository.save(updatedCertificate);

        } catch (Exception e) {
            logger.error("exception in updateCertificateDetails() : {}", e.getMessage());
        }
        return null;
    }

    public Certificate getCertificateById(int certificateId) {
        Certificate certificate=null;
        try {
            certificate = certificatesRepository.findById(certificateId).orElse(null);
        } catch (Exception e) {
            logger.error("Exception in getCertificateById(): {}", e.getMessage());
        }
        return certificate;
    }

    public boolean deleteCertificate(int certificateId) {
        try{
        Certificate certificate= getCertificateById(certificateId);
        certificatesRepository.delete(certificate);}
        catch (Exception e){
            logger.error("Exception occured in deleteCertificate() : {}",e.getMessage() );
        }

        return getCertificateById(certificateId)==null;
    }

    public List<Certificate> getAllCertificates() {
        List<Certificate> certificates = null;
        try{
            certificates=certificatesRepository.findAll();
        }catch (Exception e){
            logger.error("Exception in getAllCertificates() : {}",e.getMessage());
        }
        return certificates;
    }
}

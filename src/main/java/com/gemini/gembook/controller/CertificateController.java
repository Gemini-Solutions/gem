package com.gemini.gembook.controller;

import com.gemini.gembook.model.Certificate;
import com.gemini.gembook.service.CertificateService;
import com.gemini.gembook.service.EmployeeProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/certificates")
public class CertificateController {

    @Autowired
    CertificateService certificateService;

    @Autowired
    EmployeeProfileService employeeProfileService;

    private final Logger logger = LoggerFactory.getLogger(CertificateController.class);


    @PostMapping
    public BaseResponse addCertificates(@RequestParam(value = "employeeId") int employeeId,
                                        @Valid@RequestBody Certificate certificates){

        return certificateService.addCertificates(employeeId,certificates);
    }

    @GetMapping(value = "/certificateByEmployeeId")
    public BaseResponse getCertificatesByEmpId(@RequestParam(value="employeeId") int employeeId){
        if(employeeProfileService.getProfileById(employeeId)== null){
            return new BaseResponse("EmployeeId not exist",HttpStatus.BAD_REQUEST,null);
        }
        List<Certificate> certificates=certificateService.getCertificatesByEmpId(employeeId);
        if(certificates == null){
            logger.error("unable to fetch certificate details");
            return new BaseResponse("Error occured",HttpStatus.INTERNAL_SERVER_ERROR,null);
        }
        if(certificates.isEmpty()){
            return new BaseResponse("No Details found",HttpStatus.OK,certificates);
        }
        logger.info("Certificate details fetched");
        return new BaseResponse("Details fetched successfully",HttpStatus.OK,certificates);

    }

    @GetMapping
    public BaseResponse GetCertificateByCertificateId(@RequestParam(value = "certificateId")int certificateId){
        Certificate certificate = certificateService.getCertificateById(certificateId);
        if(certificate== null){
            logger.warn("CertificateId not exist");
            return new BaseResponse("CertificateId not exist",HttpStatus.BAD_REQUEST,null);
        }
        return new BaseResponse("Retrieved successfully",HttpStatus.OK,certificate);


    }

    @GetMapping("/allCertificateDetails")
    public BaseResponse getAllCertificateDetails(){
        List<Certificate> certificates=certificateService.getAllCertificates();
        if(certificates == null){
            return new BaseResponse("Unable to fetch certificates",HttpStatus.INTERNAL_SERVER_ERROR,null);

        }
        if(certificates.isEmpty()){
            logger.info("No certificates found");
            return new BaseResponse("No Certificates Details Found",HttpStatus.OK,certificates);

        }
        logger.info("Certificates fetched");
        return new BaseResponse("Certificates fetched",HttpStatus.OK,certificates);
    }

    @PutMapping
    public BaseResponse updateCertificate(//@RequestParam(value = "employeeId") int employeeId,
                                      @RequestParam(value = "certificateId") int certificateId,
                                      @Valid @RequestBody Certificate certificates){
        if(certificateService.getCertificateById(certificateId)== null){
            return new BaseResponse("CertificateId not exist",HttpStatus.BAD_REQUEST,null);
        }
        Certificate updatedCertificate= certificateService.updateCertificateDetails(certificateId,
                certificates);
        if(updatedCertificate == null){
            return new BaseResponse("Unable to update",HttpStatus.INTERNAL_SERVER_ERROR,null);
        }
        logger.info("Certificate details Updated");
        return new BaseResponse("Details Updated",HttpStatus.OK,updatedCertificate);



    }

    @DeleteMapping
    public BaseResponse deleteCertificate(@RequestParam(value = "certificateId") int certificateId){

        if(certificateService.getCertificateById(certificateId)== null){
            logger.warn("Id does not exist");
            return new BaseResponse("Id does not exist",HttpStatus.BAD_REQUEST,null);
        }

        if(certificateService.deleteCertificate(certificateId)){
            logger.info("Details Deleted successfully");
            return new BaseResponse("success",HttpStatus.OK,true);

        }
        logger.error("Unable to delete");
        return new BaseResponse("error occured",HttpStatus.INTERNAL_SERVER_ERROR,false);
    }
}

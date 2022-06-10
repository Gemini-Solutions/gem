package com.gemini.gembook.controller;

import com.gemini.gembook.dto.ApiResponse;
import com.gemini.gembook.dto.EmployeeDetails;
import com.gemini.gembook.elasticService.EmpSearchService;
import com.gemini.gembook.model.EmpElasticModel;
import com.gemini.gembook.model.User;
import com.gemini.gembook.repository.EmployeeProfileRepo;
import com.gemini.gembook.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/search")
public class ElasticEmpController {
    @Autowired
    EmpSearchService empSearchService;
    @Autowired
    EmployeeProfileRepo employeeProfileRepo;
    @Autowired
    UsersRepository usersRepository;
    private final Logger logger = LoggerFactory.getLogger(ElasticEmpController.class);

    @GetMapping("/EmpProfile/{input}")
    public ApiResponse searchEmployee(@PathVariable String input){
        List<EmployeeDetails> employeeProfiles = new ArrayList<>();
        SearchHits<EmpElasticModel> elasticModelList= empSearchService.multiMatchQuery(input);

//        if(elasticModelList.isEmpty()){
//            return new ApiResponse(HttpStatus.OK,"No matching results found");
//        }
        long length = elasticModelList.getTotalHits();
        for (int i = 0; i < length; i++) {
            EmpElasticModel model = elasticModelList.getSearchHit(i).getContent();
            EmployeeDetails employee = new EmployeeDetails(employeeProfileRepo.findById(model.getId()));
            Optional<User> user = usersRepository.findById(employee.getEmail());
            user.ifPresent(value -> employee.setImageUrl(value.getPhotoUrl()));
            employeeProfiles.add(employee);
        }
//        if(employeeProfiles.isEmpty()){
//            return new ApiResponse(HttpStatus.OK, Collections.<String>emptyList());
//        }
        return new ApiResponse(HttpStatus.OK,employeeProfiles);
    }
}


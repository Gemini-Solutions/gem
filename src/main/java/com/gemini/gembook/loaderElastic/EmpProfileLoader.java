package com.gemini.gembook.loaderElastic;


import com.gemini.gembook.elasticRepo.ProfileRepository;
import com.gemini.gembook.model.EmpElasticModel;
import com.gemini.gembook.model.EmployeeProfile;
import com.gemini.gembook.repository.EmployeeProfileRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@EnableScheduling
public class EmpProfileLoader {

    private final Logger logger = LoggerFactory.getLogger(EmpProfileLoader.class);

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    EmployeeProfileRepo employeeProfileRepo;

    //@PostConstruct
    @Scheduled(cron = "0 0 11 * * MON-FRI")
    public void indexData() {
        try {
            System.out.println("Loading Data Employee..........");
            List<EmployeeProfile> profiles = employeeProfileRepo.findAll();
            //List<EmpElasticModel> indexedEmpProfile = (List<EmpElasticModel>) profileRepository.findAll();
            //Set<Integer> empIds = indexedEmpProfile.stream().map(EmpElasticModel::getId).collect(Collectors.toSet());
            for (EmployeeProfile profile : profiles) {
                //if (!empIds.contains(profile.getId())) {
                    EmpElasticModel model = new EmpElasticModel();
                    model.setId(profile.getId());
                    model.setName(profile.getName());
                    model.setSkills(profile.getSkills());
                    model.setAchievements(profile.getAchievements());
                    model.setTeamName(profile.getTeamName());
                    model.setDesignation(profile.getDesignation());
                    model.setTrainingsDone(profile.getTrainingsDone());
                    model.setHobbies(profile.getHobbies());
                    model.setGemTalkDelivered(profile.getGemTalkDelivered());
                    profileRepository.save(model);

                //}
            }
            System.out.println("Employee Data indexed successfully");
        } catch (Exception e) {
            logger.error("Exception while indexing Employee data in elastic", e);

        }

    }
}




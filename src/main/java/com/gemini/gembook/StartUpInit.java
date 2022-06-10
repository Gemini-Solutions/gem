package com.gemini.gembook;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gemini.gembook.Config.MisData;
import com.gemini.gembook.model.MISEmpDetails;
import com.gemini.gembook.model.User;
import com.gemini.gembook.repository.EmployeeProfileRepo;
import com.gemini.gembook.repository.UsersRepository;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class to update User login details from MIS database.
 */
@Slf4j
@Component
@EnableScheduling
public class StartUpInit {
    private final Logger logger = LoggerFactory.getLogger(StartUpInit.class);
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private EmployeeProfileRepo employeeProfileRepo;
    @Autowired
    private MisData misData;
    @Value("${MIS_API}")
    private String misApi;

    /**
     * Init method defining a CRON job which fetches user details from MIS database and store them in local database.
     */
    @Scheduled(cron = "${CRON_EXP}")
    public void init() {
        logger.info("Current time is :: " + Calendar.getInstance().getTime());
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response
                    = restTemplate.exchange(misApi, HttpMethod.GET, entity, String.class);
            log.info("response status from MIS api : {}", response.getStatusCode());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode respObj = mapper.readTree(response.getBody());
            List<MISEmpDetails> origEmpList = new Gson().fromJson(respObj.get("Result").toString(),
                    new TypeToken<List<MISEmpDetails>>() { }.getType());

            List<MISEmpDetails> empListNN =
                    origEmpList.stream().filter(e -> e.getEmail() != null).collect(Collectors.toList());

            log.info("fetched employee details from MIS. Total count : {}", empListNN.size());

            // fetching all email ids.
            Set<String> emailSet = usersRepository.getAllEmailIds();
            List<User> userList = usersRepository.findAll();

            empListNN.stream().forEach(e -> {

                String email = e.getEmail().toLowerCase(Locale.ROOT);
                String name = e.getEmployeeName();
                String photoUrl = e.getImagePath();
                String teamName = e.getTeam();
                String designation = e.getDesignation();
                String contact = e.getMobileNumber();
                String reportingManager = e.getReportingManager();
                String managerEmail = e.getManagerEmail();
                String strJoiningDate = e.getJoiningDate();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                LocalDate joiningDate = LocalDate.parse(strJoiningDate, formatter);
                String empDOB = e.getDOB();
                DateTimeFormatter format = DateTimeFormatter.ofPattern("M/dd/yyyy");
                LocalDate dob = LocalDate.parse(empDOB, format);
                if (!emailSet.contains(email)) {
                    User user =
                            new User(email, name, photoUrl, teamName, designation, contact, reportingManager,
                                    managerEmail, dob, joiningDate);
                    usersRepository.save(user);
                } else {
                    Optional<User> matchedUser = userList.stream().filter(u -> e.getEmail().equals(email)).findFirst();
                    User user = matchedUser.orElse(null);
                    //System.out.println(user.getName());
                    if (user != null) {
                        if (!user.getUserId().equals(email)) {
                            user.setUserId(email);
                        }
                        if (!user.getPhotoUrl().equals(photoUrl)) {
                            user.setPhotoUrl(photoUrl);
                        }
                        if (!user.getDesignation().equals(designation)) {
                            user.setDesignation(designation);
                        }
                        if (!user.getTeam().equals(teamName)) {
                            user.setTeam(teamName);
                        }
                        if (!user.getContact().equals(contact)) {
                            user.setContact(contact);
                        }
                        if (!user.getName().equals(name)) {
                            user.setName(name);
                        }

                        if (!user.getReportingManagerEmail().equals(managerEmail)) {
                            user.setReportingManagerEmail(managerEmail);
                            user.setReportingManagerName(reportingManager);
                        }
                        String strDOB = String.valueOf(user.getDob());
                        String misDOB = String.valueOf(dob);
                        if (!strDOB.equalsIgnoreCase(misDOB)) {
                            user.setDob(dob);
                        }
                        String strDOJ = String.valueOf(user.getJoiningDate());
                        String misDOJ = String.valueOf(joiningDate);
                        if (!strDOJ.equalsIgnoreCase(misDOJ)) {
                            user.setJoiningDate(joiningDate);
                        }
                        usersRepository.save(user);
                    }
                }
            });
        } catch (RuntimeException e) {
            logger.error("Encountered runtime Exception in init()");
            throw e;
        } catch (Exception e) {
            logger.error("Exception in init() : {}", e.getMessage());
        }
        logger.info("Init completed !!");
    }
}

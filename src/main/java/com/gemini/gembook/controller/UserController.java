package com.gemini.gembook.controller;

import com.gemini.gembook.dto.ApiResponse;
import com.gemini.gembook.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

/**
 * Controller class for REST API to manage User details.
 */
@RestController
@RequestMapping(value = "/api/users")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UsersService usersService;

    /**
     * @return list of all users.
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getAllUsers() {
        ApiResponse response = usersService.getAllUsers();
        logger.info("ResponseMessage for getAllUsers() is : {}",response.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * Get User details via given UserId
     *
     * @param userId
     * @return User details if existed
     */
    @GetMapping(value = "/user")
    public ResponseEntity<ApiResponse> getUserById(@RequestParam(value = "userId") String userId) {
        ApiResponse response = usersService.findByUserId(userId.toLowerCase(Locale.ROOT));
        logger.info("ResponseMessage for getUserById() is : {}",response.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

package com.gemini.gembook.service;


import com.gemini.gembook.dto.ApiResponse;
import com.gemini.gembook.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Service class to serve requests managing user details on the server.
 */
@Service
public class UsersService {
    private final UsersRepository usersRepository;

    private final Logger logger = LoggerFactory.getLogger(UsersService.class);

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    /**
     * @return ApiResponse containing list of all users
     */
    public ApiResponse getAllUsers() {
        try {
            return new ApiResponse(HttpStatus.OK, usersRepository.findAll());
        } catch (Exception e) {
            logger.error("Exception while fetching all users : {}", e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve users");
        }
    }

    /**
     * Retrieves an user details via user name.
     *
     * @param userId User email is the userId
     * @return ApiResponse
     */
    public ApiResponse findByUserId(String userId) {
        try {
            return usersRepository.findById(userId).map(user ->
                    new ApiResponse(HttpStatus.OK, user)
            ).orElse(new ApiResponse(HttpStatus.NOT_ACCEPTABLE, "User doesn't exists"));
        } catch (Exception e) {
            logger.error("Exception while finding user : {}", e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch user : " + userId);
        }
    }
}

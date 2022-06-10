package com.gemini.gembook.repository;

import com.gemini.gembook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/*
 * Repository interface providing database access functionality to manage users' details in database.
 */
@Repository
public interface UsersRepository extends JpaRepository<User, String> {

    /*
     * Retrieves user ids for all users from user table.
     */
    @Query(nativeQuery = true, value = "SELECT user_id FROM gem_user")
    Set<String> getAllEmailIds();

    @Override
    @Query(nativeQuery = true, value = "SELECT * FROM gem_user")
    List<User> findAll();
}
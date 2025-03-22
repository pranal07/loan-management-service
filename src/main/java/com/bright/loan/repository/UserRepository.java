/**
 * @author Pranal
 */
package com.bright.loan.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bright.loan.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUniqueUserId(String uniqueUserId);
}
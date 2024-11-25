package org.startup.quaero.database.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.startup.quaero.database.entities.User;
import org.startup.quaero.enums.Role;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    @Query("SELECT u.role FROM User u WHERE u.id = :id")
    Role findRoleById(@Param("id") Long id);

    Optional<User> findByEmail(String email);

}

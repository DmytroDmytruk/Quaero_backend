package org.startup.quaero.database.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.startup.quaero.database.entities.EmploymentType;

import java.util.Optional;

public interface EmploymentTypeRepo extends JpaRepository<EmploymentType, Long> {
    Optional<EmploymentType> findByType(String type);
}

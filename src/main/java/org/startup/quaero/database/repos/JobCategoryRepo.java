package org.startup.quaero.database.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.startup.quaero.database.entities.JobCategory;

import java.util.Optional;

public interface JobCategoryRepo extends JpaRepository<JobCategory, Long> {
    Optional<JobCategory> findByName(String name);
}
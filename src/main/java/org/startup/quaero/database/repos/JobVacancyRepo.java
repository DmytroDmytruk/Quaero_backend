package org.startup.quaero.database.repos;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.startup.quaero.database.entities.JobVacancy;

import java.util.List;

@Repository
public interface JobVacancyRepo extends JpaRepository<JobVacancy, Long> {

    @Query("SELECT j FROM JobVacancy j " +
            "WHERE j.category.id = :categoryId " +
            "AND j.yearsOfExperience = :yearsOfExperience " +
            "AND j.id <> :vacancyId " +
            "ORDER BY FUNCTION('RAND')")
    List<JobVacancy> findSimilarVacancies(@Param("categoryId") Long categoryId,
                                          @Param("yearsOfExperience") Integer yearsOfExperience,
                                          @Param("vacancyId") Long vacancyId,
                                          Pageable pageable);
}

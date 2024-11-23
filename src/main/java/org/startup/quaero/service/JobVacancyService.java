package org.startup.quaero.service;

import org.startup.quaero.dto.vacancy.JobVacancyDto;

import java.util.List;

public interface JobVacancyService {
    JobVacancyDto getVacancyById(long vacancyId);
    List<JobVacancyDto> getSimilarVacancies(long vacancyId);
}

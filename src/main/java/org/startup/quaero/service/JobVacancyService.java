package org.startup.quaero.service;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.startup.quaero.dto.vacancy.JobVacancyDto;
import org.startup.quaero.dto.vacancy.JobVacancyFilterDto;

import java.util.List;

public interface JobVacancyService {
    JobVacancyDto getVacancyById(long vacancyId);
    List<JobVacancyDto> getSimilarVacancies(long vacancyId, int size);
    Page<JobVacancyDto> sortAndFilterVacancies(JobVacancyFilterDto filterDto, int page, int size);
    Page<JobVacancyDto> getVacanciesByHr(long hrId, int page, int size);
}

package org.startup.quaero.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.startup.quaero.database.entities.JobVacancy;
import org.startup.quaero.database.repos.JobVacancyRepo;
import org.startup.quaero.dto.vacancy.JobLanguageDto;
import org.startup.quaero.dto.vacancy.JobVacancyDto;
import org.startup.quaero.service.JobVacancyService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobVacancyServiceImpl implements JobVacancyService {

    private final JobVacancyRepo jobVacancyRepo;
    private final ModelMapper modelMapper;

    public JobVacancyDto getVacancyById(long vacancyId) {
        JobVacancy jobVacancy = jobVacancyRepo.findById(vacancyId).orElseThrow(() -> new EntityNotFoundException("Vacancy with id : " + vacancyId + " + not found"));
        JobVacancyDto jobVacancyDto = modelMapper.map(jobVacancy, JobVacancyDto.class);
        List<JobLanguageDto> jobLanguageDTOs = jobVacancy.getLanguages().stream()
                .map(language -> modelMapper.map(language, JobLanguageDto.class))
                .collect(Collectors.toList());
        jobVacancyDto.setLanguages(jobLanguageDTOs);
        return jobVacancyDto;
    }

    @Override
    public List<JobVacancyDto> getSimilarVacancies(long vacancyId) {
        JobVacancy originalVacancy = jobVacancyRepo.findById(vacancyId)
                .orElseThrow(() -> new IllegalArgumentException("Vacancy not found with ID: " + vacancyId));
        Pageable pageable = PageRequest.of(0, 20);
        List<JobVacancy> similarVacancies = jobVacancyRepo.findSimilarVacancies(
                originalVacancy.getCategory().getId(),
                originalVacancy.getYearsOfExperience(),
                vacancyId,
                pageable
        );

        return similarVacancies.stream()
                .map(vacancy -> {
                    JobVacancyDto dto = modelMapper.map(vacancy, JobVacancyDto.class);
                    List<JobLanguageDto> languages = vacancy.getLanguages().stream()
                            .map(language -> modelMapper.map(language, JobLanguageDto.class))
                            .collect(Collectors.toList());
                    dto.setLanguages(languages);
                    return dto;
                })
                .collect(Collectors.toList());
    }
}

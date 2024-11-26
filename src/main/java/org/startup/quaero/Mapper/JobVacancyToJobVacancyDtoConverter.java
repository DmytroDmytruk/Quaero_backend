package org.startup.quaero.Mapper;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.startup.quaero.database.entities.JobVacancy;
import org.startup.quaero.dto.vacancy.JobLanguageDto;
import org.startup.quaero.dto.vacancy.JobVacancyDto;

import java.util.stream.Collectors;

public class JobVacancyToJobVacancyDtoConverter implements Converter<JobVacancy, JobVacancyDto> {
    @Override
    public JobVacancyDto convert(MappingContext<JobVacancy, JobVacancyDto> mappingContext) {
        JobVacancy source = mappingContext.getSource();

        return JobVacancyDto.builder()
                .id(source.getId())
                .positionTitle(source.getPositionTitle())
                .salary(source.getSalary())
                .description(source.getDescription())
                .companyName(source.getCompanyName())
                .datePosted(source.getDatePosted())
                .categoryName(source.getCategory() != null ? source.getCategory().getName() : null)
                .employmentTypeName(source.getEmploymentType() != null ? source.getEmploymentType().getType() : null)
                .yearsOfExperience(source.getYearsOfExperience())
                .languages(source.getLanguages() != null
                        ? source.getLanguages().stream()
                        .map(lang -> JobLanguageDto.builder()
                                .languageName(lang.getLanguageName())
                                .languageLevel(lang.getLanguageLevel())
                                .build())
                        .collect(Collectors.toList())
                        : null)
                .build();
    }
}

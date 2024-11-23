package org.startup.quaero.dto.vacancy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobVacancyDto{
    private Long id;
    private String positionTitle;
    private Double salary;
    private String description;
    private String companyName;
    private LocalDateTime datePosted;
    private String categoryName;
    private Integer yearsOfExperience;
    private String employmentTypeName;
    private List<JobLanguageDto> languages;
}

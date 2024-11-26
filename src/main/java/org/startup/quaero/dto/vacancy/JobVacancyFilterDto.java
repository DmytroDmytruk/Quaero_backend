package org.startup.quaero.dto.vacancy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobVacancyFilterDto {
    private String positionTitle = "";
    private String companyName = "";
    private String categoryName = "";
    private String employmentType = "";
    private String languageName = "";
    private Double minSalary = 0.0;
    private Double maxSalary = Double.MAX_VALUE;
    private Integer minYearsOfExperience = 0;
    private Integer maxYearsOfExperience = Integer.MAX_VALUE;
    private String sortBy = "datePosted";
    private String sortDirection = "desc";
}


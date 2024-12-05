package org.startup.quaero.dto.vacancy;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.startup.quaero.dto.SortDirection;

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

    @NotBlank(message = "SortBy не має бути пусте")

    @Pattern(regexp = "positionTitle|salary|datePosted|yearsOfExperience|companyName|category.name|employmentType.type|postedBy.name", message = "Неправильне значення SortBy кретине, має бути positionTitle|salary|datePosted|yearsOfExperience|companyName|category.name|employmentType.type|postedBy.name")
    private String sortBy;

    @NotBlank(message = "SortDirection не має бути пусте")
    @Pattern(regexp = "asc|desc", message = "Неправильне значення SortDirection кретине, має бути asc|desc")
    private String sortDirection;
}


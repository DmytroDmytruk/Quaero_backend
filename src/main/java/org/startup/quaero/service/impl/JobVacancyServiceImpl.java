package org.startup.quaero.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.startup.quaero.database.entities.EmploymentType;
import org.startup.quaero.database.entities.JobCategory;
import org.startup.quaero.database.entities.JobLanguage;
import org.startup.quaero.database.entities.JobVacancy;
import org.startup.quaero.database.entities.User;
import org.startup.quaero.database.repos.EmploymentTypeRepo;
import org.startup.quaero.database.repos.JobCategoryRepo;
import org.startup.quaero.database.repos.JobVacancyRepo;
import org.startup.quaero.database.repos.UserRepo;
import org.startup.quaero.dto.vacancy.JobLanguageDto;
import org.startup.quaero.dto.vacancy.JobVacancyDto;
import org.startup.quaero.dto.vacancy.JobVacancyFilterDto;
import org.startup.quaero.dto.vacancy.SetJobVacancyDto;
import org.startup.quaero.service.JobVacancyService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobVacancyServiceImpl implements JobVacancyService {

    private final JobVacancyRepo jobVacancyRepo;
    private final UserRepo userRepo;
    private final JobCategoryRepo jobCategoryRepo;
    private final EmploymentTypeRepo employmentTypeRepo;

    private final ModelMapper modelMapper;

    @PersistenceContext
    private EntityManager entityManager;


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
    public List<JobVacancyDto> getSimilarVacancies(long vacancyId, int size) {
        JobVacancy originalVacancy = jobVacancyRepo.findById(vacancyId)
                .orElseThrow(() -> new IllegalArgumentException("Vacancy not found with ID: " + vacancyId));
        Pageable pageable = PageRequest.of(0, size);
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

    @Override
    public Page<JobVacancyDto> sortAndFilterVacancies(JobVacancyFilterDto filterDto, int page, int size) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<JobVacancy> cq = cb.createQuery(JobVacancy.class);
        Root<JobVacancy> root = cq.from(JobVacancy.class);

        Join<JobVacancy, JobCategory> categoryJoin = root.join("category", JoinType.LEFT);
        Join<JobVacancy, EmploymentType> employmentTypeJoin = root.join("employmentType", JoinType.LEFT);
        Join<JobVacancy, JobLanguage> languageJoin = root.join("languages", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.like(cb.lower(root.get("positionTitle")), "%" + filterDto.getPositionTitle().toLowerCase() + "%"));
        predicates.add(cb.like(cb.lower(root.get("companyName")), "%" + filterDto.getCompanyName().toLowerCase() + "%"));
        predicates.add(cb.like(cb.lower(categoryJoin.get("name")), "%" + filterDto.getCategoryName().toLowerCase() + "%"));
        predicates.add(cb.like(cb.lower(employmentTypeJoin.get("type")), "%" + filterDto.getEmploymentType().toLowerCase() + "%"));
        predicates.add(cb.like(cb.lower(languageJoin.get("languageName")), "%" + filterDto.getLanguageName().toLowerCase() + "%"));
        predicates.add(cb.ge(root.get("salary"), filterDto.getMinSalary()));
        predicates.add(cb.le(root.get("salary"), filterDto.getMaxSalary()));
        predicates.add(cb.ge(root.get("yearsOfExperience"), filterDto.getMinYearsOfExperience()));
        predicates.add(cb.le(root.get("yearsOfExperience"), filterDto.getMaxYearsOfExperience()));

        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        if (filterDto.getSortDirection().equalsIgnoreCase("desc")) {
            cq.orderBy(cb.desc(root.get(filterDto.getSortBy())));
        } else {
            cq.orderBy(cb.asc(root.get(filterDto.getSortBy())));
        }

        TypedQuery<JobVacancy> query = entityManager.createQuery(cq);
        query.setFirstResult(page * size);
        query.setMaxResults(size);

        List<JobVacancy> jobVacancies = query.getResultList();
        List<JobVacancyDto> jobVacancyDtos = jobVacancies.stream()
                .map(jobVacancy -> modelMapper.map(jobVacancy, JobVacancyDto.class))
                .toList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<JobVacancy> countRoot = countQuery.from(JobVacancy.class);

        Join<JobVacancy, JobCategory> countCategoryJoin = countRoot.join("category", JoinType.LEFT);
        Join<JobVacancy, EmploymentType> countEmploymentTypeJoin = countRoot.join("employmentType", JoinType.LEFT);
        Join<JobVacancy, JobLanguage> countLanguageJoin = countRoot.join("languages", JoinType.LEFT);

        List<Predicate> countPredicates = new ArrayList<>();
        countPredicates.add(cb.like(cb.lower(countRoot.get("positionTitle")), "%" + filterDto.getPositionTitle().toLowerCase() + "%"));
        countPredicates.add(cb.like(cb.lower(countRoot.get("companyName")), "%" + filterDto.getCompanyName().toLowerCase() + "%"));
        countPredicates.add(cb.like(cb.lower(countCategoryJoin.get("name")), "%" + filterDto.getCategoryName().toLowerCase() + "%"));
        countPredicates.add(cb.like(cb.lower(countEmploymentTypeJoin.get("type")), "%" + filterDto.getEmploymentType().toLowerCase() + "%"));
        countPredicates.add(cb.like(cb.lower(countLanguageJoin.get("languageName")), "%" + filterDto.getLanguageName().toLowerCase() + "%"));
        countPredicates.add(cb.ge(countRoot.get("salary"), filterDto.getMinSalary()));
        countPredicates.add(cb.le(countRoot.get("salary"), filterDto.getMaxSalary()));
        countPredicates.add(cb.ge(countRoot.get("yearsOfExperience"), filterDto.getMinYearsOfExperience()));
        countPredicates.add(cb.le(countRoot.get("yearsOfExperience"), filterDto.getMaxYearsOfExperience()));

        countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));
        countQuery.select(cb.count(countRoot));

        long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(jobVacancyDtos, PageRequest.of(page, size), total);
    }


    @Override
    public Page<JobVacancyDto> getVacanciesByHr(long hrId, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<JobVacancy> vacanciesPage = jobVacancyRepo.findByPostedById(hrId, pageable);
        return vacanciesPage.map(jobVacancy ->
                modelMapper.map(jobVacancy, JobVacancyDto.class)
        );
    }

    @Transactional
    public void createVacancy(long hrId, SetJobVacancyDto setJobVacancyDto) {
        User hr = userRepo.findById(hrId)
                .orElseThrow(() -> new IllegalArgumentException("HR with ID " + hrId + " not found"));

        JobCategory category = jobCategoryRepo.findByName(setJobVacancyDto.getCategoryName())
                .orElseGet(() -> {
                    JobCategory newCategory = JobCategory.builder()
                            .name(setJobVacancyDto.getCategoryName())
                            .build();
                    return jobCategoryRepo.save(newCategory);
                });

        EmploymentType employmentType = employmentTypeRepo.findByType(setJobVacancyDto.getEmploymentTypeName())
                .orElseGet(() -> {
                    EmploymentType newEmploymentType = EmploymentType.builder()
                            .type(setJobVacancyDto.getEmploymentTypeName())
                            .build();
                    return employmentTypeRepo.save(newEmploymentType);
                });

        JobVacancy jobVacancy = JobVacancy.builder()
                .positionTitle(setJobVacancyDto.getPositionTitle())
                .salary(setJobVacancyDto.getSalary())
                .description(setJobVacancyDto.getDescription())
                .companyName(setJobVacancyDto.getCompanyName())
                .postedBy(hr)
                .datePosted(LocalDateTime.now())
                .category(category)
                .yearsOfExperience(setJobVacancyDto.getYearsOfExperience())
                .employmentType(employmentType)
                .build();

        jobVacancy.setLanguages(mapLanguages(setJobVacancyDto.getLanguages(), jobVacancy));

        jobVacancyRepo.save(jobVacancy);
    }

    private List<JobLanguage> mapLanguages(List<SetJobVacancyDto.LanguageDto> languagesDto, JobVacancy jobVacancy) {
        return languagesDto.stream()
                .map(dto -> JobLanguage.builder()
                        .languageName(dto.getLanguageName())
                        .languageLevel(dto.getLanguageLevel())
                        .jobVacancy(jobVacancy)
                        .build())
                .toList();
    }
}

package org.startup.quaero.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.startup.quaero.dto.vacancy.JobVacancyDto;
import org.startup.quaero.dto.vacancy.JobVacancyFilterDto;
import org.startup.quaero.dto.vacancy.SetJobVacancyDto;
import org.startup.quaero.service.JobVacancyService;

import java.util.List;

@RestController("/jobVacancy")
public class JobVacancyController {

    @Autowired
    private JobVacancyService jobVacancyService;

    @Operation(summary = "Каталог вакансій відфільтрований і відсортований")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND")
    })
    @PostMapping("/getAllFilteredAndSorted")
    public ResponseEntity<Page<JobVacancyDto>> getAllFilteredAndSorted(
            @Valid @RequestBody JobVacancyFilterDto filterDto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Page<JobVacancyDto> vacancies = jobVacancyService.sortAndFilterVacancies(filterDto, page, size);
        return ResponseEntity.ok().body(vacancies);
    }

    @Operation(summary = "Інформація про ваканцію по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND")
    })
    @GetMapping("/getVacancy/{vacancyId}")
    public ResponseEntity<JobVacancyDto> getVacancy(@PathVariable long vacancyId) {
        return ResponseEntity.ok(jobVacancyService.getVacancyById(vacancyId));
    }

    @Operation(summary = "Дістати схожі вакансії")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND")
    })
    @GetMapping("/getSimilarVacanciesById")
    public ResponseEntity<List<JobVacancyDto>> getSimilarVacanciesById(@RequestParam long vacancyId, @RequestParam int size) {
        return ResponseEntity.ok(jobVacancyService.getSimilarVacancies(vacancyId, size));
    }

    @Operation(summary = "Дістати вакансії по hr")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND")
    })
    @GetMapping("/getVacanciesByHr/{hrId}")
    public ResponseEntity<Page<JobVacancyDto>> getVacanciesByHr(
            @PathVariable long hrId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(jobVacancyService.getVacanciesByHr(hrId, page, size));
    }

    @Operation(summary = "піпіпупу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND")
    })
    @PostMapping("/setVacancy{hrId}")
    public ResponseEntity<String> setVacancy(@PathVariable long hrId, @RequestBody SetJobVacancyDto setJobVacancyDto){
        jobVacancyService.createVacancy(hrId, setJobVacancyDto);
        return ResponseEntity.ok("Vacancy set successfully");
    }
}

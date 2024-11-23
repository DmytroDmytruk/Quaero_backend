package org.startup.quaero.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.startup.quaero.dto.vacancy.JobVacancyDto;
import org.startup.quaero.service.JobVacancyService;

import java.util.List;

@RestController("/jobVacancy")
public class JobVacancyController {

    @Autowired
    private JobVacancyService jobVacancyService;

    @GetMapping("/getVacancy/{vacancyId}")
    public ResponseEntity<JobVacancyDto> getVacancy(@PathVariable long vacancyId) {
        return ResponseEntity.ok(jobVacancyService.getVacancyById(vacancyId));
    }

    @GetMapping("/getSimilarVacanciesById")
    public ResponseEntity<List<JobVacancyDto>> getSimilarVacanciesById(@RequestParam long vacancyId) {
        return ResponseEntity.ok(jobVacancyService.getSimilarVacancies(vacancyId));
    }
}

package com.hipages.controller;

import com.hipages.model.ChangeJobStatusDto;
import com.hipages.domain.Job;
import com.hipages.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping("/{jobStatus}")
    public List<Job> getAllNewLeads(@PathVariable String jobStatus) {
        List<Job> jobs = jobService.getAllJobsByStatus(jobStatus);
        return jobs;
    }

    @PostMapping(path = "/change-status", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Job changeJobStatus(HttpServletRequest request, @RequestBody ChangeJobStatusDto changeJobStatusDto) {
        return jobService.changeJobStatus(changeJobStatusDto);
    }
}

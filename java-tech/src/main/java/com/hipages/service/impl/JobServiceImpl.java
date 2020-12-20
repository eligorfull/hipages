package com.hipages.service.impl;

import com.hipages.domain.Job;
import com.hipages.model.ChangeJobStatusDto;
import com.hipages.repository.JobRepository;
import com.hipages.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;

    @Override
    public List<Job> getAllJobsByStatus(String jobStatus) {
        return jobRepository.findAllByStatus(jobStatus);
    }

    @Override
    public Job changeJobStatus(ChangeJobStatusDto changeJobStatusDto) {
        Job job = jobRepository.findById(changeJobStatusDto.getId()).get();
        if (job != null) {
            job.setStatus(changeJobStatusDto.getNewStatus());
            jobRepository.saveAndFlush(job);
            return job;
        }
        return null;
    }
}

package com.hipages.service;

import com.hipages.model.ChangeJobStatusDto;
import com.hipages.domain.Job;

import java.util.List;
public interface JobService {

    List<Job> getAllJobsByStatus(String jobStatus);

    Job changeJobStatus(ChangeJobStatusDto changeJobStatusDto);
}

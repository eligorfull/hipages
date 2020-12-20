package com.hipages.repository;

import com.hipages.domain.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findAllByStatus(String status);
}

package com.hipages;

import com.hipages.domain.Category;
import com.hipages.domain.Job;
import com.hipages.domain.Suburb;
import com.hipages.model.ChangeJobStatusDto;
import com.hipages.repository.JobRepository;
import com.hipages.service.JobService;
import com.hipages.service.impl.JobServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JobServiceImplIntegrationTest {

    @TestConfiguration
    static class JobServiceImplTestContextConfiguration {

        @Bean
        public JobService jobService() {
            return new JobServiceImpl();
        }
    }
    @Autowired
    private JobService jobService;

    @MockBean
    private JobRepository jobRepository;

    private Job getAJob(String status) {
        Job job = new Job();
        job.setId(1L);
        job.setStatus(status);
        job.setContactName("Sergio");
        job.setContactPhone("0411223344");
        job.setContactEmail("sergio222@gmail.com");
        job.setDescription("Change a tab and replace a leaking pipe under the kitchen sink");
        job.setPrice(100);
        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());

        Suburb suburb = new Suburb();
        suburb.setId(2L);
        suburb.setName("Bondi");
        suburb.setPostcode("2047");
        job.setSuburb(suburb);

        Category category = new Category();
        category.setId(1L);
        category.setName("Plumbing");
        job.setCategory(category);
        return job;
    }

    @Before
    public void setUp() {
        Job job = getAJob("new");
        Mockito.when(jobRepository.findById(1L)).thenReturn(Optional.of(job));
    }

    @Before
    public void setUpAcceptedJobs() {
        String acceptedStatus = "accepted";
        List<Job> jobs = new ArrayList<>();
        jobs.add(getAJob(acceptedStatus));
        jobs.add(getAJob(acceptedStatus));
        jobs.add(getAJob(acceptedStatus));

        Mockito.when(jobRepository.findAllByStatus(acceptedStatus)).thenReturn(jobs);
    }

    @Test
    public void testGetJobsByAcceptedStatusShouldBeFound() {
        String acceptedStatus = "accepted";
        List<Job> foundJobs = jobService.getAllJobsByStatus(acceptedStatus);

        assertNotNull("It should find the jobs added before", foundJobs);
        assertEquals("It should have three jobs in accepted status", 3, foundJobs.size());

        assertEquals("The name should be store properly", "Sergio", foundJobs.get(0).getContactName());
        assertEquals("The description should be store properly", "Change a tab and replace a leaking pipe under the kitchen sink", foundJobs.get(1).getDescription());
    }

    @Test
    public void testJobStatusShouldBeChanged() {
        ChangeJobStatusDto changeJobStatusDto = new ChangeJobStatusDto();
        changeJobStatusDto.setId(1L);
        changeJobStatusDto.setNewStatus("accepted");
        Job foundJob = jobService.changeJobStatus(changeJobStatusDto);

        assertNotNull("The job should exists", foundJob);
        assertEquals("It should have the correct status", "accepted", foundJob.getStatus());
    }

}

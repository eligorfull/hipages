package com.hipages;

import com.hipages.domain.Category;
import com.hipages.domain.Job;
import com.hipages.domain.Suburb;
import com.hipages.repository.JobRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertTrue;

@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
@DataJpaTest
public class JobRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JobRepository jobRepository;

    private Job getAJob() {
        Job job = new Job();
        job.setStatus("new");
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

    @Test
    public void testFindJobsByStatus() {
        List<Job> jobsFoundBefore = jobRepository.findAllByStatus("new");
        int newJobsFound = 0;
        if (jobsFoundBefore != null) {
            newJobsFound = jobsFoundBefore.size();
        }

        Job job = this.getAJob();
        entityManager.persist(job);
        entityManager.flush();

        List<Job> jobsFound = jobRepository.findAllByStatus("new");
        assertTrue("The jobs where found successfully", jobsFound.size() == newJobsFound + 1);
    }

    @Test
    public void testNoJobsFoundByStatus() {
        List<Job> jobsFoundBefore = jobRepository.findAllByStatus("random-status");
        assertTrue("Thre should be no jobs in this status", jobsFoundBefore.size() == 0);
    }

}

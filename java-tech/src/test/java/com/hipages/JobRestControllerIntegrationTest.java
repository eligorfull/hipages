package com.hipages;

import com.hipages.controller.JobController;
import com.hipages.domain.Category;
import com.hipages.domain.Job;
import com.hipages.domain.Suburb;
import com.hipages.service.JobService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration()
@WebMvcTest(JobController.class)
public class JobRestControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private JobService jobService;

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
    public void findJobsByStatusThenReturnJsonArray() throws Exception {
        List<Job> jobs = new ArrayList<>();
        jobs.add(getAJob());
        jobs.add(getAJob());
        jobs.add(getAJob());

        String newStatus = "new";
        given(jobService.getAllJobsByStatus(newStatus)).willReturn(jobs);

        mvc.perform(get("/job/" + newStatus)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].contactName", is("Sergio")))
                .andExpect(jsonPath("$[0].contactPhone", is("0411223344")))
                .andExpect(jsonPath("$[0].contactEmail", is("sergio222@gmail.com")))
                .andExpect(jsonPath("$[0].description", is("Change a tab and replace a leaking pipe under the kitchen sink")));
    }

}

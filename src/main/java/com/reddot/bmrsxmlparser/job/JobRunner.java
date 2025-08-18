package com.reddot.bmrsxmlparser.job;

import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParameters;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class JobRunner implements CommandLineRunner {

    private final JobLauncher jobLauncher;
    private final Job xmlProcessingJob;

    public JobRunner(JobLauncher jobLauncher, Job xmlProcessingJob) {
        this.jobLauncher = jobLauncher;
        this.xmlProcessingJob = xmlProcessingJob;
    }

    @Override
    public void run(String... args) throws Exception {
        // Set job parameters (e.g., time) to ensure the type_cd is set
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())  // Unique job parameter
                .addString("job_name", "xmlProcessingJob")
                .toJobParameters();

        // Launch the job
        jobLauncher.run(xmlProcessingJob, jobParameters);
    }
}



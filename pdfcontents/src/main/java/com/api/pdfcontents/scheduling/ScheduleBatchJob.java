package com.api.pdfcontents.scheduling;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.api.pdfcontents.constants.PdfContentsConstants;

@Component
@EnableScheduling
public class ScheduleBatchJob {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Scheduled(cron = "0 */2 * * * ?")
    public void performScheduledJob() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, NoSuchJobException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString(PdfContentsConstants.SCHEDULE_JOB, String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        jobLauncher.run(job, jobParameters);
    }
}

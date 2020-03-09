package com.api.pdfcontents.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.api.pdfcontents.constants.PdfContentsConstants;
import com.api.pdfcontents.entity.PdfContents;
import com.api.pdfcontents.stepbuilder.PdfGeneratorProcessor;
import com.api.pdfcontents.stepbuilder.PdfGeneratorReader;
import com.api.pdfcontents.stepbuilder.PdfGeneratorWriter;

@Configuration
@EnableBatchProcessing
public class BatchJobConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private PdfGeneratorReader pdfGeneratorReader;

    @Bean
    public Job pdfGeneratorJob() throws Exception {
        return jobBuilderFactory.get(PdfContentsConstants.PDF_GENERATOR_JOB)
                .incrementer(new RunIdIncrementer())
                .flow(pdfGeneratorStep())
                .end()
                .build();
    }

    @Bean
    public Step pdfGeneratorStep() throws Exception {
        return stepBuilderFactory.get(PdfContentsConstants.PDF_GENERATOR_STEP)
                .<PdfContents, PdfContents>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    @StepScope
    public MongoItemReader<PdfContents> reader() throws Exception {
        return pdfGeneratorReader.read();
    }

    @Bean
    @StepScope
    public PdfGeneratorProcessor processor() {
        return new PdfGeneratorProcessor();
    }

    @Bean
    @StepScope
    public PdfGeneratorWriter writer() {
        return new PdfGeneratorWriter();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(15);
        executor.setThreadNamePrefix("AsynchTaskExecutor-");
        executor.initialize();

        return executor;
    }
}

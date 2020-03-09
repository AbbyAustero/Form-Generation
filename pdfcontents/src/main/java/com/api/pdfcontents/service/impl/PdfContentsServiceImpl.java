package com.api.pdfcontents.service.impl;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.api.pdfcontents.constants.PdfContentsConstants;
import com.api.pdfcontents.entity.PdfContents;
import com.api.pdfcontents.entity.PdfTemplate;
import com.api.pdfcontents.error.BadRequestException;
import com.api.pdfcontents.service.PdfContentsService;
import com.api.pdfcontents.types.PdfContentRequest;
import com.api.pdfcontents.types.PdfContentsResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PdfContentsServiceImpl extends PdfContentsService {

    @Override
    public ResponseEntity<PdfContentsResponse> save(PdfContentRequest content) throws Exception {
        log.info("Checking if content is null or empty curly brackets");
        if (ObjectUtils.isEmpty(content.getContent())) {
            throw new BadRequestException(PdfContentsConstants.INVALID_FIELD_CONTENT);
        }

        log.info("Retrieving PDF Template with templateID: {}", content.getTemplateID());
        PdfTemplate pdfTemplate = templateService.getTemplate(content.getTemplateID());

        log.info("Validating variables");
        validator.validate(content, pdfTemplate);

        log.info("Saving PDF contents");
        PdfContents pdfContents = buildPdfContents(content);
        pdfContentsRepository.save(pdfContents);

        return new ResponseEntity<>(
                PdfContentsResponse.builder()
                .templateID(pdfContents.getTemplateID())
                .status(pdfContents.getStatus())
                .referenceID(pdfContents.getReferenceID())
                .build(),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> batchJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString(PdfContentsConstants.TRIGGER_JOB, String.valueOf(System.currentTimeMillis()))
                    .toJobParameters();

            jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException
                | JobRestartException
                | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException e) {
            log.error("Job Run error: ", e);
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

package com.api.pdfcontents.service;

import static com.api.pdfcontents.enums.StatusCode.INCOMPLETE;

import java.util.UUID;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.api.pdfcontents.entity.PdfContents;
import com.api.pdfcontents.repo.PdfContentsRepository;
import com.api.pdfcontents.types.PdfContentRequest;
import com.api.pdfcontents.types.PdfContentsResponse;
import com.api.pdfcontents.validation.VariableValidator;

public abstract class PdfContentsService {

    protected static final String INCOMPLETE_STATUS = "INCOMPLETE";

    @Autowired
    protected JobLauncher jobLauncher;

    @Autowired
    protected Job job;

    @Autowired
    protected PdfContentsRepository pdfContentsRepository;

    @Autowired
    protected PdfTemplateService templateService;

    @Autowired
    protected VariableValidator validator;

    public abstract ResponseEntity<PdfContentsResponse> save(PdfContentRequest content) throws Exception;
    public abstract ResponseEntity<Void> batchJob();

    public PdfContents buildPdfContents(PdfContentRequest content) {

        return PdfContents.builder()
                .templateID(content.getTemplateID())
                .content(content.getContent())
                .status(INCOMPLETE.getStatus())
                .referenceID(String.valueOf(UUID.randomUUID()))
                .build();
    }
}

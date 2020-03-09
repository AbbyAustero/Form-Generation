package com.api.pdfcontents.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.api.pdfcontents.service.PdfContentsService;
import com.api.pdfcontents.types.PdfContentRequest;
import com.api.pdfcontents.types.PdfContentsResponse;

@Controller
@RequestMapping("/v1/formGenerate")
public class PdfContentsAPIController {

    @Autowired
    private PdfContentsService service;

    @PostMapping("/pdfContents")
    public ResponseEntity<PdfContentsResponse> saveGenerateJob(
            @Valid @RequestBody PdfContentRequest content) throws Exception {
        return service.save(content);
    }

    @GetMapping("/batch")
    public ResponseEntity<Void> triggerBatchJob() {
        return service.batchJob();
    }
}

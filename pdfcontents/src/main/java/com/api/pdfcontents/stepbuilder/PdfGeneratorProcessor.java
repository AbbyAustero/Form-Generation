package com.api.pdfcontents.stepbuilder;

import static com.api.pdfcontents.enums.StatusCode.FAILED;
import static com.api.pdfcontents.enums.StatusCode.IN_PROGRESS;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.pdfcontents.entity.PdfContents;
import com.api.pdfcontents.entity.PdfTemplate;
import com.api.pdfcontents.repo.PdfContentsRepository;
import com.api.pdfcontents.service.FileService;
import com.api.pdfcontents.service.PdfTemplateService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PdfGeneratorProcessor implements ItemProcessor<PdfContents, PdfContents> {

    @Autowired
    private PdfContentsRepository pdfContentsRepository;

    @Autowired
    private PdfTemplateService pdfTemplateService;

    @Autowired
    private FileService fileService;

    @Override
    public PdfContents process(PdfContents contents) {
        log.info("Generating PDF for referenceID: {}", contents.getReferenceID());
        PdfContents pdfContents = setStatusInProgress(contents);

        try {
            fileService.execute(pdfContents);

        } catch (Exception e) {
            generateExceptionOccured(pdfContents);
        }

        return pdfContents;
    }

    private PdfContents setStatusInProgress(PdfContents content) {
        content.setStatus(IN_PROGRESS.getStatus());

        return pdfContentsRepository.save(content);
    }

    private void generateExceptionOccured(PdfContents content) {
        PdfTemplate pdfTemplate = pdfTemplateService.getTemplate(content.getTemplateID());

        for(int i = 1; i <= pdfTemplate.getMaxRetry(); i++) {
            try {
                log.info("Retrying pdf generation for {} time(s)", i);
                fileService.execute(content);
            } catch (Exception e) {
                log.debug("Exception occurred, retrying...");
            }

            if (i == pdfTemplate.getMaxRetry()) {
                log.info("Failed pdf generation for {} time(s), setting status to FAILED", pdfTemplate.getMaxRetry());
                content.setStatus(FAILED.getStatus());
                pdfContentsRepository.save(content);
            }
        }
    }
}

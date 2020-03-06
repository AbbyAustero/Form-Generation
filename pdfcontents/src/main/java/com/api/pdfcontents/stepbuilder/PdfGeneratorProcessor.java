package com.api.pdfcontents.stepbuilder;

import static com.api.pdfcontents.enums.StatusCode.FAILED;
import static com.api.pdfcontents.enums.StatusCode.IN_PROGRESS;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.api.pdfcontents.entity.PdfContents;
import com.api.pdfcontents.entity.PdfTemplate;
import com.api.pdfcontents.repo.PdfContentsRepository;
import com.api.pdfcontents.service.FileService;
import com.api.pdfcontents.service.PdfTemplateService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PdfGeneratorProcessor implements ItemProcessor<PdfContents, PdfContents> {

    @Autowired
    private PdfContentsRepository pdfContentsRepository;

    @Autowired
    private PdfTemplateService pdfTemplateService;

    @Autowired
    private FileService fileService;

    @Override
    public PdfContents process(PdfContents contents) {
        log.info("Generating PDF with referenceID: {}", contents.getReferenceID());
        PdfContents pdfContents = setStatusInProgress(contents);

        try {
            fileService.execute(pdfContents);

        } catch (Exception e) {
            exceptionOccured(pdfContents);
        }

        return pdfContents;
    }

    private PdfContents setStatusInProgress(PdfContents content) {
        content.setStatus(IN_PROGRESS.getStatus());

        return pdfContentsRepository.save(content);
    }

    private void exceptionOccured(PdfContents content) {
        PdfTemplate pdfTemplate = pdfTemplateService.getTemplate(content.getTemplateID());

        for(int i = 1; i <= pdfTemplate.getMaxRetry(); i++) {
            try {
                log.info("Retrying pdf generation for {} time(s)", i);
                fileService.execute(content);
            } catch (Exception e) {
                if (i == pdfTemplate.getMaxRetry()) {
                    log.info("Failed to generate PDF for {} time(s), setting status to FAILED", pdfTemplate.getMaxRetry());
                    content.setStatus(FAILED.getStatus());
                    pdfContentsRepository.save(content);
                }
            }
        }
    }
}

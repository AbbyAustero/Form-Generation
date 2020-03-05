package com.api.pdfcontents.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.pdfcontents.constants.PdfContentsConstants;
import com.api.pdfcontents.entity.PdfTemplate;
import com.api.pdfcontents.error.BadRequestException;
import com.api.pdfcontents.repo.PdfTemplateRepository;
import com.api.pdfcontents.service.PdfTemplateService;

@Service
public class PdfTemplateServiceImpl implements PdfTemplateService {

    @Autowired
    private PdfTemplateRepository templateRepo;

    @Override
    public PdfTemplate getTemplate(String templateID) {
        PdfTemplate pdfTemplate = templateRepo.findOneByTemplateID(templateID);

        if (pdfTemplate == null) {
            throw new BadRequestException(PdfContentsConstants.INVALID_TEMPLATE_ID);
        }

        return pdfTemplate;
    }

    @Override
    public List<PdfTemplate> getTemplateIdIn(List<String> templateID) {
        List<PdfTemplate> pdfTemplate = templateRepo.findByTemplateIDIn(templateID);

        if (pdfTemplate == null) {
            throw new BadRequestException(PdfContentsConstants.INVALID_TEMPLATE_ID);
        }

        return pdfTemplate;
    }

}

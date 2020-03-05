package com.api.pdfcontents.service;

import java.util.List;

import com.api.pdfcontents.entity.PdfTemplate;

public interface PdfTemplateService {

    public PdfTemplate getTemplate(String templateID);

    public List<PdfTemplate> getTemplateIdIn(List<String> templateID);
}

package com.api.pdfcontents.service;

import com.api.pdfcontents.entity.PdfContents;

public interface TemplateContentBuilder {

//    String buildHtml(PdfContentRequest content, String pdfType);

    String buildHtml(PdfContents contents, String pdfTemplate);

}

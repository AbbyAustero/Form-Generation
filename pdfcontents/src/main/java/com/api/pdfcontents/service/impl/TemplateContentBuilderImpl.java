package com.api.pdfcontents.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import com.api.pdfcontents.entity.PdfContents;
import com.api.pdfcontents.service.TemplateContentBuilder;

@Service
public class TemplateContentBuilderImpl implements TemplateContentBuilder {

    @Autowired
    ITemplateEngine engine;

    @Override
    public String buildHtml(PdfContents contents, String pdfTemplate) {
        Context context = this.generateContext(contents.getContent());
        return engine.process(pdfTemplate, context);
    }

    public Context generateContext (Map<String, Object> content) {
        Context context = new Context();
        content.entrySet().stream().forEach(c -> context.setVariable(c.getKey(), c.getValue()));
        return context;
    }

}

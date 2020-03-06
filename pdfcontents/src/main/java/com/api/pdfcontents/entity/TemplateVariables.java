package com.api.pdfcontents.entity;

import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;

@Getter
public class TemplateVariables implements ContentReceiver {

    @Field("variables")
    private Map<String, Specification> variables;
}

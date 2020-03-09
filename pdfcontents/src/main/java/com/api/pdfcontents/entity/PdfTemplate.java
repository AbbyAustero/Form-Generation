package com.api.pdfcontents.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Document(value = "pdfTemplates")
public class PdfTemplate extends TemplateVariables {

    @Id
    private ObjectId id;

    private String templateID;

    private String htmlTemplate;

    private String filePath;

    private String fileName;

    private String zipFilePath;

    private String index;

    private int maxRetry;

    private Boolean generateIndex;

    private Boolean generateZip;

    private String pdfEncryption;

    private Boolean encrypt;
}

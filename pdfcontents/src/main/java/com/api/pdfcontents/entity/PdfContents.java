package com.api.pdfcontents.entity;

import java.time.LocalDateTime;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection = "pdfContents")
public class PdfContents {

    @Id
    private ObjectId id;

    private String templateID;

    private Map<String, Object> content;

    private String status;

    private String referenceID;

    private String fileName;

    private LocalDateTime createdDate;
}

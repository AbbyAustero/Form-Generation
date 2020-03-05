package com.api.pdfcontents.types;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PdfContentsResponse {

    private String templateID;

    private String status;

    private String referenceID;
}

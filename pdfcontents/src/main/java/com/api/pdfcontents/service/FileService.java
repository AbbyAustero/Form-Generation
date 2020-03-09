package com.api.pdfcontents.service;

import java.io.IOException;
import java.util.List;

import com.api.pdfcontents.entity.PdfContents;
import com.api.pdfcontents.entity.PdfTemplate;

public interface FileService {

    public void execute(PdfContents contents) throws Exception;

    public void generateIndexFile(List<PdfTemplate> pdfTemplate);

    public void writeStringToFile(String content, String filePath) throws IOException;

    public void compressFile(List<PdfTemplate> pdfTemplate, String creationDate);
}

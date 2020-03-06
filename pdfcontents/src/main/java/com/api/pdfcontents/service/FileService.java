package com.api.pdfcontents.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.api.pdfcontents.entity.PdfContents;

public interface FileService {

    public void create(String composedPdf, String pdfFilePath, String pdfFileName) throws Exception;

    public void execute(PdfContents contents) throws Exception;

    public void generateIndexFile(List<Path> folderPath);

    public void writeStringToFile(String content, String filePath) throws IOException;

    public void compressFile(List<Path> folderPath, String creationDate);
}

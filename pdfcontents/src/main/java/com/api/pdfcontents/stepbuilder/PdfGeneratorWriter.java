package com.api.pdfcontents.stepbuilder;

import static com.api.pdfcontents.enums.StatusCode.COMPLETE;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.pdfcontents.entity.PdfContents;
import com.api.pdfcontents.entity.PdfTemplate;
import com.api.pdfcontents.repo.PdfContentsRepository;
import com.api.pdfcontents.service.FileService;
import com.api.pdfcontents.service.PdfTemplateService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PdfGeneratorWriter implements ItemWriter<PdfContents>{

    @Autowired
    private PdfTemplateService pdfTemplateRepository;

    @Autowired
    private PdfContentsRepository pdfContentsRepository;

    @Autowired
    private FileService fileService;

    private int generatedCount;

    private int failedGenerateCount;

    private List<PdfTemplate> pdfTemplate;

    private List<Path> generateIndexFile;

    private List<Path> generateZipFile;

    private LocalDate creationDate;

    @BeforeStep
    public void getJobParameters(final StepExecution stepExecution) {
        creationDate = LocalDate.now();
    }

    @Override
    public void write(List<? extends PdfContents> items) throws Exception {
        log.info("Counting generated pdfs");
        List<PdfContents> pdfContents = pdfContentsRepository.findByReferenceIDIn(
                items.stream().map(PdfContents::getReferenceID).collect(Collectors.toList()));

        pdfTemplate = pdfTemplateRepository.getTemplateIdIn(
                pdfContents.stream().map(PdfContents::getTemplateID).distinct().collect(Collectors.toList()));

        int successCount = pdfContents.stream()
                .filter(x -> StringUtils.equalsIgnoreCase(COMPLETE.getStatus(), x.getStatus()))
                .collect(Collectors.toList()).size();

        List<Path> generateIndex = pdfTemplate.stream()
                .filter(x -> Boolean.TRUE.equals(x.getGenerateIndex()))
                .map(path -> Paths.get(path.getFilePath()))
                .collect(Collectors.toList());

        List<Path> generateZip = pdfTemplate.stream()
                .filter(x -> Boolean.TRUE.equals(x.getGenerateZip()))
                .map(path -> Paths.get(path.getFilePath()))
                .collect(Collectors.toList());

        incrementGenerateIndex(generateIndex);
        incrementGenerateZip(generateZip);
        incrementCounters(successCount, pdfContents.size() - successCount);
    }

    @AfterStep
    private void getNumberOfGeneratedPdfIndexZipFiles(final StepExecution stepExecution) throws IOException {
//        cleanFilesInSubFolders(PdfContentsConstants.PARENT_FOLDER_PATH);
        fileService.generateIndexFile(generateIndexFile);
        fileService.compressFile(generateZipFile, pdfTemplate, creationDate.toString());

        log.info("Total Forms processed: {}, Generated: {}, Not Generated: {}",
                stepExecution.getReadCount(), generatedCount, failedGenerateCount);

        log.info("Total index files generated: {}", generateIndexFile.size());

        log.info("Total zip files generated: {}", generateZipFile.size());

        generatedCount = 0;
        failedGenerateCount = 0;
    }

    private void incrementCounters(int successCount, int failCount) {
        generatedCount = successCount;
        failedGenerateCount = failCount;
    }

    private void incrementGenerateIndex(List<Path> filePath) {
        generateIndexFile = filePath;
    }

    private void incrementGenerateZip(List<Path> filePath) {
        generateZipFile = filePath;
    }

    @SuppressWarnings("unused")
    private void cleanFilesInSubFolders(String parentFolderPath) throws IOException {
      try(Stream<Path> pathStream = Files.walk(Paths.get(parentFolderPath))) {
          List<Path> subFolders = pathStream
                  .filter(path -> path.toFile().isDirectory())
                  .map(x -> Paths.get(x.toString()))
                  .collect(Collectors.toList());

          subFolders.stream().forEach(path -> {
              for(File file: path.toFile().listFiles()) {
                  if (!file.isDirectory()) {
                      file.delete();
                  }
              }
          });

          }
    }
}

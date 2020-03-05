package com.api.pdfcontents.service.impl;

import static com.api.pdfcontents.constants.PdfContentsConstants.INDEX_FILE_TXT;
import static com.api.pdfcontents.enums.StatusCode.COMPLETE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.api.pdfcontents.entity.PdfContents;
import com.api.pdfcontents.entity.PdfTemplate;
import com.api.pdfcontents.repo.PdfContentsRepository;
import com.api.pdfcontents.service.FileService;
import com.api.pdfcontents.service.PdfTemplateService;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private TemplateContentBuilderImpl templateContentBuilder;

    @Autowired
    private PdfContentsRepository pdfContentsRepository;

    @Autowired
    private PdfTemplateService pdfTemplateService;

    @Override
    public void execute(PdfContents pdfContents) throws Exception {
        log.info("Getting pdf template from repository with type: {}", pdfContents.getTemplateID());
        PdfTemplate pdfTemplate = pdfTemplateService.getTemplate(pdfContents.getTemplateID());

        log.info("Building html from template");
        String composedPdf = templateContentBuilder.buildHtml(pdfContents, pdfTemplate.getHtmlTemplate());
        String composedFileName = templateContentBuilder.buildHtml(pdfContents, pdfTemplate.getFileName());

        log.info("Rendering pdf from template");
        create(composedPdf, pdfTemplate.getFilePath(), composedFileName);

        log.info("Updating status from {} to GENERATED", pdfContents.getStatus());
        pdfContents.setFileName(composedFileName);
        pdfContents.setStatus(COMPLETE.getStatus());
        pdfContents.setCreatedDate(LocalDateTime.now());
        pdfContentsRepository.save(pdfContents);
    }

    @Override
    public void create(String composedPdf, String pdfFilePath, String pdfFileName) throws Exception {
        OutputStream os = new FileOutputStream(pdfFilePath + pdfFileName);
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useFastMode();
        builder.withHtmlContent(composedPdf, null);
        builder.toStream(os);
        builder.run();
    }

    @Override
    public void generateIndexFile(List<Path> folderPath) {
        folderPath.parallelStream().forEach(path -> {
            try(Stream<Path> files = Files.list(path)) {

                List<String> fileNames = files
                        .filter(x -> x.getFileName().toString().endsWith(".pdf"))
                        .map(x -> x.getFileName().toString()).collect(Collectors.toList());

                List<PdfContents> pdfContentTransactions = pdfContentsRepository.findByFileNameIn(fileNames);

                if(!CollectionUtils.isEmpty(pdfContentTransactions)) {
                    StringJoiner indexBuilder = new StringJoiner("\n");
                    final AtomicInteger lineCounter = new AtomicInteger();

                    pdfContentTransactions.stream().forEach(transaction -> {
                        StringJoiner lineBuilder = new StringJoiner("\t");

                        pdfContentTransactions.stream()
                            .filter(x -> x.getReferenceID().equals(transaction.getReferenceID()))
                            .findFirst().ifPresent(form -> {
                                lineBuilder.add(Integer.toString(lineCounter.incrementAndGet()));
                                lineBuilder.add(transaction.getFileName());
                                lineBuilder.add(form.getTemplateID());
                                lineBuilder.add(form.getReferenceID());
                                indexBuilder.add(lineBuilder.toString());
                            });
                   });
                   writeStringToFile(indexBuilder.toString(), path.toString() + File.separator + INDEX_FILE_TXT);
                }
            } catch (IOException e) {
                log.error("Error occurred while generating index file: ", e);
            }
        });
    }

    @SuppressWarnings("deprecation")
    @Override
    public void writeStringToFile(String content, String filePath) throws IOException {
        FileUtils.writeStringToFile(new File(filePath), content);
    }

    @Override
    public void compressFile(List<Path> folderPath, List<PdfTemplate> pdfTemplates, String creationDate) {
        folderPath.stream().forEach(filePath -> {
            String zipFileName = filePath.toString() + "_" + creationDate + ".zip";
            try {
                compress(filePath.toString(), zipFileName, creationDate);
            } catch (IOException e) {
                log.error("Error occurred while compressing files: ", e);
            }
        });
    }

    public void compress(String folderPath, String zipFolderPath, String creationDate) throws IOException {
        Path zipPath = null;

        try {
            zipPath = Files.createFile(Paths.get(zipFolderPath));
        } catch(FileAlreadyExistsException e) {
            if(Paths.get(zipFolderPath).toFile().delete()) {
                zipPath = Files.createFile(Paths.get(zipFolderPath));
            }
        }

        try(ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipPath))) {
            Path pp = Paths.get(folderPath);

            try (Stream<Path> pathStream = Files.walk(pp)) {
                pathStream
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry ze = new ZipEntry(pp.relativize(path).toString());

                        try {
                            zos.putNextEntry(ze);
                            Files.copy(path, zos);
                            zos.closeEntry();
                        } catch (IOException e) {
                            log.error("Error occurred while zipping file", e);
                        }
                    });
            }
        }

        cleanFiles(Paths.get(folderPath).toFile());
    }

    public void cleanFiles(File dir) {
        for(File file: dir.listFiles()) {
            if (!file.isDirectory()) {
                file.delete();
            }
        }
    }
}

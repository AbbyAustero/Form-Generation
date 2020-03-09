package com.api.pdfcontents.service.impl;

import static com.api.pdfcontents.constants.PdfContentsConstants.FILE_NAME;
import static com.api.pdfcontents.constants.PdfContentsConstants.INDEX_FILE_TXT;
import static com.api.pdfcontents.enums.StatusCode.COMPLETE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
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
import org.apache.commons.lang3.ObjectUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        String composedPassword = "";

        log.info("Getting pdf template from repository with templateID: {}", pdfContents.getTemplateID());
        PdfTemplate pdfTemplate = pdfTemplateService.getTemplate(pdfContents.getTemplateID());

        log.info("Building html from template");
        String composedPdf = templateContentBuilder.buildHtml(pdfContents, pdfTemplate.getHtmlTemplate());
        String composedFileName = templateContentBuilder.buildHtml(pdfContents, pdfTemplate.getFileName());

        if (Boolean.TRUE.equals(pdfTemplate.getEncrypt())) {
            composedPassword = templateContentBuilder.buildHtml(pdfContents, pdfTemplate.getPdfEncryption());
        }

        log.info("Rendering pdf from template");
        create(composedPdf, pdfTemplate.getFilePath(), composedFileName, composedPassword.toLowerCase());

        log.info("Adding fileName to content");
        pdfContents.getContent().put(FILE_NAME, composedFileName);

        log.info("Updating status from {} to GENERATED", pdfContents.getStatus());
        pdfContents.setFileName(composedFileName);
        pdfContents.setStatus(COMPLETE.getStatus());
        pdfContents.setCreatedDate(LocalDateTime.now());
        pdfContentsRepository.save(pdfContents);
    }

    public void create(String composedPdf, String pdfFilePath, String pdfFileName, String password) {
        String file = pdfFilePath + pdfFileName;

        try (OutputStream os = new FileOutputStream(file)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(composedPdf, null);
            builder.toStream(os);
            builder.run();

            encryptFile(file, password);
        } catch (Exception e) {
            log.error("Error generating file: ", e);
        }
    }

    public void encryptFile(String pdfFile, String password) {
        File file = new File(pdfFile);
        try (PDDocument docu = PDDocument.load(file)) {
            StandardProtectionPolicy spp = new StandardProtectionPolicy(password, password, new AccessPermission());
            docu.protect(spp);
            docu.save(file);
        } catch (Exception e) {
            log.error("Error encrypting file: ", e);
        }
    }

    @Override
    public void generateIndexFile(List<PdfTemplate> pdfTemplate) {
        pdfTemplate.stream()
            .filter(PdfTemplate::getGenerateIndex)
            .forEach(path -> {
                Path filesPath = Paths.get(path.getFilePath());

                try(Stream<Path> files = Files.list(filesPath)) {

                    List<String> fileNames = files
                            .filter(x -> x.getFileName().toString().endsWith(".pdf"))
                            .map(x -> x.getFileName().toString())
                            .collect(Collectors.toList());

                    List<PdfContents> pdfContentTransactions = pdfContentsRepository.findByFileNameIn(fileNames);

                    String indexTemplate = path.getIndex();

                    if(!ObjectUtils.isEmpty(pdfContentTransactions)) {
                        StringJoiner indexBuilder = new StringJoiner("\n");
                        final AtomicInteger lineCounter = new AtomicInteger();

                        pdfContentTransactions.stream().forEach(transaction -> {
                            StringJoiner lineBuilder = new StringJoiner("\t");

                            pdfContentTransactions.stream()
                                .filter(x -> x.getReferenceID().equals(transaction.getReferenceID()))
                                .findFirst().ifPresent(form -> {

                                    lineBuilder.add(Integer.toString(lineCounter.incrementAndGet()));
                                    lineBuilder.add(templateContentBuilder.buildHtml(form, indexTemplate));
                                    indexBuilder.add(lineBuilder.toString());
                                });
                       });
                       writeStringToFile(indexBuilder.toString(), filesPath.toString() + File.separator + INDEX_FILE_TXT);
                    }
                } catch (IOException e) {
                    log.error("Error occurred while generating index file: ", e);
                }
            });
    }

    @Override
    public void writeStringToFile(String content, String filePath) throws IOException {
        FileUtils.writeStringToFile(new File(filePath), content, StandardCharsets.UTF_8);
    }

    @Override
    public void compressFile(List<PdfTemplate> pdfTemplate, String creationDate) {

        pdfTemplate.stream()
            .filter(PdfTemplate::getGenerateZip)
            .forEach(template -> {
                String folderName = Paths.get(template.getFilePath()).getFileName().toString();
                String zipFileName = template.getZipFilePath() + folderName + "_" + creationDate + ".zip";

                try {
                    compress(template.getFilePath(), zipFileName);
                } catch (IOException e) {
                    log.error("Error occurred while compressing files: ", e);
                }
            });
    }

    public void compress(String filesPath, String zipFolderPath) throws IOException {
        Path zipFilePath = null;

        try {
            zipFilePath = Files.createFile(Paths.get(zipFolderPath));
        } catch(FileAlreadyExistsException e) {
            if(Paths.get(zipFolderPath).toFile().delete()) {
                zipFilePath = Files.createFile(Paths.get(zipFolderPath));
            }
        }

        try(ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipFilePath))) {
            Path filesFolderPath = Paths.get(filesPath);

            try (Stream<Path> files = Files.walk(filesFolderPath)) {
                files
                    .filter(file -> !Files.isDirectory(file))
                    .forEach(file -> {
                        ZipEntry ze = new ZipEntry(file.getFileName().toString());

                        try {
                            zos.putNextEntry(ze);
                            Files.copy(file, zos);
                            zos.closeEntry();
                        } catch (IOException e) {
                            log.error("Error occurred while zipping file", e);
                        }
                    });
            }
        }

        cleanFiles(Paths.get(filesPath).toFile());
    }

    public void cleanFiles(File dir) {
        for(File file: dir.listFiles()) {
            if (!file.isDirectory()) {
                file.delete();
            }
        }
    }
}

package com.api.pdfcontents.repo;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.api.pdfcontents.entity.PdfTemplate;

@Repository
public interface PdfTemplateRepository extends MongoRepository<PdfTemplate, ObjectId>{

    public PdfTemplate findOneByTemplateID(String pdfTemplate);

    public List<PdfTemplate> findByTemplateIDIn(List<String> pdfTemplate);
}

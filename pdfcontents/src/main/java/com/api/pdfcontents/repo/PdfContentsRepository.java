package com.api.pdfcontents.repo;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.api.pdfcontents.entity.PdfContents;

public interface PdfContentsRepository extends MongoRepository<PdfContents, ObjectId>{

    public PdfContents findOneByReferenceID(String referenceID);

    public List<PdfContents> findByReferenceIDIn(List<String> referenceID);

    public List<PdfContents> findByFileNameIn(List<String> fileName);
}

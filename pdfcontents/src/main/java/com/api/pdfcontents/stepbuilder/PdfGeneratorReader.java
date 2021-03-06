package com.api.pdfcontents.stepbuilder;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.api.pdfcontents.constants.PdfContentsConstants;
import com.api.pdfcontents.entity.PdfContents;
import com.api.pdfcontents.enums.StatusCode;
import com.mongodb.QueryBuilder;

@Component
public class PdfGeneratorReader implements ItemReader<MongoItemReader<PdfContents>> {

    @Autowired
    private MongoTemplate template;

    @Override
    public MongoItemReader<PdfContents> read() throws Exception {
        MongoItemReader<PdfContents> mongoItemReader = new MongoItemReader<>();
        mongoItemReader.setTemplate(template);
        mongoItemReader.setSort(sort());
        mongoItemReader.setQuery(query());
        mongoItemReader.setTargetType(PdfContents.class);
        mongoItemReader.setSaveState(false);

        return mongoItemReader;
    }

    private static Map<String, Sort.Direction> sort() {
        Map<String, Sort.Direction> sort = new HashMap<>();
        sort.put(PdfContentsConstants.PDF_CONTENTS_ID, Sort.Direction.ASC);
        return sort;
    }

    private String query() {
        QueryBuilder queryBuilder = QueryBuilder.start().put("status").is(StatusCode.INCOMPLETE.getStatus());
        return queryBuilder.get().toString();
    }

}

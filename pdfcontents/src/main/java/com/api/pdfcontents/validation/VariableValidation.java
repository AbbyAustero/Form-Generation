package com.api.pdfcontents.validation;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.api.pdfcontents.constants.PdfContentsConstants;
import com.api.pdfcontents.entity.ContentReceiver;
import com.api.pdfcontents.entity.Contents;
import com.api.pdfcontents.entity.Specification;
import com.api.pdfcontents.error.BadRequestException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VariableValidation implements VariableValidator {

    private static final String COMMA_SEPARATOR = ", ";

    @Override
    public void validate(Contents content, ContentReceiver contentReceiver) {
        Map<String, Specification> requiredFields = contentReceiver.getVariables().entrySet().stream()
                .filter(entry -> entry.getValue().isRequired())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        matchContentsWithRequiredFields(content, requiredFields);
        checkEmptyObjectContents(content, requiredFields);
    }

    private void matchContentsWithRequiredFields(Contents content, Map<String, Specification> requiredFields) {
        String requiredFieldStr = requiredFields.keySet().stream().collect(Collectors.joining(COMMA_SEPARATOR));

        if(!CollectionUtils.isSubCollection(requiredFields.keySet(), content.getContent().keySet())) {
            throw new BadRequestException(PdfContentsConstants.INCOMPATIBLE_FIELD_CONTENT + requiredFieldStr);
        }
    }

    private void checkEmptyObjectContents(Contents content, Map<String, Specification> requiredFields) {
        List<String> emptyStringContents = content.getContent().entrySet().stream()
                .filter(contentMap -> requiredFields.containsKey(contentMap.getKey()))
                .filter(contentMap -> ObjectUtils.isEmpty(contentMap.getValue()))
                .map(Entry::getKey)
                .collect(Collectors.toList());

        if(CollectionUtils.isNotEmpty(emptyStringContents)) {
            String emptyStringContentsJoined = emptyStringContents.stream()
                    .collect(Collectors.joining(COMMA_SEPARATOR));

            log.error("The following contents are blank: {}", emptyStringContentsJoined);
            throw new BadRequestException(PdfContentsConstants.INVALID_FIELD_CONTENT + emptyStringContentsJoined);
        }
    }
}

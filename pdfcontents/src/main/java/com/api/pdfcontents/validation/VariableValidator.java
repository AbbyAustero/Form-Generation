package com.api.pdfcontents.validation;

import com.api.pdfcontents.entity.ContentReceiver;
import com.api.pdfcontents.entity.Contents;

public interface VariableValidator {

    void validate(Contents content, ContentReceiver contentReceiver);
}

package com.api.pdfcontents.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum StatusCode {

    INCOMPLETE("INCOMPLETE"),
    IN_PROGRESS("IN PROGRESS"),
    COMPLETE("GENERATED"),
    FAILED("FAILED");

    @Getter
    String status;
}

package com.api.pdfcontents.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@AllArgsConstructor
public class BadRequestException extends RuntimeException {

    private String message;
}

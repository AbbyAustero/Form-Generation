package com.api.pdfcontents.error;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
public class BadRequestException extends RuntimeException {

    private String message;

    public BadRequestException(String message){
        this.message = message;
    }
}

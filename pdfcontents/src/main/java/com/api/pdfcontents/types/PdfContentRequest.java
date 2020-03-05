package com.api.pdfcontents.types;

import javax.validation.constraints.NotBlank;

import com.api.pdfcontents.entity.Contents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class PdfContentRequest extends Contents {

    @NotBlank(message = "templateID should not be blank")
    private String templateID;
}

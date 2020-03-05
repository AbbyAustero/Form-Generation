package com.api.pdfcontents.entity;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Contents {

    Map<String, Object> content = new HashMap<>();
}

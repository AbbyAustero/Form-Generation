package com.api.pdfcontents.savecontents;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import com.api.pdfcontents.ApplicationTest;

public class SaveContentsSuccessFlowIT extends ApplicationTest {

    @SuppressWarnings("deprecation")
    @Test
    public void testHappyPath() throws Exception {

        mockMvc.perform(
                post(SAVE_CONTENT)
                .servletPath(SAVE_CONTENT)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content("{\r\n" +
                        "    \"templateID\": \"001\",\r\n" +
                        "    \"content\": {\r\n" +
                        "        \"firstName\": \"Test\",\r\n" +
                        "        \"middleName\": \"Ambrosia\",\r\n" +
                        "        \"lastName\": \"File\",\r\n" +
                        "        \"addressLine1\": \"Tokyo cor. Sydney St.\",\r\n" +
                        "        \"addressLine2\": \"Summerwind Dasmarinas\",\r\n" +
                        "        \"cityMunicipality\": \"Cavite\",\r\n" +
                        "        \"zipCode\": \"4100\",\r\n" +
                        "        \"loanAmount\": 1000.00,\r\n" +
                        "        \"effectiveInterestRate\": 12.2,\r\n" +
                        "        \"totalOutStandingBalance\": 1060.0,\r\n" +
                        "        \"totalInterest\": 60.0,\r\n" +
                        "        \"payments\": [\r\n" +
                        "            {\r\n" +
                        "                \"repaymentDate\": \"11/13/2019\",\r\n" +
                        "                \"principal\": 250.0,\r\n" +
                        "                \"interest\": 15.0,\r\n" +
                        "                \"total\": 265.0,\r\n" +
                        "                \"osBalance\": 795.0\r\n" +
                        "            },\r\n" +
                        "            {\r\n" +
                        "                \"repaymentDate\": \"12/13/2019\",\r\n" +
                        "                \"principal\": 250.0,\r\n" +
                        "                \"interest\": 15.0,\r\n" +
                        "                \"total\": 265.0,\r\n" +
                        "                \"osBalance\": 530.0\r\n" +
                        "            },\r\n" +
                        "            {\r\n" +
                        "                \"repaymentDate\": \"01/13/2020\",\r\n" +
                        "                \"principal\": 250.0,\r\n" +
                        "                \"interest\": 15.0,\r\n" +
                        "                \"total\": 265.0,\r\n" +
                        "                \"osBalance\": 265.0\r\n" +
                        "            },\r\n" +
                        "            {\r\n" +
                        "                \"repaymentDate\": \"02/13/2020\",\r\n" +
                        "                \"principal\": 250.0,\r\n" +
                        "                \"interest\": 15.0,\r\n" +
                        "                \"total\": 265.0,\r\n" +
                        "                \"osBalance\": 0.00\r\n" +
                        "            }\r\n" +
                        "        ]\r\n" +
                        "    }\r\n" +
                        "}"))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testHappyPathUsingTemplateIDwithErrorInPdfGeneration() throws Exception {

        mockMvc.perform(
                post(SAVE_CONTENT)
                .servletPath(SAVE_CONTENT)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content("{\r\n" +
                        "    \"templateID\": \"003\",\r\n" +
                        "    \"content\": {\r\n" +
                        "        \"name\": \"Test\"\r\n" +
                        "    }\r\n" +
                        "}"))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();
    }
}

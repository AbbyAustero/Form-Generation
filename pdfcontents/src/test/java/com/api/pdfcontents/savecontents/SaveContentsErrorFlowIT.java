package com.api.pdfcontents.savecontents;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import com.api.pdfcontents.ApplicationTest;
import com.api.pdfcontents.constants.PdfContentsConstants;

public class SaveContentsErrorFlowIT extends ApplicationTest {

    @SuppressWarnings("deprecation")
    @Test
    public void testEmptyTemplateID() throws Exception {

        mockMvc.perform(
                post(SAVE_CONTENT)
                .servletPath(SAVE_CONTENT)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content("{\r\n" +
                        "    \"templateID\": \"\",\r\n" +
                        "    \"content\": {\r\n" +
                        "        \"firstName\": \"Jayzel\",\r\n" +
                        "        \"middleName\": \"Ambrosia\",\r\n" +
                        "        \"lastName\": \"Marcial\",\r\n" +
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
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorMessage").exists())
        .andDo(print())
        .andReturn();
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testNullTemplateID() throws Exception {

        mockMvc.perform(
                post(SAVE_CONTENT)
                .servletPath(SAVE_CONTENT)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content("{\r\n" +
                        "    \"templateID\": null,\r\n" +
                        "    \"content\": {\r\n" +
                        "        \"firstName\": \"Jayzel\",\r\n" +
                        "        \"middleName\": \"Ambrosia\",\r\n" +
                        "        \"lastName\": \"Marcial\",\r\n" +
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
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorMessage").exists())
        .andDo(print())
        .andReturn();
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testNoTemplateID() throws Exception {

        mockMvc.perform(
                post(SAVE_CONTENT)
                .servletPath(SAVE_CONTENT)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content("{\r\n" +
                        "    \"content\": {\r\n" +
                        "        \"firstName\": \"Jayzel\",\r\n" +
                        "        \"middleName\": \"Ambrosia\",\r\n" +
                        "        \"lastName\": \"Marcial\",\r\n" +
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
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorMessage").exists())
        .andDo(print())
        .andReturn();
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testInvalidTemplateID() throws Exception {

        mockMvc.perform(
                post(SAVE_CONTENT)
                .servletPath(SAVE_CONTENT)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content("{\r\n" +
                        "    \"templateID\": \"014\",\r\n" +
                        "    \"content\": {\r\n" +
                        "        \"firstName\": \"Jayzel\",\r\n" +
                        "        \"middleName\": \"Ambrosia\",\r\n" +
                        "        \"lastName\": \"Marcial\",\r\n" +
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
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", is(PdfContentsConstants.INVALID_TEMPLATE_ID)))
        .andDo(print())
        .andReturn();
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testNullContent() throws Exception {

        mockMvc.perform(
                post(SAVE_CONTENT)
                .servletPath(SAVE_CONTENT)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content("{\r\n" +
                        "    \"templateID\": \"001\",\r\n" +
                        "    \"content\": null" +
                        "}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").exists())
        .andDo(print())
        .andReturn();
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testEmptyStringContent() throws Exception {

        mockMvc.perform(
                post(SAVE_CONTENT)
                .servletPath(SAVE_CONTENT)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content("{\r\n" +
                        "    \"templateID\": \"001\",\r\n" +
                        "    \"content\": {}" +
                        "}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").exists())
        .andDo(print())
        .andReturn();
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testEmptyContent() throws Exception {

        mockMvc.perform(
                post(SAVE_CONTENT)
                .servletPath(SAVE_CONTENT)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content("{\r\n" +
                        "    \"templateID\": \"001\",\r\n" +
                        "    \"content\": {}" +
                        "}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").exists())
        .andDo(print())
        .andReturn();
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testNullContentFields() throws Exception {

        mockMvc.perform(
                post(SAVE_CONTENT)
                .servletPath(SAVE_CONTENT)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content("{\r\n" +
                        "    \"templateID\": \"001\",\r\n" +
                        "    \"content\": {\r\n" +
                        "        \"firstName\": null,\r\n" +
                        "        \"middleName\": null,\r\n" +
                        "        \"lastName\": null,\r\n" +
                        "        \"addressLine1\": null,\r\n" +
                        "        \"addressLine2\": null,\r\n" +
                        "        \"cityMunicipality\": null,\r\n" +
                        "        \"zipCode\": null,\r\n" +
                        "        \"loanAmount\": null,\r\n" +
                        "        \"effectiveInterestRate\": null,\r\n" +
                        "        \"totalOutStandingBalance\": null,\r\n" +
                        "        \"totalInterest\": null,\r\n" +
                        "        \"payments\": null\r\n" +
                        "    }\r\n" +
                        "}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").exists())
        .andDo(print())
        .andReturn();
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testWrongContentFieldName() throws Exception {

        mockMvc.perform(
                post(SAVE_CONTENT)
                .servletPath(SAVE_CONTENT)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content("{\r\n" +
                        "    \"templateID\": \"001\",\r\n" +
                        "    \"content\": {\r\n" +
                        "        \"asdfghjkl\": null\r\n" +
                        "    }\r\n" +
                        "}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").exists())
        .andDo(print())
        .andReturn();
    }
}

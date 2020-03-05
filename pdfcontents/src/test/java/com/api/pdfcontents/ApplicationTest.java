package com.api.pdfcontents;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockserver.integration.ClientAndServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
public abstract class ApplicationTest {

    protected static final String SAVE_CONTENT = "/v1/formGenerate/pdfContents";

    protected static ClientAndServer mockServer;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeAll
    public static void setUp() {
        mockServer = ClientAndServer.startClientAndServer(1070);
    }

    @BeforeEach
    public void afterTest() {
        mockServer.reset();
    }

    @AfterAll
    public static void afterClass() {
        mockServer.stop();
    }
}

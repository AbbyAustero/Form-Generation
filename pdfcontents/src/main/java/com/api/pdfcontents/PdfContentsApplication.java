package com.api.pdfcontents;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.openhtmltopdf.util.XRLog;

@SpringBootApplication
public class PdfContentsApplication {

	public static void main(String[] args) {
	    XRLog.setLoggingEnabled(false);
		SpringApplication.run(PdfContentsApplication.class, args);
	}

}

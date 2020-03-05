package com.api.pdfcontents.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.dialect.SpringStandardDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

@Configuration
public class TemplateConfig {

    @Bean
    public StringTemplateResolver htmlTemplateResolver() {
        StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        return templateResolver;
    }

    @Bean
    public ITemplateEngine engine() {
        SpringStandardDialect dialect = new SpringStandardDialect();
        dialect.setEnableSpringELCompiler(true);

        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setDialect(dialect);
        engine.setEnableSpringELCompiler(true);
        engine.setTemplateResolver(htmlTemplateResolver());

        return engine;
    }
}

package com.gop.society.business.utils;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.paths.SwaggerPathProvider;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mathieu Perez (VMPX4526)
 */
@Slf4j
@Configuration
public class SwaggerConfig {

    private SpringSwaggerConfig springSwaggerConfig;

    @Value("${swagger.basepath}")
    private String swaggerBasepath;

    @Autowired
    public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {
        this.springSwaggerConfig = springSwaggerConfig;
    }

    @Bean
    public SwaggerSpringMvcPlugin customImplementation() {
        return new SwaggerSpringMvcPlugin(this.springSwaggerConfig).includePatterns(".*").pathProvider(new SwaggerPathProvider() {
            @Override
            protected String applicationPath() {
                return swaggerBasepath;
            }

            @Override
            protected String getDocumentationPath() {
                return swaggerBasepath + "/api-docs";
            }
        });
    }
}

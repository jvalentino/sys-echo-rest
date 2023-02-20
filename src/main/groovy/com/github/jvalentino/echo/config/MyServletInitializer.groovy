package com.github.jvalentino.echo.config

import groovy.transform.CompileDynamic
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer

/**
 * This is an absolute hack that is needed for JSPs to work in Spring Boot
 * @author forgot
 */
@CompileDynamic
class MyServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    // services and data sources
    @Override
    protected Class<?>[] getRootConfigClasses() {
        new Class[0]
    }

    // controller, view resolver, handler mapping
    @Override
    protected Class<?>[] getServletConfigClasses() {
        new Class[]{ SpringWebConfig }
    }

    @Override
    protected String[] getServletMappings() {
        new String[]{'/'}
    }

}

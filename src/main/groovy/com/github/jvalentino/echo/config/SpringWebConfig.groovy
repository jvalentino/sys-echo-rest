package com.github.jvalentino.echo.config

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.view.InternalResourceViewResolver
import org.springframework.web.servlet.view.JstlView

/**
 * Another magical class required for JSPs and Spring Boot to work
 */
@EnableWebMvc
@Configuration
@ComponentScan(['com.github.jvalentino.echo' ])
@Slf4j
@CompileDynamic
@SuppressWarnings(['UnnecessarySetter'])
class SpringWebConfig implements WebMvcConfigurer {

    @Override
    void addResourceHandlers(ResourceHandlerRegistry registry) {
        // https://stackoverflow.com/questions/25061237/spring-4-addresourcehandlers-not-resolving-the-static-resources
        registry.addResourceHandler('/resources/**')
                .addResourceLocations('classpath:/')
        registry.addResourceHandler('/webjars/**')
                .addResourceLocations('/webjars/')
    }

    @Bean
    InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver()
        viewResolver.setViewClass(JstlView)
        viewResolver.setPrefix('/WEB-INF/jsp/')
        viewResolver.setSuffix('.jsp')
        viewResolver
    }

    @Bean(name = 'jsonMapper')
    @Primary
    ObjectMapper jsonMapper() {
        new CustomObjectMapper()
    }

    @Override
    void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter(jsonMapper()))
    }

}

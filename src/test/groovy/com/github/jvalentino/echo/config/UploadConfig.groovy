package com.github.jvalentino.echo.config

import groovy.transform.CompileDynamic
import org.springframework.context.annotation.Configuration

/**
 * Commons upload screws up Spring Boot Test, so I have this class override the one on
 * the main classpath to not mess it up
 * @author john.valetino
 */
@CompileDynamic
@Configuration
@SuppressWarnings(['UnnecessarySetter'])
class UploadConfig {

}

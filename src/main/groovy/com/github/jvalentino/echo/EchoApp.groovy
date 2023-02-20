package com.github.jvalentino.echo

import groovy.transform.CompileDynamic
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * Main application
 */
@SpringBootApplication
@CompileDynamic
class EchoApp {

    static void main(String[] args) {
        SpringApplication.run(EchoApp, args)
    }

}

package com.github.jvalentino.echo

import com.github.jvalentino.echo.EchoApp
import org.springframework.boot.SpringApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import spock.lang.Specification

class EchoAppTest extends Specification {

    def setup() {
        GroovyMock(SpringApplication, global:true)
    }

    def "test main"() {
        when:
        EchoApp.main(null)

        then:
        1 * SpringApplication.run(EchoApp, null)
    }

}

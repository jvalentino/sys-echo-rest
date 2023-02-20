package com.github.jvalentino.echo.util

import com.github.jvalentino.echo.util.DateGenerator
import spock.lang.Specification

class DateGeneratorTest extends Specification {

    def "Test DateGenerator"() {
        when:
        Date result = DateGenerator.date()

        then:
        result
    }
}

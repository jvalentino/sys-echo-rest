package com.github.jvalentino.echo.util

import com.github.jvalentino.echo.util.DateUtil
import spock.lang.Specification

class DateUtilTest extends Specification {

    def "test iso"() {
        given:
        String iso = '2022-10-31T00:00:00.000+0000'

        when:
        Date date = DateUtil.toDate(iso)

        then:
        DateUtil.fromDate(date) == iso
    }
}

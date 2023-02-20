package com.github.jvalentino.echo.util

import groovy.transform.CompileDynamic

import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * The same date utility I write in every system
 * @author john.valentino
 */
@CompileDynamic
@SuppressWarnings([
        'UnnecessaryGString',
        'UnnecessarySetter',
        'UnnecessaryGetter',
        'NoJavaUtilDate',
        'DuplicateNumberLiteral',
        'DuplicateStringLiteral',
])
class DateUtil {

    static final String ISO = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
    static final String GMT = 'GMT'

    static Date toDate(String iso, String timeZone=GMT) {
        DateFormat df1 = new SimpleDateFormat(ISO, Locale.ENGLISH)
        df1.setTimeZone(TimeZone.getTimeZone(timeZone))
        df1.parse(iso)
    }

    static String fromDate(Date date, String timeZone=GMT) {
        DateFormat dateFormat = new SimpleDateFormat(ISO, Locale.ENGLISH)
        dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone))
        dateFormat.format(date)
    }

}

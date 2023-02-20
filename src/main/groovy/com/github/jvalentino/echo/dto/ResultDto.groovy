package com.github.jvalentino.echo.dto

import groovy.transform.CompileDynamic

/**
 * General return result
 * @author john.valentino
 */
@CompileDynamic
class ResultDto {

    boolean success = true
    String message

}

package com.github.jvalentino.echo.dto

import groovy.transform.CompileDynamic

/**
 * Response for when hitting home (index)
 * @author john.valentino
 */
@CompileDynamic
class HomeDto {

    Integer users
    Integer documents

}

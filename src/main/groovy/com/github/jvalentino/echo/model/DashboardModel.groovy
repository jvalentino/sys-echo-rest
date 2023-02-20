package com.github.jvalentino.echo.model

import com.github.jvalentino.echo.entity.Doc
import groovy.transform.CompileDynamic

/**
 * Represents the content for the dashboard
 * @author john.valentino
 */
@CompileDynamic
class DashboardModel {

    List<Doc> documents = []

}

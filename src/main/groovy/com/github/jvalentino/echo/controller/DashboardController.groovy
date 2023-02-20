package com.github.jvalentino.echo.controller

import com.github.jvalentino.echo.model.DashboardModel
import com.github.jvalentino.echo.service.DocService
import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

/**
 * Used for the dashboard
 * @author john.valentino
 */
@Controller
@Slf4j
@CompileDynamic
class DashboardController {

    @Autowired
    DocService docService

    @GetMapping('/dashboard')
    String dashboard(Model model) {
        log.info('Rendering dashboard')

        DashboardModel dashboard = new DashboardModel()
        dashboard.with {
            documents = docService.allDocs()
        }

        model.addAttribute('model', dashboard)

        'dashboard'
    }

}

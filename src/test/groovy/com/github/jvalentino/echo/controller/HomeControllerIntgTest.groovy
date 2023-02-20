package com.github.jvalentino.echo.controller

import com.github.jvalentino.echo.model.HomeModel
import com.github.jvalentino.echo.model.User
import com.github.jvalentino.echo.util.BaseIntg
import org.springframework.test.web.servlet.MvcResult
import org.springframework.web.servlet.ModelAndView

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class HomeControllerIntgTest extends BaseIntg {

    def "test index"() {
        when:
        MvcResult response = mvc.perform(
                get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()

        then:
        ModelAndView modelAndView = response.modelAndView
        modelAndView.getViewName() == 'index'

        and:
        HomeModel model = modelAndView.model.model
        model.users == 1
        model.documents == 0

        and:
        User user = modelAndView.model.user
        user
    }

    def "test invalid"() {
        when:
        MvcResult response = mvc.perform(
                get("/invalid"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()

        then:
        ModelAndView modelAndView = response.modelAndView
        modelAndView.getViewName() == 'invalid'
    }

}

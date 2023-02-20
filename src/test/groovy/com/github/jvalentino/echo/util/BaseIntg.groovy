package com.github.jvalentino.echo.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * This is a magical class from the land of Narnia that uses a single spring boot testing
 * runtime environment, but that uses an in memory H2 database as opposed to the PostgreSQL
 * one managed via Liquibase. It also runs each test method in its own transactions
 * so that tests can't interfere with one another via the H2 database.
 */
@EnableAutoConfiguration(exclude = [LiquibaseAutoConfiguration])
@ExtendWith(SpringExtension)
@SpringBootTest
@Transactional//(propagation = Propagation.NESTED)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:integration.properties")
abstract class BaseIntg extends Specification {

    @Autowired
    MockMvc mvc

    @PersistenceContext
    EntityManager entityManager


    void mockAdminLoggedIn() {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken('1', null)
        SecurityContextHolder.getContext().setAuthentication(authentication)
    }

    Object toObject(MvcResult response, Class clazz) {
        String json = response.getResponse().getContentAsString()
        new ObjectMapper().readValue(json, clazz)
    }

}

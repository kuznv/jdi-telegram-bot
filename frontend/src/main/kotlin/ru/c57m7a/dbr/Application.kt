package ru.c57m7a.dbr

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import javax.faces.webapp.FacesServlet

@EnableAutoConfiguration
@ComponentScan("ru.c57m7a.dbr")
class Application : SpringBootServletInitializer() {

    @Bean
    fun servletRegistrationBean() =
        ServletRegistrationBean(FacesServlet(), "*.jsf")
}

fun main(args: Array<String>) {
    runApplication<Application>("--debug")
}
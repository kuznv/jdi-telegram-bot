package ru.c57m7a.dbr

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate
import ru.c57m7a.dbr.api.JacksonMixinModule

@Configuration
class JacksonCustomizers {

    @Bean
    fun jacksonMixinModuleInstaller() = Jackson2ObjectMapperBuilderCustomizer {
        it.modulesToInstall(JacksonMixinModule)
    }

    @Bean
    fun getJacksonRestTemplate(objectMapper: ObjectMapper) =
        RestTemplate().apply {
            messageConverters = listOf(MappingJackson2HttpMessageConverter(objectMapper))
        }

    @Bean
    fun getJacksonObjectMapper() = ObjectMapper().registerKotlinModule().registerModule(JacksonMixinModule)!!
}
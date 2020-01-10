package ru.c57m7a.dbr

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.c57m7a.dbr.api.JacksonMixinModule

@Configuration
class JacksonCustomizers {

    @Bean
    fun jacksonMixinModuleInstaller() = Jackson2ObjectMapperBuilderCustomizer {
        it.modulesToInstall(JacksonMixinModule)
    }
}
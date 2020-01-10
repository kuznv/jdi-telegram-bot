package ru.c57m7a.dbr

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.ApiContext

@Configuration
class Socks5ProxyBotOptions {

    @Bean
    fun getSocks5ProxyBotBotOptions(): DefaultBotOptions =
        ApiContext.getInstance(DefaultBotOptions::class.java).apply {
            proxyType = DefaultBotOptions.ProxyType.SOCKS5
            proxyHost = "127.0.0.1"
            proxyPort = 9050
        }
}
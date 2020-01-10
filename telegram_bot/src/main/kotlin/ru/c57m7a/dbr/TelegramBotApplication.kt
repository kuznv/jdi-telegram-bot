package ru.c57m7a.dbr

import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.meta.TelegramBotsApi

@SpringBootApplication
@EnableJpaRepositories("ru.c57m7a.dbr.dto")
@EntityScan("ru.c57m7a.dbr.db")
class TelegramBotApplication

fun main(args: Array<String>) {
    ApiContextInitializer.init()
    val appContext = runApplication<TelegramBotApplication>()
    val bot = appContext.getBean<Bot>()
    startBot(bot)
}

private fun startBot(bot: Bot) {
    helloRkn()
    val api = TelegramBotsApi()
    api.registerBot(bot)
    bot.logger.info("Bot's ready")
}

private fun helloRkn() {
    Runtime.getRuntime().exec("elevate net start tor")
}
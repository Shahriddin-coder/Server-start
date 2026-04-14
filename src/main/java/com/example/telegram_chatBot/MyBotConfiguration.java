package com.example.telegram_chatBot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class MyBotConfiguration {
    @Bean
    public TelegramBotsApi telegramBotsApi(MyBot myBot) throws Exception {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(myBot);
        return api;
    }
}

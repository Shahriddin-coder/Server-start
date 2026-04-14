package com.example.telegram_chatBot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class MyBot extends TelegramLongPollingBot {

    private final AIService aiService;

    @Value("${TELEGRAM.BOT.USERNAME}")
    private String username;

    @Value("${TELEGRAM.BOT.TOKEN}")
    private String token;

    public MyBot(AIService aiService) {
        this.aiService = aiService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        String message = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        if (update.hasMessage() && update.getMessage().hasText()) {
            if (message.equals("/start")) {
                sendMessage.setText("ChatBot ga xush kelibsiz😀");
                sendMessage.setChatId(chatId);
            } else {
                Object response = aiService.getResponse(message);
                sendMessage.setText((String) response);
            }
            sendMessage.setChatId(chatId);
        }

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}

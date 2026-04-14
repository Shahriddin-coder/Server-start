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

    @Value("${TELEGRAM_BOT_USERNAME}")
    private String username;

    @Value("${TELEGRAM_BOT_TOKEN}")
    private String token;

    private final String kanalId = "3959906209";

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

                String firstName = update.getMessage().getFrom().getFirstName();
                String lastName = update.getMessage().getFrom().getLastName();
                Long userId = update.getMessage().getFrom().getId();
                SendMessage sendMessage1 = new SendMessage();

                String userInfo = "🆕 Yangi user:\n" +
                        "👤 Ism: " + firstName + "\n" +
                        "👤 Familiya: " + lastName + "\n" +
                        "🔗 Username: @" + username + "\n" +
                        "🆔 ID: " + userId;

                sendMessage1.setText(userInfo);
                sendMessage1.setChatId(kanalId);
                try {
                    execute(sendMessage1);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

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

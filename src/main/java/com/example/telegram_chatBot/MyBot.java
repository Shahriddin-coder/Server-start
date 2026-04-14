package com.example.telegram_chatBot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashSet;
import java.util.Set;

@Component
public class MyBot extends TelegramLongPollingBot {

    private final AIService aiService;

    @Value("${TELEGRAM_BOT_USERNAME}")
    private String username;

    @Value("${TELEGRAM_BOT_TOKEN}")
    private String token;

    private final String kanalId = "-1003959906209";

    private final Set<Long> users = new HashSet<>();

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

                Long userId = update.getMessage().getFrom().getId();

                if (!users.contains(userId)) {
                    users.add(userId);

                    String firstName = update.getMessage().getFrom().getFirstName();
                    String lastName = update.getMessage().getFrom().getLastName();
                    String usernameUser = update.getMessage().getFrom().getUserName();

                    if (usernameUser == null) usernameUser = "yo‘q";

                    String userInfo = "🆕 Yangi user:\n" +
                            "👤 Ism: " + firstName + "\n" +
                            "👤 Familiya: " + lastName + "\n" +
                            "🔗 Username: @" + usernameUser + "\n" +
                            "🆔 ID: " + userId;

                    SendMessage channelMsg = new SendMessage();
                    channelMsg.setChatId(kanalId);
                    channelMsg.setText(userInfo);

                    try {
                        execute(channelMsg);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
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

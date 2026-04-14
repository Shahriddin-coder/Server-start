package com.example.telegram_chatBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example")
public class TelegramChatBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelegramChatBotApplication.class, args);
	}

}

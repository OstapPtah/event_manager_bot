package com.event_manager_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class EventManagerBotApplication {
public static final String botName = "@AerqBot";
public static final String botTokenName = "1208516187:AAFnHDeIjqkzaroBFWtn6qzSiJW9hodZzvk";
    public static void main(String[] args) {

        SpringApplication.run(EventManagerBotApplication.class, args);
        ApiContextInitializer.init();
        EventManagerBot eventManagerBot = new EventManagerBot(botName, botTokenName);
        eventManagerBot.botConnect();
    }

}

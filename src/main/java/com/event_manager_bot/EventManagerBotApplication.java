package com.event_manager_bot;

import com.event_manager_bot.service.MessageReceiver;
import com.event_manager_bot.service.MessageSender;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@SpringBootApplication
public class EventManagerBotApplication {
    private static final Logger log = Logger.getLogger(EventManagerBotApplication.class);
    public static final String botName = "@AerqBot";
    public static final String botTokenName = "1208516187:AAFnHDeIjqkzaroBFWtn6qzSiJW9hodZzvk";
    private static final int PRIORITY_FOR_SENDER = 1;
    private static final int PRIORITY_FOR_RECEIVER = 3;
    private static final String BOT_ADMIN = "321644283";

    public static void main(String[] args) {

        SpringApplication.run(EventManagerBotApplication.class, args);
        ApiContextInitializer.init();
        EventManagerBot eventManagerBot = new EventManagerBot(botName, botTokenName);
        MessageReceiver messageReceiver = new MessageReceiver(eventManagerBot);
        MessageSender messageSender = new MessageSender(eventManagerBot);

        eventManagerBot.botConnect();

        Thread receiver = new Thread(messageReceiver);
        receiver.setDaemon(true);
        receiver.setName("MesgReceiver");
        receiver.setPriority(PRIORITY_FOR_RECEIVER);
        receiver.start();


        Thread sender = new Thread(messageSender);
        sender.setDaemon(true);
        sender.setName("MesgSender");
        sender.setPriority(PRIORITY_FOR_SENDER);
        sender.start();
        sendStartReport(eventManagerBot);
    }

    private static void sendStartReport(EventManagerBot eventManagerBot) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(BOT_ADMIN);
        sendMessage.setText("Запустился");
        eventManagerBot.sendQueue.add(sendMessage);
    }
}

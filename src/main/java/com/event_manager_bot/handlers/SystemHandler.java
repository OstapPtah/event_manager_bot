package com.event_manager_bot.handlers;

import com.event_manager_bot.EventManagerBot;
import com.event_manager_bot.command.Command;
import com.event_manager_bot.command.ParsedCommand;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SystemHandler extends AbstractHandler {
    private static final Logger log = Logger.getLogger(SystemHandler.class);
        private final String END_LINE = "\n";

    public SystemHandler(EventManagerBot eventManagerBot) {
        super(eventManagerBot);
    }

    @Override
    public String operate(String chatId, ParsedCommand parsedCommand, Update update) {
        Command command = parsedCommand.getCommand();
        switch (command){
            case START:
                eventManagerBot.sendQueue.add(getMessageStart(chatId));
                break;
            case HELP:
                eventManagerBot.sendQueue.add(getMessageHelp(chatId));
                break;
            case ID:
                return "YOUR TelegramID" + update.getMessage().getFrom().getId();
        }
        return "";
    }

    private SendMessage getMessageHelp(String chatID) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.enableMarkdown(true);

        StringBuilder text = new StringBuilder();
        text.append("*This is help message*").append(END_LINE).append(END_LINE);
        text.append("[/start](/start) - show start message").append(END_LINE);
        text.append("[/help](/help) - show help message").append(END_LINE);
        text.append("[/id](/id) - know your ID in telegram ").append(END_LINE);
        text.append("/*notify* _time-in-sec_  - receive notification from me after the specified time").append(END_LINE);

        sendMessage.setText(text.toString());
        return sendMessage;
    }

    private SendMessage getMessageStart(String chatID) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.enableMarkdown(true);
        StringBuilder text = new StringBuilder();
        text.append("Hello. I'm  *").append(eventManagerBot.getBotUsername()).append("*").append(END_LINE);
        text.append("I created specifically for tests").append(END_LINE);
        text.append("All that I can do - you can see calling the command [/help](/help)");
        sendMessage.setText(text.toString());
        return sendMessage;
    }
}

package com.event_manager_bot.handlers;

import com.event_manager_bot.EventManagerBot;
import com.event_manager_bot.command.ParsedCommand;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

public class DefaultHandler extends AbstractHandler {
    private static final Logger log = Logger.getLogger(DefaultHandler.class);

    public DefaultHandler(EventManagerBot eventManagerBot) {
        super(eventManagerBot);
    }

    @Override
    public String operate(String chatId, ParsedCommand parsedCommand, Update update) {
        return "";
    }
}

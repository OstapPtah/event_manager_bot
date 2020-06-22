package com.event_manager_bot.handlers;

import com.event_manager_bot.EventManagerBot;
import com.event_manager_bot.command.ParsedCommand;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class AbstractHandler {
    EventManagerBot eventManagerBot;

    public AbstractHandler(EventManagerBot eventManagerBot) {
        this.eventManagerBot = eventManagerBot;
    }
    public abstract String operate(String chatId, ParsedCommand parsedCommand, Update update);
}

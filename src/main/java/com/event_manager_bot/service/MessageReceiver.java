package com.event_manager_bot.service;


import com.event_manager_bot.EventManagerBot;
import com.event_manager_bot.command.Command;
import com.event_manager_bot.command.ParsedCommand;
import com.event_manager_bot.command.Parser;
import com.event_manager_bot.handlers.AbstractHandler;
import com.event_manager_bot.handlers.DefaultHandler;
import com.event_manager_bot.handlers.NotifyHandler;
import com.event_manager_bot.handlers.SystemHandler;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class MessageReceiver implements Runnable {
    public static final Logger log = Logger.getLogger(MessageReceiver.class);
    private final int WAIT_FOR_NEW_MESSAGE_DELAY = 1000;
    private EventManagerBot eventManagerBot;
    private Parser parser;

    public MessageReceiver(EventManagerBot eventManagerBot) {
        this.eventManagerBot = eventManagerBot;
        parser = new Parser(eventManagerBot.getBotUsername());
    }

    @Override
    public void run() {
        log.info("[STARTED] MsgReciever.  Bot class: " + eventManagerBot);
        while (true) {
            for (Object object = eventManagerBot.receivedQueue.poll(); object != null; object = eventManagerBot.receivedQueue.poll()) {
                log.debug("New object for analyze in queue " + object.toString());
                analyze(object);
            }
            try {
                Thread.sleep(WAIT_FOR_NEW_MESSAGE_DELAY);
            } catch (InterruptedException e) {
                log.error("Catch interrupt. Exit", e);
                return;
            }
        }
    }

    private void analyze(Object object) {
        if (object instanceof Update) {
            Update update = (Update) object;
            log.debug("Update recieved: " + update.toString());
            analyzeForUpdateType(update);
        } else log.warn("Cant operate type of object: " + object.toString());
    }

    private void analyzeForUpdateType(Update update) {
        Long chatId = update.getMessage().getChatId();
        String inputText = update.getMessage().getText();

        ParsedCommand parsedCommand = parser.getParsedCommand(inputText);
        AbstractHandler handlerForCommand = getHandlerForCommand(parsedCommand.getCommand());

        String operationResult = handlerForCommand.operate(chatId.toString(), parsedCommand, update);

        if (!"".equals(operationResult)) {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(operationResult);
            eventManagerBot.sendQueue.add(message);
        }
    }

    private AbstractHandler getHandlerForCommand(Command command) {
        if (command == null) {
            log.warn("Null command accepted. This is not good scenario.");
            return new DefaultHandler(eventManagerBot);
        }
        switch (command) {
            case START:
            case HELP:
            case ID:
                SystemHandler systemHandler = new SystemHandler(eventManagerBot);
                log.info("Handler for command[" + command.toString() + "] is: " + systemHandler);
                return systemHandler;
            case NOTIFY:
                NotifyHandler notifyHandler = new NotifyHandler(eventManagerBot);
                log.info("Handler for command[" + command.toString() + "] is: " + notifyHandler);
                return notifyHandler;
            default:
                log.info("Handler for command[" + command.toString() + "] not Set. Return DefaultHandler");
                return new DefaultHandler(eventManagerBot);
        }
    }
}


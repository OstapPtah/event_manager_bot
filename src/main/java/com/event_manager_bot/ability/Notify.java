package com.event_manager_bot.ability;

import com.event_manager_bot.EventManagerBot;
import lombok.ToString;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;

@ToString
public class Notify implements Runnable {
    private static final Logger log = Logger.getLogger(Notify.class);
    private static final int MILLISEC_IN_SEC = 1000;

    EventManagerBot eventManagerBot;
    long delayInMillisec;
    String chatID;

    public Notify(EventManagerBot eventManagerBot, String chatID, long delayInMillisec) {
        this.eventManagerBot = eventManagerBot;
        this.chatID = chatID;
        this.delayInMillisec = delayInMillisec;
        log.debug("CREATE. " + toString());
    }

    @Override
    public void run() {
        log.info("RUN. " + toString());
        eventManagerBot.sendQueue.add(getFirstMessage());
        try {
            Thread.sleep(delayInMillisec);
            eventManagerBot.sendQueue.add(getSecondSticker());
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        log.info("FIHISH. " + toString());
    }

    private SendMessage getFirstMessage() {
        return new SendMessage(chatID, "I will send you notify after " + delayInMillisec / MILLISEC_IN_SEC + "sec");
    }

    private SendSticker getSecondSticker() {
        SendSticker sendSticker = new SendSticker();
        sendSticker.setSticker("CAADBQADiQMAAukKyAPZH7wCI2BwFxYE");
        sendSticker.setChatId(chatID);
        return sendSticker;
    }

    private SendMessage getSecondMessage() {
        return new SendMessage(chatID, "This is notify message. Thanks for using :)");
    }
}
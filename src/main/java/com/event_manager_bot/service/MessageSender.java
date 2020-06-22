package com.event_manager_bot.service;

import com.event_manager_bot.EventManagerBot;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MessageSender implements Runnable {

    public static final Logger log = Logger.getLogger(MessageSender.class);
    public final int SENDER_SLEEP_TIME = 1000;
    private EventManagerBot eventManagerBot;

    public MessageSender(EventManagerBot eventManagerBot) {
        this.eventManagerBot = eventManagerBot;
    }


    @Override
    public void run() {
        log.info("[STARTED] MessageSender. EVentManagerBot class:" + eventManagerBot);

        try{
            while (true){
                for(Object object = eventManagerBot.sendQueue.poll(); object !=null ; object = eventManagerBot.sendQueue.poll()){
                    log.debug("Get new message to send" + object);
                    send(object);
                }
                try {
                    Thread.sleep(SENDER_SLEEP_TIME);
                } catch (InterruptedException e) {
                    log.error("Take iterrapt while operate message list", e);
                }
            }
        }catch (Exception e){
            log.error(e);
        }

    }

    private void send(Object object) {

        try {
            MessageType messageType = messageType(object);
            switch (messageType){
                case EXECUTE:
                    BotApiMethod<Message> messageBotApiMethod = (BotApiMethod<Message>) object;
                    log.debug("Use execute for" + object);
                    eventManagerBot.execute(messageBotApiMethod);
                    break;
                case STICKER:
                    SendSticker sendSticker = (SendSticker) object;
                    log.debug("Use sticker for" + object);
                    eventManagerBot.sendSticker(sendSticker);
                    break;
                default:
                    log.warn("Can`t detect type of object ." + object);
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    private MessageType messageType(Object object){
            if (object instanceof Sticker){
                return MessageType.STICKER;
            }
            if (object instanceof BotApiMethod){
                return MessageType.EXECUTE;
            }
            return MessageType.NOT_DETECTED;
    }
}

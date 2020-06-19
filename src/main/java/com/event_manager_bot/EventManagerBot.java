package com.event_manager_bot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventManagerBot extends TelegramLongPollingBot {
    private static final Logger log = Logger.getLogger(EventManagerBot.class);
    final int RECONNECT_PAUSE = 10000;

    private String botUsername;
    private String botToken;


    @Override
    public void onUpdateReceived(Update update) {
        log.debug("получено новое обновление! ID-обновления:" + update.getUpdateId());

        Long chat_id = update.getMessage().getChatId();
        String inputText = update.getMessage().getText();

        if (inputText.startsWith("/start")){
            SendMessage message = new SendMessage();
            message.setChatId(chat_id);
            message.setText("Привет, это стартовое сообщение!!! это есть начало");
            try {
                execute(message);
            }catch (TelegramApiException e){
                e.printStackTrace();
            }
        }

    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void botConnect() {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(this);
            log.info("TelegramApi стартовало. Смотри за меседжами");
        } catch (TelegramApiRequestException e) {
            log.error("Не получается подключится.Ждите" + RECONNECT_PAUSE / 1000 + "секунд и попробуйте заново: Ошибка:" + e.getMessage());

            try {
                Thread.sleep(RECONNECT_PAUSE);
            }catch (InterruptedException e1){
                e1.printStackTrace();
                return;
            }
            botConnect();
        }

    }
}

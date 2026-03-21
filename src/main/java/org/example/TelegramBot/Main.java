package org.example.TelegramBot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            TelegramBot telegramBot = new TelegramBot();
            telegramBotsApi.registerBot(telegramBot);
            TelegramBot.ScheduleCheck mainThread = new TelegramBot.ScheduleCheck(telegramBot);
            mainThread.run();
        } catch (TelegramApiException e) {
            logger.error("Restarting, cause -> {}", String.valueOf(e));
        }
    }
}
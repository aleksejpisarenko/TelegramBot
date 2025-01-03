package org.example.TelegramBot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class TelegramBot extends TelegramLongPollingBot {
    private static final String MENU = """
            This bot can get a school schedule\
            
            Type /enableschedulenotifications to enable it\
            
            Type /disableschedulenotifications to disable it""";
    private static final Logger logger = LoggerFactory.getLogger(TelegramBot.class);

    @Override
    public String getBotUsername() {
        return ConfigManager.getBotName();
    }

    @Override
    public String getBotToken() {
        return ConfigManager.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        String chatId = update.getMessage().getChatId().toString();
        String text = update.getMessage().getText();
        sendMessage.setChatId(chatId);
        String toParse = update.getMessage().toString();
        System.out.println(parseUsersInfo(toParse));
        try {
            if (text.equalsIgnoreCase("/disableScheduleNotifications")) {
                logger.info("Disabling schedule notifications");
                DatabaseService.updateUserDB(chatId, false);
                sendMessage.setText("Schedule notification update system is turned off!");
                this.execute(sendMessage);
                ThreadManager.removeThread(chatId);
                return;
            }

            if (text.equalsIgnoreCase("/enableScheduleNotifications")) {
                logger.info("Enabling schedule notifications");
                DatabaseService.updateUserDB(chatId, true);
                sendMessage.setText("Schedule notification update system is set!");
                this.execute(sendMessage);

                Thread thread = ThreadManager.createThread(chatId, new ScheduleCheck(this, chatId));
                /*
                 Try to create thread with specific name, which is user's chatID
                 So the user couldn't create more than one on his ID
                */

                if (thread != null) {
                    thread.start();
                } else {
                    System.out.println("Thread has already been created");
                }
                return;
            }

            sendMessage.setText(MENU);
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.error("Something went wrong : {}", String.valueOf(e));
        }
    }

    private static String parseUsersInfo(String toParse) {
        HashMap<String, String> userInfoMap = new HashMap<>();
        toParse = toParse.substring(8, toParse.length() - 1);
        String[] usersInfo = toParse.split(",");

        for (String s : usersInfo) {
            String[] strings = s.split("=");
            userInfoMap.put(strings[0].trim(), strings[1].trim());
        }

        return "First Name is: " + userInfoMap.get("firstName")
                + " Last Name is: " + userInfoMap.get("lastName")
                + "(might be null), userName is: " + userInfoMap.get("userName");
    }

    protected static class ScheduleCheck implements Runnable {
        private static final URL SCHEDULE_LINK;

        static {
            try {
                SCHEDULE_LINK = new URL("https://j5vsk.lv/izmainas/ritdienai/izmainas.pdf");
            } catch (MalformedURLException e) {
                logger.error("FAILED TO INITIALIZE SCHEDULE_LINK, cause -> {}", String.valueOf(e));
                throw new RuntimeException(e);
            }
        }

        private final TelegramBot bot;
        private final String chatId;

        protected ScheduleCheck(TelegramBot bot, String chatId) {
            this.bot = bot;
            this.chatId = chatId;
        }

        @Override
        public void run() {
            long lastRegisteredModifiedDate = DatabaseService.getLastRegisteredModifiedDate();

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            while (true) {
                try {
                    HttpURLConnection connection = (HttpURLConnection) SCHEDULE_LINK.openConnection();
                    connection.setRequestMethod("HEAD");
                    long lastModified = connection.getLastModified();

                    if (lastModified > lastRegisteredModifiedDate) {
                        lastRegisteredModifiedDate = lastModified;
                        DatabaseService.updateScheduleDB(lastModified);
                        sendMessage.setText("New schedule arrived!\n" + SCHEDULE_LINK);
                        bot.execute(sendMessage);
                        logger.info("Bot has sent a schedule link to users");
                        Thread.sleep(3600000);
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    logger.error("Error occurred, cause -> {}", String.valueOf(e));
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.error("Error occurred with thread sleeping object -> {}, cause -> {}", this, e);
                    break;
                }
            }
        }
    }
}
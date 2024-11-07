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
import java.sql.*;

public class TelegramBot extends TelegramLongPollingBot {
    private static final String MENU = "This bot can get a school schedule" +
            "\nType /enableschedulenotifications to enable it" +
            "\nType /disableschedulenotifications to disable it";
    private static boolean isScheduleEnabled = false;
    private static final Logger logger = LoggerFactory.getLogger(TelegramBot.class);

    @Override
    public String getBotUsername() {
        return "javafirstproject_bot";
    }

    @Override
    public String getBotToken() {
        return "7420465438:AAFuhw9QBCl_1Rkuzshelbfw1r8OE5fdAu8";
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        String chatId = update.getMessage().getChatId().toString();
        String text = update.getMessage().getText();
        sendMessage.setChatId(chatId);
        
        try {
            if (text.equalsIgnoreCase("/disableScheduleNotifications")) {
                logger.info("Disabling schedule notifications");
                isScheduleEnabled = false;
                ScheduleCheck.updateUserDB(chatId, false);
                sendMessage.setText("Schedule notification update system is turned off!");
                this.execute(sendMessage);
                return;
            }

            if (text.equalsIgnoreCase("/enableScheduleNotifications")) {
                logger.info("Enabling schedule notifications");
                isScheduleEnabled = true;
                ScheduleCheck.updateUserDB(chatId, true);
                sendMessage.setText("Schedule notification update system is set!");
                this.execute(sendMessage);
                Thread thread = new Thread(new ScheduleCheck(this, chatId));
                thread.start();
                return;
            }

            sendMessage.setText(MENU);
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.error("Something went wrong : " + e);
        }
    }

    public void restoreUsers() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("PostgreSQL driver was not found -> {}", String.valueOf(e));
        }

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "3211");
            ResultSet rs = connection.createStatement().executeQuery("SELECT chatid, isschenabled FROM users"))
        {
            while (rs.next()) {
                String chId = rs.getString("chatid");
                boolean isEnabled = rs.getBoolean("isschenabled");
                if (isEnabled) {
                    isScheduleEnabled = true;
                    Thread thread = new Thread(new ScheduleCheck(this, chId));
                    thread.start();
                }
            }
        } catch (SQLException e) {
            logger.error("Something went wrong with connection to DB, cause -> {}", String.valueOf(e));
            e.printStackTrace();
        }
    }

    private static class ScheduleCheck implements Runnable {
        private static final URL SCHEDULE_LINK;

        static {
            try {
                SCHEDULE_LINK = new URL("https://j5vsk.lv/izmainas/ritdienai/izmainas.pdf");
            } catch (MalformedURLException e) {
                logger.error("FAILED TO INITIALIZE SCHEDULE_LINK, cause -> " + e);
                throw new RuntimeException(e);
            }
        }

        private final TelegramBot bot;
        private final String chatId;

        private ScheduleCheck(TelegramBot bot, String chatId) {
            this.bot = bot;
            this.chatId = chatId;
        }

        @Override
        public void run() {
            long lastRegistredModifiedDate = getLastRegistredModifiedDate();

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            while (true) {
                if (isScheduleEnabled) {
                    try {
                        HttpURLConnection connection = (HttpURLConnection) SCHEDULE_LINK.openConnection();
                        connection.setRequestMethod("HEAD");
                        long lastModified = connection.getLastModified();

                        if (lastModified > lastRegistredModifiedDate) { // true only for testing
                            lastRegistredModifiedDate = lastModified;
                            updateScheduleDB(lastModified);
                            sendMessage.setText("New schedule arrived!\n" + SCHEDULE_LINK);
                            bot.execute(sendMessage);
                            logger.info("Bot has sent a schedule link to users");
                        }
                        connection.disconnect();
                    } catch (Exception e) {
                        logger.error("Error occured, cause -> " + e);
                    }
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        logger.error("Error occured with thread sleeping object -> " + this + ", cause -> " + e);
                    }
                } else {
                    break;
                }
            }

        }

        private static long getLastRegistredModifiedDate() {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                logger.error("PostgreSQL driver was not found -> {}" , String.valueOf(e));
            }

            long toReturn = 0;

            try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "3211");
                 Statement statement = connection.createStatement();
                 ResultSet rs = statement.executeQuery("SELECT lastmodified from schedule"))
            {
                while (rs.next()) {
                    toReturn = rs.getLong("lastmodified");
                }
            } catch (SQLException e) {
                logger.error("Something went wrong with connection to DB, cause -> {}", String.valueOf(e));
            }

            return toReturn;
        }

        private static void updateUserDB(String chatId, boolean isScheduleEnabled) {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                logger.error("PostgreSQL driver was not found -> {}", String.valueOf(e));
            }
            String insertQuery = "INSERT INTO users (chatid, isschenabled) values (?, ?) ON CONFLICT (chatid) " +
                    "DO UPDATE SET isschenabled = EXCLUDED.isschenabled";

            try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "3211");
                PreparedStatement ps = connection.prepareStatement(insertQuery))
            {
                ps.setString(1, chatId);
                ps.setBoolean(2, isScheduleEnabled);
                ps.executeUpdate();
            } catch (SQLException e) {
                logger.error("Something went wrong with connection to DB, cause -> {}", String.valueOf(e));
                e.printStackTrace();
            }
        }

        private static void updateScheduleDB(long lastModified) {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                logger.error("PostgreSQL driver was not found -> {}", String.valueOf(e));
            }
            String insertQuery = "INSERT INTO schedule (lastmodified) values (?) ON CONFLICT (lastmodified) DO UPDATE SET lastmodified = EXCLUDED.lastmodified";

            try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "3211");
                 PreparedStatement ps = connection.prepareStatement(insertQuery)) {
                ps.setLong(1, lastModified);
                ps.executeUpdate();
            } catch (SQLException e) {
                logger.error("Something went wrong with connection to DB, cause -> {}", String.valueOf(e));
                e.printStackTrace();
            }
        }
    }
}

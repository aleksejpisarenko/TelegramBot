package org.example.TelegramBot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.sql.*;

public class Main {
    public static long lastRegistredModifiedDate = 0;
    private static Logger logger = LoggerFactory.getLogger(Main.class);
    static {
        getLastRegistredModifiedDate();
    }

    public static void main(String[] args) {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new TelegramBot());
        } catch (TelegramApiException e) {
            logger.error("Something went wrong in main method, cause -> {}", String.valueOf(e));
        }
    }

    private static void getLastRegistredModifiedDate() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("PostgreSQL driver was not found -> {}" , String.valueOf(e));
        }

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "3211");
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SELECT lastModified from schedule"))
        {
            while (rs.next()) {
                lastRegistredModifiedDate = rs.getLong("lastmodified");
                System.out.println(lastRegistredModifiedDate);
            }
        } catch (SQLException e) {
            logger.error("Something went wrong with connection to DB, cause -> {}", String.valueOf(e));
        }
    }
}
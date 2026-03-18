package org.example.TelegramBot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DatabaseService {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseService.class);
    private static final String databaseUser = ConfigManager.getDatabaseUsername();
    private static final String databasePass = ConfigManager.getDatabasePassword();
    private static final String databaseURL = ConfigManager.getDatabaseURL();

    static void updateScheduleDB(long lastModified) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("PostgreSQL driver was not found -> {}", String.valueOf(e));
        }
        String insertQuery = "INSERT INTO schedule (lastmodified) values (?) ON CONFLICT (lastmodified) DO UPDATE SET lastmodified = EXCLUDED.lastmodified";
        String deleteQuery = "DELETE FROM schedule";

        try (Connection connection = DriverManager.getConnection(databaseURL, databaseUser, databasePass);
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
             PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            deleteStatement.executeUpdate();
            insertStatement.setLong(1, lastModified);
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Something went wrong with connection to DB, cause -> {}", String.valueOf(e));
            e.printStackTrace();
        }
    }

    static long getLastRegisteredModifiedDate() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("PostgreSQL driver was not found -> {}", String.valueOf(e));
        }
        long toReturn = 0;

        try (Connection connection = DriverManager.getConnection(databaseURL, databaseUser, databasePass);
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SELECT lastmodified FROM schedule")) {
            while (rs.next()) {
                toReturn = rs.getLong("lastmodified");
            }
        } catch (SQLException e) {
            logger.error("Something went wrong with connection to DB, cause -> {}", String.valueOf(e));
        }

        return toReturn;
    }

    public static void updateUserDB(String chatId, boolean isScheduleEnabled) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("PostgreSQL driver was not found -> {}", String.valueOf(e));
        }
        String insertQuery = "INSERT INTO users (chatid, isschenabled) values (?, ?) ON CONFLICT (chatid) " +
                "DO UPDATE SET isschenabled = EXCLUDED.isschenabled";

        try (Connection connection = DriverManager.getConnection(databaseURL, databaseUser, databasePass);
             PreparedStatement ps = connection.prepareStatement(insertQuery)) {
            ps.setString(1, chatId);
            ps.setBoolean(2, isScheduleEnabled);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Something went wrong with connection to DB, cause -> {}", String.valueOf(e));
            e.printStackTrace();
        }
    }

    public static void restoreUsers(TelegramBot bot) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("PostgreSQL driver was not found -> {}", String.valueOf(e));
        }

        try (Connection connection = DriverManager.getConnection(databaseURL, databaseUser, databasePass);
             ResultSet rs = connection.createStatement().executeQuery("SELECT chatid, isschenabled FROM users")) {
            while (rs.next()) {
                String chatId = rs.getString("chatid");
                boolean isEnabled = rs.getBoolean("isschenabled");
                if (isEnabled) {
                    Thread thread = ThreadManager.createThread(chatId, new TelegramBot.ScheduleCheck(bot, chatId));

                    if (thread != null) {
                        thread.start();
                    } else {
                        System.out.println("Thread has already been created");
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Something went wrong with connection to DB, cause -> {}", String.valueOf(e));
            e.printStackTrace();
        }
    }
}

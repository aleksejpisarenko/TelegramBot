package org.example.TelegramBot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
    private static final Properties configProperties = new Properties();
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);

    static {
        try (FileInputStream fis = new FileInputStream("C:\\Users\\nemok\\IdeaProjects\\TelegramBot\\src\\main\\java\\org\\example\\TelegramBot\\Config.properties")) {
            configProperties.load(fis);
        } catch (IOException e) {
            logger.error("Couldn't load configurational file, ->{}", String.valueOf(e));
        }
    }

    public static String getDatabaseUsername() {
        logger.debug("Database username was received");
        return String.valueOf(configProperties.get("db_user"));
    }

    public static String getDatabasePassword() {
        logger.debug("Database password was received");
        return String.valueOf(configProperties.get("db_pass"));
    }

    public static String getBotToken() {
        logger.debug("Bot token was received");
        return String.valueOf(configProperties.get("bot_token"));
    }

    public static String getBotName() {
        logger.debug("Bot name was received");
        return String.valueOf(configProperties.get("bot_name"));
    }

    public static String getDatabaseURL() {
        logger.debug("Database url was received");
        return String.valueOf(configProperties.get("db_url"));
    }
}

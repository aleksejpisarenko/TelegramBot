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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TelegramBot extends TelegramLongPollingBot {
    private static final String MENU = "This bot can get a local time, my age,\nschool schedule, and some more secrets" +
            "\nType /getlocaltime to get it" +
            "\nType /getmyage to get it" +
            "\nType /enableschedulenotifications to enable it" +
            "\nType /disableschedulenotifications to disable it";
    private static boolean isScheduleEnabled = false;
    private static final Logger logger = LoggerFactory.getLogger(TelegramBot.class);

    @Override
    public String getBotUsername() {
        return "NAME";
    }

    @Override
    public String getBotToken() {
        return "TOKEN";
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        String chatId = update.getMessage().getChatId().toString();
        String text = update.getMessage().getText();
        sendMessage.setChatId(chatId);

        try {
            if (text.equalsIgnoreCase("Hi") || text.equalsIgnoreCase("Привет")) {
                logger.info("Someone said Hi");
                sendMessage.setText("Hello, how are you doing?");
                this.execute(sendMessage);
                return;
            }

            if (text.equalsIgnoreCase("Cycle")) {
                logger.info("Someone found out about secret Cycle!");
                for (int i = 0; i < 10; i++) {
                    sendMessage.setText(STR."\{i}");
                    this.execute(sendMessage);
                }
                return;
            }

            if (text.equalsIgnoreCase("Катя")) {
                sendMessage.setText("Катя is my sister");
                this.execute(sendMessage);
                return;
            }

            if (text.equalsIgnoreCase("Katya")) {
                sendMessage.setText("Katya is my sister");
                this.execute(sendMessage);
                return;
            }

            if (text.equalsIgnoreCase("Papa")) {
                sendMessage.setText("Papa is my father");
                this.execute(sendMessage);
                return;
            }

            if (text.equalsIgnoreCase("Папа")) {
                sendMessage.setText("Папа is my father");
                this.execute(sendMessage);
                return;
            }

            if (text.equalsIgnoreCase("Андрей")) {
                sendMessage.setText("Андрей is my father");
                this.execute(sendMessage);
                return;
            }

            if (text.equalsIgnoreCase("Andrew")) {
                sendMessage.setText("Andrew is my father");
                this.execute(sendMessage);
                return;
            }

            if (text.equalsIgnoreCase("Mama")) {
                sendMessage.setText("Mama is my mother");
                this.execute(sendMessage);
                return;
            }

            if (text.equalsIgnoreCase("Мама")) {
                sendMessage.setText("Мама is my mother");
                this.execute(sendMessage);
                return;
            }

            if (text.equalsIgnoreCase("Марианна")) {
                sendMessage.setText("Марианна is my mother");
                this.execute(sendMessage);
                return;
            }

            if (text.equalsIgnoreCase("Marianna")) {
                sendMessage.setText("Marianna is my mother");
                this.execute(sendMessage);
                return;
            }

            if (text.equalsIgnoreCase("Sergey")) {
                sendMessage.setText("Sergey is my big brother");
                this.execute(sendMessage);
                return;
            }

            if (text.equalsIgnoreCase("Сергей")) {
                sendMessage.setText("Сергей is my big brother");
                this.execute(sendMessage);
                return;
            }

            if (text.equalsIgnoreCase("Glamour")) {
                sendMessage.setText("Glamour is our youngest cat");
                this.execute(sendMessage);
                return;
            }

            if (text.equalsIgnoreCase("Гламур")) {
                sendMessage.setText("Гламур is our youngest cat");
                this.execute(sendMessage);
                return;
            }

            if (text.equalsIgnoreCase("Candy")) {
                sendMessage.setText("Candy is our oldest cat");
                this.execute(sendMessage);
                return;
            }

            if (text.equalsIgnoreCase("Кенди")) {
                sendMessage.setText("Кенди is our oldest cat");
                this.execute(sendMessage);
                return;
            }

            if (text.equalsIgnoreCase("Алексей")) {
                sendMessage.setText("Алексей made this bot");
                this.execute(sendMessage);
                return;
            }

            if (text.equalsIgnoreCase("Alexey")) {
                sendMessage.setText("Alexey made this bot");
                this.execute(sendMessage);
                return;
            }

            if (text.equalsIgnoreCase("/getlocaltime")) {
                logger.info("Someone got local time");
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy - HH:mm:ss");
                sendMessage.setText(STR."Local time is : \{LocalDateTime.now().format(dateTimeFormatter)} EEST");
                this.execute(sendMessage);
                return;
            }

            if (text.equalsIgnoreCase("/getmyage")) {
                logger.info("Someone got my age");
                LocalDateTime birthday = LocalDateTime.of(2008, 10, 7, 12, 0, 0);
                LocalDateTime current = LocalDateTime.now();
                int age = 0;

                if (current.getDayOfYear() >= birthday.getDayOfYear()) {
                    age += current.getYear() - birthday.getYear();
                    sendMessage.setText(STR."I am \{age} years old:)");
                    this.execute(sendMessage);
                    return;
                } else {
                    age += current.getYear() - birthday.getYear() - 1;
                    sendMessage.setText(STR."I am \{age} years old:)");
                    this.execute(sendMessage);
                    return;
                }
            }

            if (text.equalsIgnoreCase("/howtoalarm")) {
                sendMessage.setText("Choose command - /setalarm HH:mm:ss \n in HH:mm:ss place' you should put the time(24-h time format only)");
                this.execute(sendMessage);
                return;
            }

            if (text.contains("/setalarm")) {
                String line = text.substring(10);
                LocalTime time = LocalTime.parse(line);

                AlarmTask alarmTask = new AlarmTask(this, chatId, time);
                Thread thread = new Thread(alarmTask);
                thread.start();

                sendMessage.setText(STR."Alarm is set for \{time.toString()}");
                this.execute(sendMessage);
                return;
            }

            if (text.equalsIgnoreCase("/disableScheduleNotifications")) {
                logger.info("Disabling schedule notifications");
                isScheduleEnabled = false;
                sendMessage.setText("Current status of schedule notif. service is " + isScheduleEnabled);
                this.execute(sendMessage);
                return;
            }

            if (text.equalsIgnoreCase("/enableScheduleNotifications")) {
                logger.info("Enabling schedule notifications");
                isScheduleEnabled = true;
                sendMessage.setText("Schedule notification update system is set!");
                this.execute(sendMessage);
                Thread thread = new Thread(new ScheduleCheck(this, chatId));
                thread.start();
                return;
            }


            sendMessage.setText(MENU);
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println(STR."Something went wrong : \{e}");
        }
    }

    private static class ScheduleCheck implements Runnable {
        private static final URL SCHEDULE_LINK;

        static {
            try {
                SCHEDULE_LINK = new URL("https://j5vsk.lv/izmainas/ritdienai/izmainas.pdf");
            } catch (MalformedURLException e) {
                logger.error(STR."FAILED TO INITIALIZE SCHEDULE_LINK, cause -> \{e}");
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
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            while (true) {
                if (isScheduleEnabled) {
                    try {
                        HttpURLConnection connection = (HttpURLConnection) SCHEDULE_LINK.openConnection();
                        connection.setRequestMethod("HEAD");
                        long lastModified = connection.getLastModified();

                        if (lastModified > Main.lastRegistredModifiedDate) {
                            Main.lastRegistredModifiedDate = lastModified;
                            sendMessage.setText(STR."New schedule arrived!\n\{SCHEDULE_LINK}");
                            bot.execute(sendMessage);
                            logger.info("Bot has sent a schedule link to users");
                        }
                        connection.disconnect();
                    } catch (Exception e) {
                        logger.error(STR."Error occured, cause -> \{e}");
                    }
                    try {
                        Thread.sleep(3500);
                    } catch (InterruptedException e) {
                        logger.error(STR."Error occured with thread sleeping object -> \{this}, cause -> \{e}");
                    }
                } else {
                    break;
                }
            }

        }
    }

    private static class AlarmTask implements Runnable {
        private final TelegramBot bot;
        private final String chatId;
        private final LocalTime alarmTime;

        public AlarmTask(TelegramBot bot, String chatId, LocalTime alarmTime) {
            this.bot = bot;
            this.chatId = chatId;
            this.alarmTime = alarmTime;
        }

        @Override
        public void run() {
            while (true) {
                LocalTime curr = LocalTime.now();

                if (curr.getHour() == alarmTime.getHour() &&
                        curr.getMinute() == alarmTime.getMinute() &&
                        curr.getSecond() == alarmTime.getSecond()) {
                    try {
                        for (int z = 0; z < 3; z++) {
                            for (int i = 0; i < 15; i++) {
                                SendMessage sendMessage = new SendMessage();
                                sendMessage.setChatId(chatId);
                                sendMessage.setText(String.valueOf(i));
                                bot.execute(sendMessage);
                            }
                        }
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}



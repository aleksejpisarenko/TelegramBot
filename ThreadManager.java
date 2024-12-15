package org.example.TelegramBot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class ThreadManager {
    private static final ConcurrentHashMap<String, Thread> activeUserThreads= new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(ThreadManager.class);

    public static Thread createThread(String name, Runnable task) {
        synchronized (activeUserThreads) {
            if (activeUserThreads.containsKey(name)) {
                logger.info("Thread map does already contain: '" + name + "'");
                return null;
            }

            Thread thread = new Thread(task, name);
            activeUserThreads.put(name, thread);
            logger.info("Thread was successfully added to thread map: '" + name + "'");
            return thread;
        }
    }

    public static boolean removeThread(String name) {
        synchronized (activeUserThreads) {
            if (!activeUserThreads.containsKey(name)) {
                logger.info("Thread map does not contain: '" + name + "'");
                return false;
            }
            activeUserThreads.get(name).interrupt();
            logger.info("Thread '" + name + "' was successfully interrupted");
            activeUserThreads.remove(name);
            logger.info("Thread '" + name + "' was successfully removed");
            return true;
        }
    }
}

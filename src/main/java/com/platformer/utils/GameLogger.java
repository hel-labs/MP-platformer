package com.platformer.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GameLogger {

    private static final GameLogger INSTANCE = new GameLogger();
    private final List<String> log = new ArrayList<>();
    private static final DateTimeFormatter FMT
            = DateTimeFormatter.ofPattern("HH:mm:ss");

    private GameLogger() {
    }

    public static GameLogger get() {
        return INSTANCE;
    }

    public void info(String message) {
        String entry = "[INFO]  " + timestamp() + " " + message;
        log.add(entry);
        System.out.println(entry);
    }

    public void warn(String message) {
        String entry = "[WARN]  " + timestamp() + " " + message;
        log.add(entry);
        System.out.println(entry);
    }

    public void error(String message, Exception e) {
        String entry = "[ERROR] " + timestamp() + " " + message
                + (e != null ? ": " + e.getMessage() : "");
        log.add(entry);
        System.err.println(entry);
        if (e != null) {
            e.printStackTrace();
        }
    }

    public List<String> getLog() {
        return List.copyOf(log);
    }

    public void clear() {
        log.clear();
    }

    private String timestamp() {
        return LocalTime.now().format(FMT);
    }
}

package com.platformer.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class PlayerProfileManager {

    private static final String FILE_PATH = "player_profile.txt";
    private static String currentPlayerName;

    private PlayerProfileManager() {
    }

    public static synchronized String getCurrentPlayerName() {
        if (currentPlayerName == null) {
            currentPlayerName = readSavedName();
        }
        return currentPlayerName;
    }

    public static synchronized void setCurrentPlayerName(String name) {
        String sanitized = sanitizeName(name);
        currentPlayerName = sanitized;
        try {
            Files.writeString(Path.of(FILE_PATH), sanitized, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized boolean hasSavedProfile() {
        return Files.exists(Path.of(FILE_PATH));
    }

    private static String readSavedName() {
        try {
            if (!Files.exists(Path.of(FILE_PATH))) {
                return "PLAYER";
            }
            String saved = Files.readString(Path.of(FILE_PATH), StandardCharsets.UTF_8);
            return sanitizeName(saved);
        } catch (IOException e) {
            return "PLAYER";
        }
    }

    private static String sanitizeName(String raw) {
        if (raw == null) {
            return "PLAYER";
        }

        String cleaned = raw.replace('|', ' ').trim();
        if (cleaned.isEmpty()) {
            return "PLAYER";
        }

        if (cleaned.length() > 16) {
            return cleaned.substring(0, 16);
        }

        return cleaned;
    }
}

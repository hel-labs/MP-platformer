package com.platformer.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class PlayerProfileManager {

    private static final String FILE_PATH = "player_profile.dat";
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
        try (DataOutputStream dos = new DataOutputStream(
                new java.io.FileOutputStream(FILE_PATH))) {
            dos.writeUTF(sanitized);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized boolean hasSavedProfile() {
        return new java.io.File(FILE_PATH).exists();
    }

    private static String readSavedName() {
        try (DataInputStream dis = new DataInputStream(
                new java.io.FileInputStream(FILE_PATH))) {
            return sanitizeName(dis.readUTF());
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
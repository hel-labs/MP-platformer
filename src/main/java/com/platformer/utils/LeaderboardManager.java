package com.platformer.utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardManager {

    private static final String FILE_PATH = "leaderboard.txt";
    private static final int MAX_ENTRIES = 50;
    private static final List<ScoreEntry> entries = new ArrayList<>();
    private static boolean loaded = false;

    public record ScoreEntry(String playerName, double bestScore, long totalDurationSeconds, String lastPlayed)
            implements Comparable<ScoreEntry> {

        @Override
        public int compareTo(ScoreEntry other) {
            int byScore = Double.compare(other.bestScore, this.bestScore);
            if (byScore != 0) {
                return byScore;
            }

            int byDuration = Long.compare(other.totalDurationSeconds, this.totalDurationSeconds);
            if (byDuration != 0) {
                return byDuration;
            }

            return this.playerName.compareToIgnoreCase(other.playerName);
        }
    }

    public static synchronized void savePlayerProgress(String playerName, double score, long durationSeconds) {
        loadScores();

        String sanitizedName = sanitizeName(playerName);
        long safeDuration = Math.max(0, durationSeconds);
        double safeScore = Math.max(0, score);
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        int existingIndex = findPlayerIndex(sanitizedName);
        if (existingIndex >= 0) {
            ScoreEntry existing = entries.get(existingIndex);
            double bestScore = Math.max(existing.bestScore(), safeScore);
            long totalDuration = existing.totalDurationSeconds() + safeDuration;
            entries.set(existingIndex, new ScoreEntry(existing.playerName(), bestScore, totalDuration, date));
        } else {
            entries.add(new ScoreEntry(sanitizedName, safeScore, safeDuration, date));
        }

        sortAndSave();
    }

    public static synchronized List<ScoreEntry> getEntries() {
        loadScores();
        return new ArrayList<>(entries);
    }

    private static void loadScores() {
        if (loaded) {
            return;
        }

        loaded = true;
        entries.clear();

        try {
            List<String> lines = Files.readAllLines(Path.of(FILE_PATH));
            int legacyIndex = 1;

            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|");

                if (parts.length == 4) {
                    entries.add(new ScoreEntry(
                            sanitizeName(parts[0]),
                            Double.parseDouble(parts[1]),
                            Long.parseLong(parts[2]),
                            parts[3]));
                } else if (parts.length == 3) {
                    // Backward compatibility with old score-only format.
                    entries.add(new ScoreEntry(
                            "LEGACY-" + legacyIndex++,
                            Double.parseDouble(parts[0]),
                            Long.parseLong(parts[1]),
                            parts[2]));
                }
            }

            entries.sort(null);
            if (entries.size() > MAX_ENTRIES) {
                entries.subList(MAX_ENTRIES, entries.size()).clear();
            }
        } catch (Exception ignored) {
        }
    }

    private static void sortAndSave() {
        entries.sort(null);
        if (entries.size() > MAX_ENTRIES) {
            entries.subList(MAX_ENTRIES, entries.size()).clear();
        }

        try (PrintWriter writer = new PrintWriter(FILE_PATH, "UTF-8")) {
            for (ScoreEntry e : entries) {
                writer.printf("%s|%.2f|%d|%s%n", e.playerName(), e.bestScore(), e.totalDurationSeconds(), e.lastPlayed());
            }
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static int findPlayerIndex(String playerName) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).playerName().equalsIgnoreCase(playerName)) {
                return i;
            }
        }
        return -1;
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

    public static String formatDuration(long seconds) {
        long h = seconds / 3600;
        long m = (seconds % 3600) / 60;
        long s = seconds % 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }
}

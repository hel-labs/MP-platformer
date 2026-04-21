package com.platformer.utils;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class LeaderboardManager {

    private static final String FILE_PATH = "leaderboard.txt";
    private static final int MAX_ENTRIES = 50;
    private static List<ScoreEntry> entries = new ArrayList<>();

    public record ScoreEntry(double score, long durationSeconds, String date) implements Comparable<ScoreEntry> {
        @Override
        public int compareTo(ScoreEntry o

        ) {
            return Double.compare(o.score, this.score);
        }
    }

    public static void saveScore(double score, long durationSeconds) {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        entries.add(new ScoreEntry(score, durationSeconds, date));
        sortAndSave();
    }

    public static List<ScoreEntry> getEntries() {
        loadScores();
        return entries;
    }

    private static void loadScores() {
        if (!entries.isEmpty()) {
            return;
        }
        try {
            List<String> lines = Files.readAllLines(Path.of(FILE_PATH));
            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    entries.add(new ScoreEntry(
                            Double.parseDouble(parts[0]),
                            Long.parseLong(parts[1]),
                            parts[2]));
                }
            }
            entries.sort(null);
            if (entries.size() > MAX_ENTRIES) {
                entries = entries.subList(0, MAX_ENTRIES);
            }
        } catch (IOException e) {

        }
    }

    private static void sortAndSave() {
        entries.sort(null);
        if (entries.size() > MAX_ENTRIES) {
            entries = entries.subList(0, MAX_ENTRIES);
        }

        try (PrintWriter writer = new PrintWriter(FILE_PATH, "UTF-8")) {
            for (ScoreEntry e : entries) {
                writer.printf("%.2f|%d|%s%n", e.score(), e.durationSeconds(), e.date());
            }
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static String formatDuration(long seconds) {
        long h = seconds / 3600;
        long m = (seconds % 3600) / 60;
        long s = seconds % 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }
}

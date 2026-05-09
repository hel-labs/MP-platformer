package com.platformer.core;

/**
 * Application entry point for MP-Platformer.
 */
public class MainClass {

    /**
     * Starts the game with default JVM settings.
     *
     * @param args unused CLI arguments
     */
    public static void main(String[] args) {

        System.setProperty("sun.java2d.uiScale", "1");
        System.setProperty("sun.java2d.dpiaware", "true");
        new Game();

    }

}

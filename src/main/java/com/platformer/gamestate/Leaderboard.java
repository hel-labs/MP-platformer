package com.platformer.gamestate;

import com.platformer.core.Game;
import com.platformer.input.*;
import com.platformer.utils.LeaderboardManager;
import com.platformer.utils.LeaderboardManager.ScoreEntry;
import java.awt.*;
import java.util.List;

public class Leaderboard extends State implements Statemethods {

    private static final String BACK_TEXT = "Press ESC to go back";

    public Leaderboard(Game game) {
        super(game);
    }

    @Override
    public void update() {
        InputHandler input = game.getInputHandler();
        if (input.isJustPressed(InputHandler.ESCAPE)) {
            Gamestate.state = Gamestate.MENU;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(30, 30, 40));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        Font titleFont = new Font("Arial", Font.BOLD, (int) (28 * Game.SCALE));
        Font rowFont = new Font("Arial", Font.PLAIN, (int) (18 * Game.SCALE));
        Font backFont = new Font("Arial", Font.PLAIN, (int) (16 * Game.SCALE));

        g.setFont(titleFont);
        g.setColor(Color.WHITE);
        g.drawString("LEADERBOARD", (Game.GAME_WIDTH - g.getFontMetrics().stringWidth("LEADERBOARD")) / 2,
                (int) (60 * Game.SCALE));

        List<ScoreEntry> scores = LeaderboardManager.getEntries();
        g.setFont(rowFont);
        int y = (int) (100 * Game.SCALE);

        g.setColor(Color.LIGHT_GRAY);
        g.drawString("#", (int) (40 * Game.SCALE), y);
        g.drawString("PLAYER", (int) (80 * Game.SCALE), y);
        g.drawString("BEST PTS", (int) (260 * Game.SCALE), y);
        g.drawString("TIME PLAYED", (int) (390 * Game.SCALE), y);
        g.drawString("LAST SEEN", (int) (560 * Game.SCALE), y);
        y += (int) (20 * Game.SCALE);

        g.setColor(Color.WHITE);
        for (int i = 0; i < Math.min(scores.size(), 15); i++) {
            ScoreEntry e = scores.get(i);
            g.drawString((i + 1) + ".", (int) (40 * Game.SCALE), y);
            g.drawString(e.playerName(), (int) (80 * Game.SCALE), y);
            g.drawString(String.format("%.0f", e.bestScore()), (int) (280 * Game.SCALE), y);
            g.drawString(LeaderboardManager.formatDuration(e.totalDurationSeconds()), (int) (390 * Game.SCALE), y);
            g.drawString(e.lastPlayed(), (int) (560 * Game.SCALE), y);
            y += (int) (22 * Game.SCALE);
        }

        if (scores.isEmpty()) {
            g.drawString("No runs recorded yet.", Game.GAME_WIDTH / 3, y);
        }

        g.setFont(backFont);
        g.setColor(Color.GRAY);
        g.drawString(BACK_TEXT, (Game.GAME_WIDTH - g.getFontMetrics().stringWidth(BACK_TEXT)) / 2,
                Game.GAME_HEIGHT - (int) (30 * Game.SCALE));
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseClicked'");
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mousePressed'");
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseReleased'");
    }

    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseMoved'");
    }
}
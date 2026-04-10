package com.echoes.core;

import com.echoes.entities.Player;
import com.echoes.entities.enemies.WanderingSlime;
import com.echoes.input.InputHandler;
import com.echoes.utils.GameLogger;

import javax.swing.*;
import java.awt.*;

/**
 * Standalone battle engine test runner.
 *
 * Launches a Swing window running only the battle system.
 * No platformer, no rooms, no tiles — just a battle against
 * a WanderingSlime so you can verify every system works
 * before integrating with the overworld.
 *
 * Controls:
 *   Arrow keys  — navigate action menu
 *   Z           — confirm / advance dialogue
 *   X           — skip dialogue to end (same as Z for now)
 *   R           — restart battle (after it ends)
 *   Escape      — quit
 *
 * Run with:
 *   ./gradlew run
 *
 * Or build and run:
 *   ./gradlew jar
 *   java -jar build/libs/EchoesOfTheUnderground-1.0.jar
 */
public class BattleTest extends JPanel implements Runnable {

    // Window / loop config
    public  static final int   WIDTH      = 800;
    public  static final int   HEIGHT     = 480;
    private static final int   TARGET_FPS = 60;
    private static final double NS_PER_FRAME =
        1_000_000_000.0 / TARGET_FPS;

    // Core systems
    private final InputHandler input = new InputHandler();
    private Thread   gameThread;

    // Battle state — recreated on restart
    private BattleState battleState;
    private boolean     battleOver = false;
    private boolean     restartRequested = false;

    // ---------------------------------------------------------------
    // Setup
    // ---------------------------------------------------------------

    public BattleTest() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);

        // Install key bindings — works without focus management.
        // WHEN_IN_FOCUSED_WINDOW means keys fire as long as the
        // window is in the foreground, regardless of which component
        // inside the window holds focus.
        input.install(this);

        startNewBattle();
    }

    private void startNewBattle() {
        Player        player = new Player();
        WanderingSlime slime  = new WanderingSlime(200f, 100f);

        battleState = new BattleState(player, slime);
        battleState.setOnBattleEnd(() -> {
            battleOver = true;
            GameLogger.get().info("Battle ended — press R to restart.");
        });
        battleState.onEnter();

        battleOver       = false;
        restartRequested = false;

        GameLogger.get().info("New battle started.");
    }

    // ---------------------------------------------------------------
    // Game loop
    // ---------------------------------------------------------------

    public void start() {
        gameThread = new Thread(this);
        gameThread.setDaemon(true);
        gameThread.start();
    }

    @Override
    public void run() {
        long   lastTime = System.nanoTime();
        double delta    = 0;

        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / NS_PER_FRAME;
            lastTime = now;

            if (delta >= 1) {
                float dt = (float)(1.0 / TARGET_FPS);
                tick(dt);
                repaint();
                delta--;
            }

            try { Thread.sleep(1); } catch (InterruptedException ignored) {}
        }
    }

    // ---------------------------------------------------------------
    // Tick — update logic
    // ---------------------------------------------------------------

    private void tick(float dt) {
        // 1. Clear just-pressed flags from last frame (MUST be first)
        input.tick();

        // 2. Global controls — work regardless of battle state
        if (input.isJustPressed(java.awt.event.KeyEvent.VK_ESCAPE)) {
            System.exit(0);
        }
        if (input.isJustPressed(java.awt.event.KeyEvent.VK_R)) {
            startNewBattle();
            return;
        }

        // 3. Route input and update to active state
        if (!battleOver) {
            battleState.handleInput(input);
            battleState.update(dt);
        } else {
            // Show "press R to restart" — input handled above
        }
    }

    // ---------------------------------------------------------------
    // Render
    // ---------------------------------------------------------------

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Smooth text
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        if (!battleOver) {
            battleState.render(g2);
        } else {
            renderBattleOverScreen(g2);
        }

        // Debug overlay — remove before submission
        renderDebugOverlay(g2);
    }

    private void renderBattleOverScreen(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 20));
        String msg = "Battle Over";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(msg, (WIDTH - fm.stringWidth(msg)) / 2, HEIGHT / 2 - 20);

        g.setFont(new Font("Monospaced", Font.PLAIN, 13));
        String sub = "Press R to restart  |  Escape to quit";
        fm = g.getFontMetrics();
        g.drawString(sub, (WIDTH - fm.stringWidth(sub)) / 2, HEIGHT / 2 + 16);
    }

    private void renderDebugOverlay(Graphics2D g) {
        g.setFont(new Font("Monospaced", Font.PLAIN, 10));
        g.setColor(new Color(100, 255, 100, 180));
        g.drawString("DEBUG | Z:confirm  Arrows:navigate  R:restart  Esc:quit",
                     8, HEIGHT - 8);
    }

    // ---------------------------------------------------------------
    // Entry point
    // ---------------------------------------------------------------

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Echoes — Battle Engine Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            BattleTest panel = new BattleTest();
            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            // No requestFocusInWindow() needed —
            // KeyBindings with WHEN_IN_FOCUSED_WINDOW don't require
            // the panel to hold focus, only the window to be active.

            panel.start();

            GameLogger.get().info("Battle engine standalone runner started.");
        });
    }
}
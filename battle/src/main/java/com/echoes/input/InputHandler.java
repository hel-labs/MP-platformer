package com.echoes.input;

import java.awt.event.KeyEvent;
import java.util.Arrays;

/**
 * Tracks keyboard state across three arrays:
 *   held         — true while the key is physically held down
 *   justPressed  — true only on the first tick after a key is pressed
 *   justReleased — true only on the first tick after a key is released
 *
 * HOW TO REGISTER (use KeyBindings, NOT KeyListener):
 *
 *   InputHandler input = new InputHandler();
 *   input.install(panel);   // that's it — no focus required
 *
 * Why KeyBindings instead of KeyListener?
 *   KeyListener only fires when the component has keyboard focus.
 *   Focus is fragile — it can be stolen by the OS, IDE, or other
 *   components. KeyBindings with WHEN_IN_FOCUSED_WINDOW work as long
 *   as the window is in the foreground, regardless of which component
 *   holds focus inside the window.
 *
 * Thread safety:
 *   Swing fires key actions on the EDT.
 *   tick() and isJustPressed() are called from the game thread.
 *   justPressed uses a separate pending array written by the EDT
 *   and consumed atomically by tick() to prevent race conditions.
 *
 * Call tick() at the START of every game loop iteration.
 */
public class InputHandler {

    private static final int KEY_COUNT = 256;

    // Written only by game thread (tick + isX methods)
    private final boolean[] held         = new boolean[KEY_COUNT];
    private final boolean[] justPressed  = new boolean[KEY_COUNT];
    private final boolean[] justReleased = new boolean[KEY_COUNT];

    // Written by EDT, consumed by tick() — avoids clearing before read
    private final boolean[] pendingPressed  = new boolean[KEY_COUNT];
    private final boolean[] pendingReleased = new boolean[KEY_COUNT];

    // ---------------------------------------------------------------
    // Installation — call once after panel is created
    // ---------------------------------------------------------------

    /**
     * Installs key bindings on the given panel.
     * Works even if the panel does not have keyboard focus,
     * as long as the window is focused (WHEN_IN_FOCUSED_WINDOW).
     */
    public void install(javax.swing.JComponent panel) {
        // Keys we care about
        int[] keys = {
            KeyEvent.VK_Z, KeyEvent.VK_X,
            KeyEvent.VK_UP, KeyEvent.VK_DOWN,
            KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
            KeyEvent.VK_R, KeyEvent.VK_ESCAPE
        };

        javax.swing.InputMap  im = panel.getInputMap(
            javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        javax.swing.ActionMap am = panel.getActionMap();

        for (int key : keys) {
            // Pressed binding (onKeyDown=true)
            String pressName   = "press_"   + key;
            String releaseName = "release_" + key;

            im.put(javax.swing.KeyStroke.getKeyStroke(key, 0, false), pressName);
            im.put(javax.swing.KeyStroke.getKeyStroke(key, 0, true),  releaseName);

            final int k = key;
            am.put(pressName,   new javax.swing.AbstractAction() {
                @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                    onKeyDown(k);
                }
            });
            am.put(releaseName, new javax.swing.AbstractAction() {
                @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                    onKeyUp(k);
                }
            });
        }
    }

    // ---------------------------------------------------------------
    // Called by key bindings on the EDT
    // ---------------------------------------------------------------

    private synchronized void onKeyDown(int code) {
        if (code < 0 || code >= KEY_COUNT) return;
        if (!held[code]) pendingPressed[code] = true;
        held[code] = true;
    }

    private synchronized void onKeyUp(int code) {
        if (code < 0 || code >= KEY_COUNT) return;
        held[code]             = false;
        pendingReleased[code]  = true;
    }

    // ---------------------------------------------------------------
    // Frame tick — call ONCE at START of each game loop tick
    // ---------------------------------------------------------------

    public synchronized void tick() {
        // Consume pending arrays into justPressed/justReleased
        System.arraycopy(pendingPressed,  0, justPressed,  0, KEY_COUNT);
        System.arraycopy(pendingReleased, 0, justReleased, 0, KEY_COUNT);
        Arrays.fill(pendingPressed,  false);
        Arrays.fill(pendingReleased, false);
    }

    // ---------------------------------------------------------------
    // Query methods — call AFTER tick()
    // ---------------------------------------------------------------

    /** True while the key is physically held down. */
    public synchronized boolean isHeld(int keyCode) {
        if (keyCode < 0 || keyCode >= KEY_COUNT) return false;
        return held[keyCode];
    }

    /** True only on the first tick after the key was pressed. */
    public synchronized boolean isJustPressed(int keyCode) {
        if (keyCode < 0 || keyCode >= KEY_COUNT) return false;
        return justPressed[keyCode];
    }

    /** True only on the first tick after the key was released. */
    public synchronized boolean isJustReleased(int keyCode) {
        if (keyCode < 0 || keyCode >= KEY_COUNT) return false;
        return justReleased[keyCode];
    }

    // ---------------------------------------------------------------
    // Convenience constants
    // ---------------------------------------------------------------

    public static final int CONFIRM = KeyEvent.VK_Z;
    public static final int CANCEL  = KeyEvent.VK_X;
    public static final int UP      = KeyEvent.VK_UP;
    public static final int DOWN    = KeyEvent.VK_DOWN;
    public static final int LEFT    = KeyEvent.VK_LEFT;
    public static final int RIGHT   = KeyEvent.VK_RIGHT;
    public static final int ESCAPE  = KeyEvent.VK_ESCAPE;
}


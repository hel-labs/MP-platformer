package com.platformer.input;

import java.awt.event.KeyEvent;
import java.util.Arrays;

/**
 * Central keyboard input tracker with held/pressed/released states.
 */
public class InputHandler {

    private static final int KEY_COUNT = 256;

    private final boolean[] held = new boolean[KEY_COUNT];
    private final boolean[] justPressed = new boolean[KEY_COUNT];
    private final boolean[] justReleased = new boolean[KEY_COUNT];

    private final boolean[] pendingPressed = new boolean[KEY_COUNT];
    private final boolean[] pendingReleased = new boolean[KEY_COUNT];

    /**
     * Installs key bindings on the provided Swing component.
     *
     * @param panel target Swing component
     */
    public void install(javax.swing.JComponent panel) {

        int[] keys = {
            KeyEvent.VK_Z, KeyEvent.VK_X,
            KeyEvent.VK_UP, KeyEvent.VK_DOWN,
            KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
            KeyEvent.VK_A, KeyEvent.VK_D,
            KeyEvent.VK_W, KeyEvent.VK_S,
            KeyEvent.VK_SPACE,
            KeyEvent.VK_ESCAPE,
            KeyEvent.VK_F11,
            KeyEvent.VK_ENTER
        };

        javax.swing.InputMap im = panel.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        javax.swing.ActionMap am = panel.getActionMap();

        for (int key : keys) {

            String pressName = "press_" + key;
            String releaseName = "release_" + key;

            im.put(javax.swing.KeyStroke.getKeyStroke(key, 0, false), pressName);
            im.put(javax.swing.KeyStroke.getKeyStroke(key, 0, true), releaseName);

            final int k = key;

            am.put(pressName, new javax.swing.AbstractAction() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    onKeyDown(k);
                }
            });

            am.put(releaseName, new javax.swing.AbstractAction() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    onKeyUp(k);
                }
            });
        }
    }

    private synchronized void onKeyDown(int code) {
        if (code < 0 || code >= KEY_COUNT) {
            return;
        }
        if (!held[code]) {
            pendingPressed[code] = true;
        }
        held[code] = true;
    }

    private synchronized void onKeyUp(int code) {
        if (code < 0 || code >= KEY_COUNT) {
            return;
        }
        held[code] = false;
        pendingReleased[code] = true;
    }

    /**
     * Commits pending key transitions for the current frame.
     */
    public synchronized void tick() {
        System.arraycopy(pendingPressed, 0, justPressed, 0, KEY_COUNT);
        System.arraycopy(pendingReleased, 0, justReleased, 0, KEY_COUNT);
        Arrays.fill(pendingPressed, false);
        Arrays.fill(pendingReleased, false);
    }

    /**
     * @param keyCode KeyEvent code
     * @return true if the key is currently held
     */
    public synchronized boolean isHeld(int keyCode) {
        return keyCode >= 0 && keyCode < KEY_COUNT && held[keyCode];
    }

    /**
     * @param keyCode KeyEvent code
     * @return true if the key was pressed this frame
     */
    public synchronized boolean isJustPressed(int keyCode) {
        return keyCode >= 0 && keyCode < KEY_COUNT && justPressed[keyCode];
    }

    /**
     * @param keyCode KeyEvent code
     * @return true if the key was released this frame
     */
    public synchronized boolean isJustReleased(int keyCode) {
        return keyCode >= 0 && keyCode < KEY_COUNT && justReleased[keyCode];
    }

    /** Confirm action key (Z). */
    public static final int CONFIRM = KeyEvent.VK_Z;
    /** Cancel action key (X). */
    public static final int CANCEL = KeyEvent.VK_X;
    /** Arrow up key. */
    public static final int UP = KeyEvent.VK_UP;
    /** Arrow down key. */
    public static final int DOWN = KeyEvent.VK_DOWN;
    /** Arrow left key. */
    public static final int LEFT = KeyEvent.VK_LEFT;
    /** Arrow right key. */
    public static final int RIGHT = KeyEvent.VK_RIGHT;
    /** Jump key (space). */
    public static final int JUMP = KeyEvent.VK_SPACE;
    /** Escape key. */
    public static final int ESCAPE = KeyEvent.VK_ESCAPE;
    /** Alternate up key (W). */
    public static final int UP_W = KeyEvent.VK_W;
    /** Alternate down key (S). */
    public static final int DOWN_S = KeyEvent.VK_S;
    /** Alternate left key (A). */
    public static final int LEFT_A = KeyEvent.VK_A;
    /** Alternate right key (D). */
    public static final int RIGHT_D = KeyEvent.VK_D;
    /** Enter key. */
    public static final int ENTER = KeyEvent.VK_ENTER;
}

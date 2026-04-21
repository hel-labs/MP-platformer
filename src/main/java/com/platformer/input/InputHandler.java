package com.platformer.input;

import java.awt.event.KeyEvent;
import java.util.Arrays;

public class InputHandler {

    private static final int KEY_COUNT = 256;

    private final boolean[] held = new boolean[KEY_COUNT];
    private final boolean[] justPressed = new boolean[KEY_COUNT];
    private final boolean[] justReleased = new boolean[KEY_COUNT];

    private final boolean[] pendingPressed = new boolean[KEY_COUNT];
    private final boolean[] pendingReleased = new boolean[KEY_COUNT];

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

    public synchronized void tick() {
        System.arraycopy(pendingPressed, 0, justPressed, 0, KEY_COUNT);
        System.arraycopy(pendingReleased, 0, justReleased, 0, KEY_COUNT);
        Arrays.fill(pendingPressed, false);
        Arrays.fill(pendingReleased, false);
    }

    public synchronized boolean isHeld(int keyCode) {
        return keyCode >= 0 && keyCode < KEY_COUNT && held[keyCode];
    }

    public synchronized boolean isJustPressed(int keyCode) {
        return keyCode >= 0 && keyCode < KEY_COUNT && justPressed[keyCode];
    }

    public synchronized boolean isJustReleased(int keyCode) {
        return keyCode >= 0 && keyCode < KEY_COUNT && justReleased[keyCode];
    }

    public static final int CONFIRM = KeyEvent.VK_Z;
    public static final int CANCEL = KeyEvent.VK_X;
    public static final int UP = KeyEvent.VK_UP;
    public static final int DOWN = KeyEvent.VK_DOWN;
    public static final int LEFT = KeyEvent.VK_LEFT;
    public static final int RIGHT = KeyEvent.VK_RIGHT;
    public static final int JUMP = KeyEvent.VK_SPACE;
    public static final int ESCAPE = KeyEvent.VK_ESCAPE;
    public static final int UP_W = KeyEvent.VK_W;
    public static final int DOWN_S = KeyEvent.VK_S;
    public static final int LEFT_A = KeyEvent.VK_A;
    public static final int RIGHT_D = KeyEvent.VK_D;
    public static final int ENTER = KeyEvent.VK_ENTER;
}

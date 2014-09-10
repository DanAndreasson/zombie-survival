package com.codescrew.zombiesurvival.handlers;

public class BBInput {

    public static int x;
    public static int y;
    public static boolean down;
    public static boolean pdown;

    public static boolean[] keys;
    public static boolean[] pkeys;
    private static final int NUM_KEYS = 2;
    public static final int BUTTON1 = 0;
    public static final int BUTTON2 = 1;

    static {
        keys = new boolean[NUM_KEYS];
        pkeys = new boolean[NUM_KEYS];
    }

    public static void update() {
        pdown = down;
        System.arraycopy(keys, 0, pkeys, 0, NUM_KEYS);
    }

    public static boolean isPressed() { return down && !pdown; }

    public static void setKey(int i, boolean b) { keys[i] = b; }

}


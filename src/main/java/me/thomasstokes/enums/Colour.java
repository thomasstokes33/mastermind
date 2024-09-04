package me.thomasstokes.enums;

import java.util.Random;

public enum Colour {
    RED,
    BLUE,
    GREEN,
    YELLOW,
    BROWN,
    PURPLE,
    BLACK,
    NONE;
    public static Colour []VALUES = values();
    private static Random RANDOM = new Random();

    public static Colour randomColour() {
        int length = VALUES.length;
        return VALUES[RANDOM.nextInt(length)];
    }

}

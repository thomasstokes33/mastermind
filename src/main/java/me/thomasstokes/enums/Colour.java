package me.thomasstokes.enums;

import java.util.Random;

public enum Colour {
    RED,
    BLUE,
    GREEN,
    YELLOW,
    BROWN,
    BLACK,
    WHITE,
    NONE;
    public final static Colour []VALUES = values();
    public final static Colour []VALUES_WITHOUT_NONE = setupValuesWithoutNone();
    private final static Random RANDOM = new Random();
    public static final Colour CORRECT_COLOUR_CORRECT_POSITION_RESULT_COLOUR = Colour.BLACK;
    public static final Colour CORRECT_COLOUR_WRONG_POSITION_RESULT_COLOUR = Colour.WHITE;
    public final static Colour HIDDEN_PIN_COLOUR = Colour.BROWN;

    public static Colour[] setupValuesWithoutNone() {
        Colour []allValues = values();
        Colour []valuesExcludingNone = new Colour[allValues.length - 1];
        int position = 0;
        for (Colour colour : allValues) {
            if (colour != Colour.NONE) {
                valuesExcludingNone[position++] = colour;
            }
        }
        return valuesExcludingNone;
    }
    /**
     * 
     * @return Returns random colour excluding None.
     */
    public static Colour randomColour() {
        int length = VALUES_WITHOUT_NONE.length;
        return VALUES_WITHOUT_NONE[RANDOM.nextInt(length)];
    }
    
    public String toString() {
        return super.toString().toLowerCase();
    }

}

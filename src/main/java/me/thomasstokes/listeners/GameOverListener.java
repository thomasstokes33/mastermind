package me.thomasstokes.listeners;

import java.util.List;

import me.thomasstokes.enums.Colour;

/**
 * Used by the Game to notify listeners that the game is over.
 */
public interface GameOverListener {
    public void gameOver(boolean isVictory, List<Colour> theHiddenAnswer);
}

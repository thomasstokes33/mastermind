package me.thomasstokes.listeners;

import java.util.List;

import me.thomasstokes.enums.Colour;

public interface GameOverListener {
    public void gameOver(boolean isVictory, List<Colour> theHiddenAnswer);
}

package me.thomasstokes.listeners;

import java.util.List;

import me.thomasstokes.enums.Colour;

/**
 * Used to indicate to a listener that a guess has been locked in.
 */
public interface GuessConfirmedListener {
    void guessConfirmed(List<Colour> theConfirmedGuess);
}

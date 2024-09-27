package me.thomasstokes.listeners;

import java.util.List;

import me.thomasstokes.enums.Colour;

/**
 * Used to notify a listener that a guess has been confirmed in a GuessRow.
 */
public interface GuessConfirmedListener {
    void guessConfirmed(List<Colour> theConfirmedGuess);
}

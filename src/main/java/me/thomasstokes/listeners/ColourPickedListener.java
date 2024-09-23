package me.thomasstokes.listeners;

import me.thomasstokes.enums.Colour;

/**
 * Used to notify that a colour has been picked.
 */
public interface ColourPickedListener {

    void colourPicked(Colour colour);
}

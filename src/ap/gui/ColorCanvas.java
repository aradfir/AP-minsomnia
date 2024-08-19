package ap.gui;

import java.awt.*;

public enum ColorCanvas {
    PRIMARY(new Color(46,47,43)),
    SECONDARY(new Color(105, 94, 184)),
    TERTIARY(Color.white),
    SECONDARY_TEXT_COLOR(new Color(239,238,247)),
    PRIMARY_TEXT_COLOR(new Color(153,153,153)),
    TERTIARY_TEXT_COLOR(new Color(102,102,102));


    private Color associatedColor;

    ColorCanvas(Color associatedColor) {
        this.associatedColor = associatedColor;
    }

    void setColor(Color newColor) {
        this.associatedColor = newColor;
    }
    Color getColor()
    {
        return this.associatedColor;
    }
}

package ap.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The type Cool button, borderless with color change on hover.
 */
public class CoolButton  extends JButton {
    /**
     * Instantiates a new Cool button.
     *
     * @param text the text inside the button
     */
    public CoolButton(String text) {
        super(text);
        this.setBorderPainted(false);
        this.addMouseListener(new MouseHoverChecker());
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setFocusable(false);
    }

    /**
     * checks for mouse hover.
     */
    public class MouseHoverChecker extends MouseAdapter {
        /**
         * The Color factor, what to mult by.
         */
        private double colorFactor=0.9;

        /**
         * multiply colors.
         *
         * @param input  the input
         * @param factor the factor to multiply by
         * @return the color result
         */
        public Color colorMult(Color input,double factor)
        {
            return new Color(Math.max((int)(input.getRed()  *factor), 0),
                    Math.max((int)(input.getGreen()*factor), 0),
                    Math.max((int)(input.getBlue() *factor), 0),
                    input.getAlpha());
        }
        @Override
        public void mouseEntered(MouseEvent e) {
            e.getComponent().setBackground(colorMult( e.getComponent().getBackground(),colorFactor));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            e.getComponent().setBackground(colorMult( e.getComponent().getBackground(),1/colorFactor));
        }
    }

}

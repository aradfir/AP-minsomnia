package ap.gui;

import javax.swing.*;
import java.awt.*;

/**
 * The type Paneled label.
 */
public class PaneledLabel extends JPanel {
    private JLabel label;

    /**
     * Instantiates a new Paneled label, not affected by theme colors.
     *
     * @param text      the text of the panel
     * @param backColor the back color of panel and label
     * @param textColor the text color of label
     */
    public PaneledLabel(String text, Color backColor,Color textColor) {
        super();
        this.label=new JLabel(text);
        label.setBackground(backColor);
        label.setForeground(textColor);
        this.add(label);
        this.setBackground(backColor);

        this.setVisible(true);

    }
}

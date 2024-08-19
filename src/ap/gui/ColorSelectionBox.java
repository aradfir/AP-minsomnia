package ap.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The type Color selection box, to launch a JColorChooser.
 */
public class ColorSelectionBox extends JPanel {
    private  JPanel previewColor;

    /**
     * Instantiates a new Color selection box.
     *
     * @param textLabel     the text of the label on the left
     * @param buttonText    the text on the button
     * @param initialColor  the initial color to show for the preview
     * @param colorManager  the color manager to paint the components inside this
     * @param changingColor the color this box is responsible for changing
     */
    public ColorSelectionBox(String textLabel, String buttonText, Color initialColor,ColorManager colorManager,ColorCanvas changingColor) {

        JLabel text=new JLabel(textLabel);
        JButton button=new JButton(buttonText);
        colorManager.colorComponent(text,ColorCanvas.PRIMARY,ColorCanvas.SECONDARY_TEXT_COLOR);
        colorManager.colorComponent(this,ColorCanvas.PRIMARY,ColorCanvas.PRIMARY_TEXT_COLOR);
        colorManager.colorComponent(button,ColorCanvas.SECONDARY,ColorCanvas.SECONDARY_TEXT_COLOR);
        previewColor=new JPanel();
        previewColor.setBackground(initialColor);
        previewColor.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
        JPanel previewAndButton=new JPanel();
        colorManager.colorComponent(previewAndButton,ColorCanvas.PRIMARY,ColorCanvas.PRIMARY_TEXT_COLOR);
        previewAndButton.add(previewColor);
        previewAndButton.add(button);
        previewColor.setMaximumSize(new Dimension(20,20));
        previewColor.setPreferredSize(new Dimension(20,20));
        previewColor.setLocation(20,20);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color jc=JColorChooser.showDialog(null,"SelectAColor",previewColor.getBackground(),false);

                switch (changingColor)
                {

                    case PRIMARY:
                        colorManager.setPrimaryBackgroundColor(jc);
                        break;
                    case SECONDARY:
                        colorManager.setSecondaryBackgroundColor(jc);
                        break;
                    case TERTIARY:
                        colorManager.setTertiaryBackgroundColor(jc);
                        break;
                    case SECONDARY_TEXT_COLOR:
                        colorManager.setPrimaryTextColor(jc);
                        break;
                    case PRIMARY_TEXT_COLOR:
                        colorManager.setSecondaryTextColor(jc);
                        break;
                    case TERTIARY_TEXT_COLOR:
                        colorManager.setTertiaryTextColor(jc);
                        break;
                }
                previewColor.setBackground(jc);

            }
        });
        this.setLayout(new BorderLayout());
        this.add(text,BorderLayout.WEST);
        this.add(previewAndButton,BorderLayout.EAST);

        this.setPreferredSize(new Dimension(200,40));
    }

    /**
     * Sets current color for preview.
     *
     * @param currentColor the current color
     */
    public void setCurrentColor(Color currentColor) {
        previewColor.setBackground( currentColor);
    }
}

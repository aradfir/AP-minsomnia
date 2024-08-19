package ap.gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The type Color manager.
 */
public class ColorManager {
    private ArrayList<JComponent> primaryBackgroundComponents=new ArrayList<>();
    private ArrayList<JComponent> secondaryBackgroundComponents=new ArrayList<>();
    private ArrayList<JComponent> tertiaryBackgroundComponents=new ArrayList<>();

    private ArrayList<JComponent> primaryTextColorComponents=new ArrayList<>();
    private ArrayList<JComponent> secondaryTextColorComponents=new ArrayList<>();
    private ArrayList<JComponent> tertiaryTextColorComponents=new ArrayList<>();

    /**
     * Sets primary background color.
     *
     * @param c the new color
     */
    void setPrimaryBackgroundColor(Color c)
    {
        ColorCanvas.PRIMARY.setColor(c);
        for (JComponent component:primaryBackgroundComponents) {
            component.setBackground(c);
        }
    }

    /**
     * Sets secondary background color.
     *
     * @param c the new color
     */
    void setSecondaryBackgroundColor(Color c)
    {
        ColorCanvas.SECONDARY.setColor(c);
        for (JComponent component:secondaryBackgroundComponents) {
            component.setBackground(c);
        }
    }

    /**
     * Sets tertiary background color.
     *
     * @param c the new color
     */
    void setTertiaryBackgroundColor(Color c)
    {
        ColorCanvas.TERTIARY.setColor(c);
        for (JComponent component:tertiaryBackgroundComponents) {
            component.setBackground(c);
        }
    }

    /**
     * Sets primary text color.
     *
     * @param c the new color
     */
    void setPrimaryTextColor(Color c)
    {
        ColorCanvas.PRIMARY_TEXT_COLOR.setColor(c);
        for (JComponent component:primaryTextColorComponents) {
            component.setForeground(c);
        }
    }

    /**
     * Sets secondary text color.
     *
     * @param c the new color
     */
    void setSecondaryTextColor(Color c)
    {
        ColorCanvas.SECONDARY_TEXT_COLOR.setColor(c);
        for (JComponent component:secondaryTextColorComponents) {
            component.setForeground(c);
        }
    }

    /**
     * Sets tertiary text color.
     *
     * @param c the new color
     */
    void setTertiaryTextColor(Color c)
    {
        ColorCanvas.TERTIARY_TEXT_COLOR.setColor(c);
        for (JComponent component:tertiaryTextColorComponents) {
            component.setForeground(c);
        }
    }

    /**
     * Color component.
     *
     * @param component the component
     * @param backColor the backcolor
     * @param foreColor the forecolor
     */
    void colorComponent(JComponent component,ColorCanvas backColor,ColorCanvas foreColor)
    {

        component.setForeground(foreColor.getColor());
        component.setBackground(backColor.getColor());

        switch (backColor)
        {

            case PRIMARY:
                primaryBackgroundComponents.add(component);
                break;
            case SECONDARY:
                secondaryBackgroundComponents.add(component);
                break;
            case TERTIARY:
                tertiaryBackgroundComponents.add(component);
                break;
        }
        switch (foreColor)
        {
            case PRIMARY_TEXT_COLOR:
                primaryTextColorComponents.add(component);
                break;
            case SECONDARY_TEXT_COLOR:
                secondaryTextColorComponents.add(component);
                break;
            case TERTIARY_TEXT_COLOR:
                tertiaryTextColorComponents.add(component);
                break;
        }
    }
    private Color nextColor(Scanner scanner){
        int tempr,tempg,tempb;
        while(!scanner.hasNextInt()) {
            scanner.next();
        }
        tempr = scanner.nextInt();

        while(!scanner.hasNextInt()) {
            scanner.next();
        }
        tempg = scanner.nextInt();

        while(!scanner.hasNextInt()) {
            scanner.next();
        }
        tempb = scanner.nextInt();

        return new Color(tempr,tempg,tempb);
    }

    /**
     * Read settings from settings.json.
     */
    public void readSettings()  {
        try {
            Scanner scanner=new Scanner(new File("settings.json"));
            //scanner.useDelimiter("\\s*\\d*\\s*");

            //System.out.println(scanner.toString());

            try {
                ColorCanvas.PRIMARY.setColor(nextColor(scanner));
                ColorCanvas.SECONDARY.setColor(nextColor(scanner));
                ColorCanvas.TERTIARY.setColor(nextColor(scanner));
                ColorCanvas.PRIMARY_TEXT_COLOR.setColor(nextColor(scanner));
                ColorCanvas.SECONDARY_TEXT_COLOR.setColor(nextColor(scanner));
                ColorCanvas.TERTIARY_TEXT_COLOR.setColor(nextColor(scanner));
            }
            catch (Exception InputMismatchException)
            {
                JOptionPane.showMessageDialog(null,"Settings file is corrupt! reverting to defaults.","ERROR",JOptionPane.ERROR_MESSAGE);
                OptionsGUI.loadDefaults(this);
                OptionsGUI.writeToFile(null);
            }
        }
        catch (Exception FileNotFoundException)
        {
            JOptionPane.showMessageDialog(null,"Settings file not found! attempting to create default.","ERROR",JOptionPane.ERROR_MESSAGE);
            OptionsGUI.loadDefaults(this);
            OptionsGUI.writeToFile(null);

        }

    }
}

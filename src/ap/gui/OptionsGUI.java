package ap.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The type Options gui.
 */
public class OptionsGUI {

    private ColorSelectionBox primaryBox;
    private ColorSelectionBox secondaryBox;
    private ColorSelectionBox tertiaryBox;
    private ColorSelectionBox primaryTextBox;
    private ColorSelectionBox secondaryTextBox;
    private ColorSelectionBox tertiaryTextBox;

    /**
     * Instantiates a new Options gui.
     *
     * @param callerFrame    the caller frame
     * @param colorManager   the color manager
     * @param dialogPosition the dialog position
     */
    public OptionsGUI(MainGUI callerFrame, ColorManager colorManager, Point dialogPosition) {
        JDialog myDialog = new JDialog((Dialog) null, "Preferences...", true);
        JCheckBox followRedirect = new JCheckBox("Follow Redirect", MainGUI.isFollowRedirect());
        JCheckBox isMinimizedOnTray = new JCheckBox("Minimize on exit", true);
        isMinimizedOnTray.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isMinimizedOnTray.isSelected())
                    callerFrame.getMainFrame().setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                else
                    callerFrame.getMainFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
        followRedirect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainGUI.setFollowRedirect(followRedirect.isSelected());
            }
        });
        JPanel checkboxes = new JPanel(new BorderLayout());
        colorManager.colorComponent(checkboxes, ColorCanvas.PRIMARY, ColorCanvas.PRIMARY_TEXT_COLOR);
        colorManager.colorComponent(isMinimizedOnTray, ColorCanvas.PRIMARY, ColorCanvas.SECONDARY_TEXT_COLOR);
        colorManager.colorComponent(followRedirect, ColorCanvas.PRIMARY, ColorCanvas.SECONDARY_TEXT_COLOR);
        checkboxes.add(isMinimizedOnTray, BorderLayout.WEST);
        checkboxes.add(followRedirect, BorderLayout.EAST);
        myDialog.setLayout(new BoxLayout(myDialog.getContentPane(), BoxLayout.Y_AXIS));
        myDialog.add(checkboxes);
         primaryBox       = (ColorSelectionBox) myDialog.add(new ColorSelectionBox("Primary Color", "Change", ColorCanvas.PRIMARY.getColor(), colorManager, ColorCanvas.PRIMARY));
         secondaryBox     = (ColorSelectionBox) myDialog.add(new ColorSelectionBox("Secondary Color", "Change", ColorCanvas.SECONDARY.getColor(), colorManager, ColorCanvas.SECONDARY));
         tertiaryBox      = (ColorSelectionBox) myDialog.add(new ColorSelectionBox("Tertiary Color", "Change", ColorCanvas.TERTIARY.getColor(), colorManager, ColorCanvas.TERTIARY));
         primaryTextBox   = (ColorSelectionBox) myDialog.add(new ColorSelectionBox("Primary Text", "Change", ColorCanvas.PRIMARY_TEXT_COLOR.getColor(), colorManager, ColorCanvas.PRIMARY_TEXT_COLOR));
         secondaryTextBox = (ColorSelectionBox) myDialog.add(new ColorSelectionBox("Secondary Text", "Change", ColorCanvas.SECONDARY_TEXT_COLOR.getColor(), colorManager, ColorCanvas.SECONDARY_TEXT_COLOR));
         tertiaryTextBox  = (ColorSelectionBox) myDialog.add(new ColorSelectionBox("Tertiary Text", "Change", ColorCanvas.TERTIARY_TEXT_COLOR.getColor(), colorManager, ColorCanvas.TERTIARY_TEXT_COLOR));

        JButton resetToDefaultBtn = new JButton("Reset");
        JButton saveToFileBtn = new JButton("Save");
        JPanel buttonsContainer = new JPanel(new BorderLayout());
        buttonsContainer.add(resetToDefaultBtn, BorderLayout.WEST);
        buttonsContainer.add(saveToFileBtn, BorderLayout.EAST);
        resetToDefaultBtn.setFocusable(false);
        saveToFileBtn.setFocusable(false);
        colorManager.colorComponent(resetToDefaultBtn, ColorCanvas.SECONDARY, ColorCanvas.SECONDARY_TEXT_COLOR);
        colorManager.colorComponent(saveToFileBtn, ColorCanvas.SECONDARY, ColorCanvas.SECONDARY_TEXT_COLOR);
        colorManager.colorComponent(buttonsContainer, ColorCanvas.PRIMARY, ColorCanvas.PRIMARY);
        resetToDefaultBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDefaults(colorManager);
                reloadColorPreviews();
            }
        });
        saveToFileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writeToFile(myDialog);
            }
        });
        myDialog.add(buttonsContainer);
        myDialog.setResizable(false);
        myDialog.pack();
        myDialog.setLocation(dialogPosition.x - myDialog.getWidth() / 2, dialogPosition.y - myDialog.getHeight() / 2);
        myDialog.setVisible(true);

    }

    /**
     * Write the settings to file.
     *
     * @param myDialog the owner dialog
     */
    public static void writeToFile(Component myDialog)
    {
        String outString = String.format("{\n" +
                        "  \"background_colors\": {\n" +
                        "    \"primary\": {\n" +
                        "      \"r\": %d ,\n" +
                        "      \"g\": %d ,\n" +
                        "      \"b\": %d\n" +
                        "    },\n" +
                        "    \"secondary\": {\n" +
                        "      \"r\": %d ,\n" +
                        "      \"g\": %d ,\n" +
                        "      \"b\": %d\n" +
                        "    },\n" +
                        "    \"tertiary\": {\n" +
                        "      \"r\": %d ,\n" +
                        "      \"g\": %d ,\n" +
                        "      \"b\": %d\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"text_colors\": {\n" +
                        "\n" +
                        "    \"primary\": {\n" +
                        "      \"r\": %d ,\n" +
                        "      \"g\": %d ,\n" +
                        "      \"b\": %d\n" +
                        "    },\n" +
                        "    \"secondary\": {\n" +
                        "      \"r\": %d ,\n" +
                        "      \"g\": %d ,\n" +
                        "      \"b\": %d\n" +
                        "    },\n" +
                        "    \"tertiary\": {\n" +
                        "      \"r\": %d ,\n" +
                        "      \"g\": %d ,\n" +
                        "      \"b\": %d\n" +
                        "    }\n" +
                        "  }\n" +
                        "}",

                ColorCanvas.PRIMARY.getColor().getRed(),
                ColorCanvas.PRIMARY.getColor().getGreen(),
                ColorCanvas.PRIMARY.getColor().getBlue(),

                ColorCanvas.SECONDARY.getColor().getRed(),
                ColorCanvas.SECONDARY.getColor().getGreen(),
                ColorCanvas.SECONDARY.getColor().getBlue(),

                ColorCanvas.TERTIARY.getColor().getRed(),
                ColorCanvas.TERTIARY.getColor().getGreen(),
                ColorCanvas.TERTIARY.getColor().getBlue(),

                ColorCanvas.PRIMARY_TEXT_COLOR.getColor().getRed(),
                ColorCanvas.PRIMARY_TEXT_COLOR.getColor().getGreen(),
                ColorCanvas.PRIMARY_TEXT_COLOR.getColor().getBlue(),

                ColorCanvas.SECONDARY_TEXT_COLOR.getColor().getRed(),
                ColorCanvas.SECONDARY_TEXT_COLOR.getColor().getGreen(),
                ColorCanvas.SECONDARY_TEXT_COLOR.getColor().getBlue(),

                ColorCanvas.TERTIARY_TEXT_COLOR.getColor().getRed(),
                ColorCanvas.TERTIARY_TEXT_COLOR.getColor().getGreen(),
                ColorCanvas.TERTIARY_TEXT_COLOR.getColor().getBlue());
        try {
            FileWriter fileWriter = new FileWriter("settings.json", false);
            fileWriter.write(outString);
            JOptionPane.showMessageDialog(myDialog, "File written successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            fileWriter.close();

        } catch (IOException ex) {
            File file = new File("settings.json");
            if (!file.exists()) {

                try {
                    file.createNewFile();
                    JOptionPane.showMessageDialog(myDialog, "The settings file was missing! It has been created. press this button again to write to it.", "Warning", JOptionPane.WARNING_MESSAGE);
                } catch (IOException exc) {
                    JOptionPane.showMessageDialog(myDialog, "The settings file was missing! It cant be re-created due to unknown reasons! Please create it manually.", "Error", JOptionPane.ERROR_MESSAGE);
                    exc.printStackTrace();
                }
                JOptionPane.showMessageDialog(myDialog, "Unknown error while writing to file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    /**
     * Load defaults of the program.
     *
     * @param colorManager the color manager to set its values to default
     */
    public static void loadDefaults(ColorManager colorManager)
    {
        Color defaultPrimary = new Color(46, 47, 43);
        Color defaultSecondary = new Color(105, 94, 184);
        Color defaultTertiary = new Color(255, 255, 255);
        Color defaultPrimaryText = new Color(153, 153, 153);
        Color defaultSecondaryText = new Color(239, 238, 247);
        Color defaultTertiaryText = new Color(102, 102, 102);
        colorManager.setPrimaryBackgroundColor(defaultPrimary);
        colorManager.setSecondaryBackgroundColor(defaultSecondary);
        colorManager.setTertiaryBackgroundColor(defaultTertiary);
        colorManager.setPrimaryTextColor(defaultPrimaryText);
        colorManager.setSecondaryTextColor(defaultSecondaryText);
        colorManager.setTertiaryTextColor(defaultTertiaryText);
    }
    private void reloadColorPreviews(){
        primaryBox.setCurrentColor(ColorCanvas.PRIMARY.getColor());
        secondaryBox.setCurrentColor(ColorCanvas.SECONDARY.getColor());
        tertiaryBox.setCurrentColor(ColorCanvas.TERTIARY.getColor());

        primaryTextBox.setCurrentColor(ColorCanvas.PRIMARY_TEXT_COLOR.getColor());
        secondaryTextBox.setCurrentColor(ColorCanvas.SECONDARY_TEXT_COLOR.getColor());
        tertiaryTextBox.setCurrentColor(ColorCanvas.TERTIARY_TEXT_COLOR.getColor());
    }
}

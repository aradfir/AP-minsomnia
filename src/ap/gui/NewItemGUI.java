package ap.gui;

import javax.swing.*;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * The type New item gui.
 */
public class NewItemGUI {

    /**
     * Instantiates a new New item gui.
     *
     * @param callerFrame    the caller frame
     * @param colorManager   the color manager
     * @param historyPanel   the history panel of the caller
     * @param dialogPosition the position of the dialog
     */
    public NewItemGUI(MainGUI callerFrame, ColorManager colorManager, HistoryPanel historyPanel, Point dialogPosition) {

        Vector<String> directoriesInHistory = new Vector<>();
        historyPanel.getAllChildren((TreeNode) historyPanel.getModel().getRoot(), "/", directoriesInHistory);
        JDialog myDialog = new JDialog(callerFrame.getMainFrame(), "New Request...", true);
        myDialog.setBackground(ColorCanvas.PRIMARY.getColor());

        JPanel mainContainerPanel = new JPanel(new BorderLayout());
        mainContainerPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        mainContainerPanel.setBackground(ColorCanvas.PRIMARY.getColor());

        JComboBox itemTypes = new JComboBox();
        for (RequestType rt : RequestType.values()) {
            itemTypes.addItem(rt);
        }
        itemTypes.addItem("Folder");
        mainContainerPanel.add(itemTypes, BorderLayout.WEST);
        JButton createButton = new JButton("Create!");
        mainContainerPanel.add(createButton, BorderLayout.EAST);
        createButton.setBackground(ColorCanvas.SECONDARY.getColor());
        createButton.setForeground(ColorCanvas.SECONDARY_TEXT_COLOR.getColor());
        JPanel textBoxAndComboBox = new JPanel(new BorderLayout());
        GhostTextField requestName = new GhostTextField("Name...",15);
        requestName.setBackground(ColorCanvas.TERTIARY.getColor());
        requestName.setForeground(ColorCanvas.TERTIARY_TEXT_COLOR.getColor());
        //requestName.setBorder(BorderFactory.createEmptyBorder());

        textBoxAndComboBox.add(requestName, BorderLayout.NORTH);
        textBoxAndComboBox.setBackground(ColorCanvas.PRIMARY.getColor());
        JComboBox directories = new JComboBox(directoriesInHistory);
        directories.setBackground(ColorCanvas.TERTIARY.getColor());
        directories.setForeground(ColorCanvas.TERTIARY_TEXT_COLOR.getColor());
        itemTypes.setBackground(ColorCanvas.TERTIARY.getColor());
        itemTypes.setForeground(ColorCanvas.TERTIARY_TEXT_COLOR.getColor());
        textBoxAndComboBox.add(directories, BorderLayout.CENTER);
        mainContainerPanel.add(textBoxAndComboBox, BorderLayout.CENTER);
        myDialog.add(mainContainerPanel);
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (itemTypes.getSelectedItem().equals("Folder")) {
                    historyPanel.addNewDirectory((String) directories.getSelectedItem(), requestName.getText());
                    myDialog.setVisible(false);
                    myDialog.dispose();
                } else {
                    historyPanel.addNewRequest((String) directories.getSelectedItem(), new GraphicalRequest((RequestType) itemTypes.getSelectedItem(), requestName.getText()));
                    myDialog.setVisible(false);
                    myDialog.dispose();

                }

            }
        });
        myDialog.setResizable(false);
        myDialog.pack();
        myDialog.setLocation(dialogPosition.x - myDialog.getWidth() / 2, dialogPosition.y - myDialog.getHeight() / 2);
        myDialog.setVisible(true);


    }
}

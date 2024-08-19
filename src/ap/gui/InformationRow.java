package ap.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

/**
 * The type Information row, used for body and header rows in both response and request.
 */
class InformationRow extends JPanel implements Serializable {
    private JTextField key;
    private JTextField value;
    private JCheckBox isEnabled;
    private JButton delete;

    /**
     * Gets key.
     *
     * @return the key textfield(left one)
     */
    public JTextField getKey() {
        return key;
    }

    /**
     * Gets value.
     *
     * @return the value textfield(right one)
     */
    public JTextField getValue() {
        return value;
    }

    /**
     * Gets is enabled.
     *
     * @return the enabled checkbox
     */
    public JCheckBox getIsEnabled() {
        return isEnabled;
    }

    /**
     * Gets delete.
     *
     * @return the delete button
     */
    public JButton getDelete() {
        return delete;
    }

    /**
     * Instantiates a new Information row.
     * This is used for response and has now delete button or checkbox
     *
     * @param colorManager  the color manager
     * @param leftTextInfo  the left text info
     * @param rightTextInfo the right text info
     */
    public InformationRow(ColorManager colorManager, String leftTextInfo, String rightTextInfo) {
        super();
        key = new JTextField(leftTextInfo, 15);
        key.setVisible(true);
        value = new JTextField(rightTextInfo, 15);
        value.setVisible(true);
        colorManager.colorComponent(key, ColorCanvas.PRIMARY, ColorCanvas.PRIMARY_TEXT_COLOR);
        colorManager.colorComponent(value, ColorCanvas.PRIMARY, ColorCanvas.PRIMARY_TEXT_COLOR);
        colorManager.colorComponent(this, ColorCanvas.PRIMARY, ColorCanvas.PRIMARY_TEXT_COLOR);
        this.add(key);
        this.add(value);
        this.setMaximumSize(new Dimension(this.getPreferredSize().width, key.getPreferredSize().height + 10));
        this.setPreferredSize(new Dimension(this.getPreferredSize().width, key.getPreferredSize().height + 10));
        // this.setBackground(Color.red);
        key.setEditable(false);
        value.setEditable(false);
        this.updateUI();
        this.validate();
        this.repaint();
        this.setVisible(true);


    }


    /**
     * Instantiates a new Information row.
     *
     * @param ownerPanel    the owner panel
     * @param gbc           the gridbagconstraint of owner, used for deletion
     * @param colorManager  the color manager
     * @param leftTextInfo  the left text info
     * @param rightTextInfo the right text info
     */
    private JPanel ownerPanel;

    public JPanel getOwnerPanel() {
        return ownerPanel;
    }

    public void setOwnerPanel(JPanel ownerPanel) {
        this.ownerPanel = ownerPanel;
    }

    public InformationRow(JPanel ownerPanel, GridBagConstraints gbc, ColorManager colorManager, String leftTextInfo, String rightTextInfo, boolean hasFile) {
        super();
        this.ownerPanel = ownerPanel;
        key = new GhostTextField(leftTextInfo, 15);
        key.setVisible(true);
        value = new GhostTextField(rightTextInfo, 15);
        value.setVisible(true);
        delete = new JButton("Delete");
        delete.setVisible(true);

        delete.addActionListener(new DeleteActionListener(gbc));
        isEnabled = new JCheckBox("", true);
        isEnabled.setVisible(true);
        colorManager.colorComponent(this, ColorCanvas.PRIMARY, ColorCanvas.PRIMARY_TEXT_COLOR);
        colorManager.colorComponent(key, ColorCanvas.PRIMARY, ColorCanvas.PRIMARY_TEXT_COLOR);
        colorManager.colorComponent(value, ColorCanvas.PRIMARY, ColorCanvas.PRIMARY_TEXT_COLOR);
        colorManager.colorComponent(isEnabled, ColorCanvas.PRIMARY, ColorCanvas.PRIMARY_TEXT_COLOR);
        colorManager.colorComponent(delete, ColorCanvas.SECONDARY, ColorCanvas.SECONDARY_TEXT_COLOR);

        delete.setFocusable(false);
        this.add(key);
        this.add(value);
        this.add(isEnabled);
        this.add(delete);
        if (hasFile) {
            JButton fileChooser = new JButton("File");
            colorManager.colorComponent(fileChooser, ColorCanvas.SECONDARY, ColorCanvas.SECONDARY_TEXT_COLOR);
            fileChooser.addActionListener(new FileSelectorActionListener());

            this.add(fileChooser);
        }
        this.setVisible(true);
        this.setOpaque(true);

    }

    public boolean isEnabled() {
        return isEnabled != null && isEnabled.isSelected();
    }

    public String getKeyText() {
        return key.getText();
    }

    public String getValueText() {
        return value.getText();
    }
    private class FileSelectorActionListener implements ActionListener,Serializable{
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new java.io.File("."));
            chooser.setDialogTitle("Select file...");
            chooser.setAcceptAllFileFilterUsed(true);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

                System.out.println("@ " + chooser.getSelectedFile().toString());
                value.setText("@" + chooser.getSelectedFile().toString());
            } else {
                System.out.println("No Selection ");
            }
        }
    }
    private class DeleteActionListener implements ActionListener,Serializable{
        private GridBagConstraints gbc;

        public DeleteActionListener(GridBagConstraints gbc) {
            this.gbc = gbc;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            InformationRow.this.setVisible(false);
            InformationRow.this.ownerPanel.remove(InformationRow.this);
            InformationRow.this.isEnabled.setSelected(false);
            System.out.println(isEnabled.isSelected());
            gbc.gridy--;


        }
    }

}


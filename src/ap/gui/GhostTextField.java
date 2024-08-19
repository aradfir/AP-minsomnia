package ap.gui;

import javax.swing.*;
import javax.swing.tree.TreeModel;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.Serializable;

/**
 * The type Ghost text field, a textfield with a ghost preview text that disappears on click.
 */
public class GhostTextField extends JTextField implements Serializable {
    private String ghostText;
    private boolean isGhostMode;

    /**
     * Instantiates a new Ghost text field.
     *
     * @param ghostText the ghost text
     */
    public GhostTextField(String ghostText) {
        super(ghostText);
        this.ghostText=ghostText;
        isGhostMode=false;
        this.addFocusListener(new GhostTextHandler());
    }

    /**
     * Instantiates a new Ghost text field.
     *
     * @param ghostText the ghost text
     * @param columns   the columns count
     */
    public GhostTextField(String ghostText, int columns) {
        super(ghostText, columns);
        this.ghostText=ghostText;
        this.setText(ghostText);
        this.addFocusListener(new GhostTextHandler());
    }

    @Override
    public String getText() {
        if(super.getText().equals(ghostText))
            return "";
        return super.getText();
    }

    /**
     * Gets ghost text.
     *
     * @return the ghost text
     */
    public String getGhostText() {
        return ghostText;
    }

    /**
     * Sets ghost text.
     *
     * @param ghostText the ghost text
     */
    public void setGhostText(String ghostText) {
        this.ghostText = ghostText;
    }

    /**
     * The type Ghost text handler, checks when to enable or disable that text.
     */
    public class GhostTextHandler implements FocusListener{
        @Override
        public void focusGained(FocusEvent e) {

            if(e.isTemporary())
                return;
            if(e.getOppositeComponent()==null)
                return;
            if(GhostTextField.super.getText().trim().equals(ghostText))
                GhostTextField.this.setText("");
                isGhostMode=false;
        }

        @Override
        public void focusLost(FocusEvent e) {

            if(GhostTextField.this.getText().trim().equals("")) {
                GhostTextField.this.setText(ghostText);
                isGhostMode=true;
            }
        }
    }
}

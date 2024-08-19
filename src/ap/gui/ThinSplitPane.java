package ap.gui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;

public class ThinSplitPane extends JSplitPane {
    public ThinSplitPane(Component newLeftComponent, Component newRightComponent) {
        super(JSplitPane.HORIZONTAL_SPLIT, true, newLeftComponent, newRightComponent);
        BasicSplitPaneDivider divider = ( (BasicSplitPaneUI) this.getUI()).getDivider();
        divider.setDividerSize(2);
        divider.setBackground(ColorCanvas.SECONDARY_TEXT_COLOR.getColor());
    }
}

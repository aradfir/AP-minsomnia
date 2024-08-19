package ap.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * The type Main gui.
 */
public class MainGUI {

    private JFrame mainFrame;
    private JMenuBar menuBar;
    static private ColorManager colorManager;
    private HistoryPanel historyPanel;
    private ThinSplitPane leftSplitPane = null;
    private ThinSplitPane rightSplitPane = null;
    private static boolean followRedirect=true;

    public static boolean isFollowRedirect() {
        return followRedirect;
    }

    public static void setFollowRedirect(boolean followRedirects) {
        followRedirect = followRedirects;
    }

    /**
     * Gets left split pane.
     *
     * @return the left split pane
     */
    public ThinSplitPane getLeftSplitPane() {
        return leftSplitPane;
    }

    /**
     * Gets right split pane.
     *
     * @return the right split pane
     */
    public ThinSplitPane getRightSplitPane() {
        return rightSplitPane;
    }

    /**
     * Gets main frame.
     *
     * @return the main frame
     */
    public JFrame getMainFrame() {
        return mainFrame;
    }

    /**
     * Gets color manager.
     *
     * @return the color manager
     */
    public static ColorManager getColorManager() {
        return colorManager;
    }

    /**
     * Gets history panel.
     *
     * @return the history panel
     */
    public HistoryPanel getHistoryPanel() {
        return historyPanel;
    }

    /**
     * Creates the menu bar
     * @param leftSide the left panel, used for toggle sidebar
     * @return the confiugred mneu
     */
    private JMenuBar createMenuBar(JPanel leftSide) {
        menuBar = new JMenuBar();

        JMenu applicationMenu = new JMenu("Application");
        JMenu viewMenu = new JMenu("View");
        JMenu helpMenu = new JMenu("Help");
        JMenuItem preferencesMenuItem = new JMenuItem("Preferences", KeyEvent.VK_P);

        JMenuItem quitMenuItem = new JMenuItem("Quit", KeyEvent.VK_Q);
        JMenuItem fullscreenMenuItem = new JMenuItem("Toggle fullscreen", KeyEvent.VK_F);
        JMenuItem toggleSideBarItem = new JMenuItem("Toggle sidebar", KeyEvent.VK_S);
        JMenuItem helpMenuItem = new JMenuItem("Help", KeyEvent.VK_F1);
        JMenuItem aboutItem = new JMenuItem("About us", KeyEvent.VK_A);
        preferencesMenuItem.setAccelerator(KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK));
        helpMenuItem.setAccelerator(KeyStroke.getKeyStroke('H', InputEvent.ALT_DOWN_MASK));
        aboutItem.setAccelerator(KeyStroke.getKeyStroke('A', InputEvent.ALT_DOWN_MASK));
        fullscreenMenuItem.setAccelerator(KeyStroke.getKeyStroke('F', InputEvent.CTRL_DOWN_MASK));
        toggleSideBarItem.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
        toggleSideBarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (leftSplitPane.getLeftComponent() != null) {
                    leftSplitPane.setLeftComponent(null);
                } else
                    leftSplitPane.setLeftComponent(leftSide);
            }
        });
        preferencesMenuItem.addActionListener(new ShowOptions());

        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainFrame, "This program is a minimalistic version of the program Insomnia, hence MinSomnia.\nThis program has been written by Arad Firouzkouhi (9831047) for Advanced Programming class of summer of 2020.");
            }
        });
        helpMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainFrame, "You must use the new button from the left side to create a request, fill out the middle segment and press SEND. Duh.");
            }
        });
        quitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.setVisible(false);
                mainFrame.dispose();
                System.exit(0);
            }
        });
        fullscreenMenuItem.addActionListener(new FullscreenHandler());
        applicationMenu.add(preferencesMenuItem);
        applicationMenu.add(quitMenuItem);

        viewMenu.add(fullscreenMenuItem);
        viewMenu.add(toggleSideBarItem);
        helpMenu.add(helpMenuItem);


        helpMenu.add(aboutItem);
        menuBar.add(applicationMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);
        return menuBar;
    }

    private void configureTray() throws AWTException {
        SystemTray systemTray = SystemTray.getSystemTray();
        PopupMenu trayMenu = new PopupMenu();

        MenuItem trayMenuRestore = new MenuItem("Restore");
        MenuItem trayMenuQuit = new MenuItem("Quit");

        trayMenu.add(trayMenuRestore);
        trayMenu.add(trayMenuQuit);
        trayMenuRestore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.setVisible(true);
            }
        });
        trayMenuQuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.setVisible(false);
                mainFrame.dispose();
                System.exit(0);
            }
        });
        TrayIcon icon = new TrayIcon(new ImageIcon("logo.png").getImage().getScaledInstance(16, 16, Image.SCALE_AREA_AVERAGING), "Minsomnia", trayMenu);
        systemTray.add(icon);
    }

    /**
     * Instantiates a new Main gui.
     *
     * @throws AWTException the awt exception
     */
    public MainGUI() throws AWTException {

        colorManager = new ColorManager();
        colorManager.readSettings();
        mainFrame = new JFrame("MINsomnia");
        mainFrame.setLayout(new BorderLayout());
        JPanel mainBody = new JPanel(new BorderLayout());
        JPanel leftSide = new JPanel(new BorderLayout());

        mainFrame.add(createMenuBar(leftSide), BorderLayout.NORTH);
        JButton mainButton = new JButton("MinSomnia");
        //  mainButton.setBorderPainted(false);
        JPanel buttonsAndSearch = new JPanel(new BorderLayout());

        colorManager.colorComponent(mainButton, ColorCanvas.SECONDARY, ColorCanvas.SECONDARY_TEXT_COLOR);

        mainButton.setPreferredSize(new Dimension(200, 45));
        mainButton.setFocusable(false);
        buttonsAndSearch.add(mainButton, BorderLayout.NORTH);
        JPanel newAndSearch = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.weightx = 4;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.BOTH;
        GhostTextField searchBar = new GhostTextField("Filter...");
        colorManager.colorComponent(searchBar, ColorCanvas.PRIMARY, ColorCanvas.PRIMARY_TEXT_COLOR);
        searchBar.setPreferredSize(new Dimension(102, 26));
        searchBar.setBorder(BorderFactory.createLoweredBevelBorder());
        newAndSearch.add(searchBar, gbc);
        gbc.gridx = 10;
        gbc.weightx = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;

        gbc.gridx = 11;
        JButton newRequest = new JButton("New...");

        newRequest.setFocusable(false);

        newRequest.addActionListener(new NewRequestMenu());
        colorManager.colorComponent(newRequest, ColorCanvas.SECONDARY, ColorCanvas.SECONDARY_TEXT_COLOR);
        newAndSearch.add(newRequest, gbc);
        colorManager.colorComponent(newAndSearch, ColorCanvas.PRIMARY, ColorCanvas.PRIMARY_TEXT_COLOR);
        buttonsAndSearch.add(newAndSearch, BorderLayout.CENTER);
        leftSide.add(buttonsAndSearch, BorderLayout.NORTH);
        historyPanel = new HistoryPanel();

        colorManager.colorComponent(historyPanel, ColorCanvas.PRIMARY, ColorCanvas.SECONDARY);
        historyPanel.setPreferredSize(new Dimension(mainButton.getWidth(), 500));
        leftSide.add(historyPanel, BorderLayout.CENTER);

        JPanel temp = new JPanel(new BorderLayout());
        JPanel temp1 = new JPanel(new BorderLayout());
        JLabel midSegmentTempText = new JLabel("<html><body>You havent selected a request yet!<br> Create a request using the new button and then select it.</body></html>", JLabel.CENTER);
        colorManager.colorComponent(midSegmentTempText, ColorCanvas.PRIMARY, ColorCanvas.SECONDARY_TEXT_COLOR);
        temp.add(midSegmentTempText, BorderLayout.CENTER);
        JLabel rightSegmentTempText = new JLabel("<html><body>You havent sent a request yet!<br> Select a request and press the send button to show the response.</body></html>", JLabel.CENTER);
        colorManager.colorComponent(rightSegmentTempText, ColorCanvas.PRIMARY, ColorCanvas.SECONDARY_TEXT_COLOR);
        temp1.add(rightSegmentTempText, BorderLayout.CENTER);
        temp.setPreferredSize(new Dimension(600, leftSide.getHeight()));
        temp1.setPreferredSize(new Dimension(600, leftSide.getHeight()));
        leftSplitPane = new ThinSplitPane(leftSide, temp);
        rightSplitPane = new ThinSplitPane(leftSplitPane, temp1);
        colorManager.colorComponent(temp, ColorCanvas.PRIMARY, ColorCanvas.PRIMARY_TEXT_COLOR);
        colorManager.colorComponent(temp1, ColorCanvas.PRIMARY, ColorCanvas.PRIMARY_TEXT_COLOR);
        historyPanel.setMiddleSegmentSeperator(leftSplitPane,this);
        mainBody.add(rightSplitPane);
        leftSplitPane.setContinuousLayout(true);

        mainFrame.setIconImage(new ImageIcon("logo.png").getImage());


        mainButton.setHorizontalAlignment(SwingConstants.LEFT);

        mainFrame.add(mainBody, BorderLayout.CENTER);
        configureTray();
        mainFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    /**
     * Show gui.
     */
    void showGUI() {
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    /**
     * The listener to show the NEW REQUEST GUI.
     */
    public class NewRequestMenu implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Point dialogPosition = new Point(mainFrame.getLocation().x + mainFrame.getWidth() / 2, mainFrame.getLocation().y + mainFrame.getHeight() / 2);
            new NewItemGUI(MainGUI.this, colorManager, historyPanel, dialogPosition);
        }
    }

    /**
     * The listener to show the options GUI.
     */
    public class ShowOptions implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Point dialogPosition = new Point(mainFrame.getLocation().x + mainFrame.getWidth() / 2, mainFrame.getLocation().y + mainFrame.getHeight() / 2);
            new OptionsGUI(MainGUI.this, colorManager, dialogPosition);
        }
    }

    /**
     * The type Fullscreen handler.
     */
    public class FullscreenHandler implements ActionListener {
        /**
         * The Is full screened.
         */
        boolean isFullScreened = false;
        /**
         * The Size before full screen.
         */
        Dimension sizeBeforeFullScreen = null;
        /**
         * The Location before full screen.
         */
        Point locationBeforeFullScreen = null;

        @Override
        public void actionPerformed(ActionEvent e) {

            if (!isFullScreened) {
                locationBeforeFullScreen = mainFrame.getLocation();
                sizeBeforeFullScreen = mainFrame.getSize();

                mainFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
                mainFrame.setLocation(0, 0);
            } else {
                mainFrame.setSize(sizeBeforeFullScreen);
                mainFrame.setLocation(locationBeforeFullScreen);
            }
            isFullScreened = !isFullScreened;
        }
    }
}

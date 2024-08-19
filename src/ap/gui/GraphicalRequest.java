package ap.gui;

import ap.console.MainConsole;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The type Graphical request used to show the middle segment of the program.
 */
public class GraphicalRequest implements Serializable {
    private RequestType type;
    private String name;
    private GhostTextField url;
  //  private JPanel midSegmentPanel;
    private ArrayList<InformationRow> bodyRows;
    private ArrayList<InformationRow> headerRows;

    //make an array of Information rows to put in the jtabbedpanes centers
    private JScrollPane makeInsideOfTab(ColorManager colorManager, String keyDefaultText, String valueDefaultText, ArrayList<InformationRow> infoArraylist,boolean hasFile) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.weighty = 0.0;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel panel = new JPanel(new GridBagLayout());

        //panel.setPreferredSize(header.);
        colorManager.colorComponent(panel, ColorCanvas.PRIMARY, ColorCanvas.PRIMARY_TEXT_COLOR);
        JScrollPane pane = new JScrollPane(panel);
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);


        gbc.gridy = 0;
        if(infoArraylist.size()!=0) {
            //needs to load data
            for (InformationRow row : infoArraylist) {
                colorManager.colorComponent(row, ColorCanvas.PRIMARY, ColorCanvas.PRIMARY_TEXT_COLOR);
                panel.add(row, gbc);
                panel.updateUI();
                gbc.gridy++;
            }
        }
        else {
            InformationRow row = new InformationRow(panel, gbc, colorManager, keyDefaultText, valueDefaultText, hasFile);
            infoArraylist.add(row);
            row.setOwnerPanel(panel);
            colorManager.colorComponent(row, ColorCanvas.PRIMARY, ColorCanvas.PRIMARY_TEXT_COLOR);
            panel.add(row, gbc);
            gbc.gridy++;
        }

        gbc.gridy = 9999;
        JButton addNew = new JButton("+");
        addNew.setFocusable(false);
        colorManager.colorComponent(addNew, ColorCanvas.SECONDARY, ColorCanvas.SECONDARY_TEXT_COLOR);
        panel.add(addNew, gbc);
        addNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InformationRow row = new InformationRow(panel, gbc, colorManager, keyDefaultText, valueDefaultText,hasFile);
                infoArraylist.add(row);
                colorManager.colorComponent(row, ColorCanvas.PRIMARY, ColorCanvas.PRIMARY_TEXT_COLOR);
                panel.add(row, gbc);
                panel.updateUI();
                gbc.gridy++;

            }
        });

        gbc.gridy = infoArraylist.size()+1;
        return pane;
    }

    /**
     * Instantiates a new Graphical request.
     *
     * @param type     the type of the request
     * @param name     the name of the request
     */
    public GraphicalRequest(RequestType type, String name){
        this.type = type;
        this.name = name;
        headerRows= new ArrayList<>();
        bodyRows=new ArrayList<>();
    }
    public JPanel getGraphicalRequest(MainGUI ownerGUI) {
        final HistoryPanel owner=ownerGUI.getHistoryPanel();
        JPanel middleSegment = new JPanel(new BorderLayout());
        middleSegment.setPreferredSize(new Dimension(600, 45 + 26 + 500));
        //create the request url, request type and send button
        JPanel requestURLArea = new JPanel(new BorderLayout());
        JComboBox requestType = new JComboBox();
        for (RequestType type1 : RequestType.values()) {
            requestType.addItem(type1);
        }
        ColorManager colorManager = MainGUI.getColorManager();
        colorManager.colorComponent(requestType, ColorCanvas.TERTIARY, ColorCanvas.TERTIARY_TEXT_COLOR);
        requestType.setPreferredSize(new Dimension(70, 45));
        //set the combobox to borderless
        for (int i = 0; i < requestType.getComponentCount(); i++) {
            if (requestType.getComponent(i) instanceof JComponent) {
                ((JComponent) requestType.getComponent(i)).setBorder(new EmptyBorder(0, 0, 0, 0));
            }


            if (requestType.getComponent(i) instanceof AbstractButton) {
                ((AbstractButton) requestType.getComponent(i)).setBorderPainted(false);
            }
        }
        requestType.setSelectedItem(type);
        //change in history when type is changed
        requestType.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                GraphicalRequest.this.type = (RequestType) e.getItem();
                owner.updateUI();
            }
        });
        requestURLArea.add(requestType, BorderLayout.WEST);
        if(url==null) {
            url = new GhostTextField("Enter url...");
        }
        url.setPreferredSize(new Dimension(350, 45));
        url.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        colorManager.colorComponent(url, ColorCanvas.TERTIARY, ColorCanvas.TERTIARY_TEXT_COLOR);
        requestURLArea.add(url, BorderLayout.CENTER);
        CoolButton sendButton = new CoolButton("Send");
        sendButton.setPreferredSize(new Dimension(70, 45));

        colorManager.colorComponent(sendButton, ColorCanvas.TERTIARY, ColorCanvas.TERTIARY_TEXT_COLOR);
        requestURLArea.add(sendButton, BorderLayout.EAST);
        middleSegment.add(requestURLArea, BorderLayout.NORTH);
        JPanel requestInfo = new JPanel(new BorderLayout());
        colorManager.colorComponent(requestInfo, ColorCanvas.PRIMARY, ColorCanvas.PRIMARY_TEXT_COLOR);
        middleSegment.add(requestInfo, BorderLayout.CENTER);


        //create the center tabbed pane
        JTabbedPane bodyHeader = new JTabbedPane();
        JPanel body = new JPanel(new BorderLayout());
        JPanel header = new JPanel(new BorderLayout());
        bodyHeader.add("Header", header);


        header.add(makeInsideOfTab(colorManager, "Header", "Value", headerRows,false), BorderLayout.CENTER);
        body.add(makeInsideOfTab(colorManager, "Name", "Value", bodyRows,true), BorderLayout.CENTER);
        bodyHeader.add("Body", body);

        colorManager.colorComponent(bodyHeader, ColorCanvas.TERTIARY, ColorCanvas.TERTIARY_TEXT_COLOR);

        requestInfo.add(bodyHeader, BorderLayout.CENTER);
        colorManager.colorComponent(requestURLArea, ColorCanvas.TERTIARY, ColorCanvas.TERTIARY_TEXT_COLOR);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //JPanel response= null;
                //response.setPreferredSize(new Dimension(middleSegment.getPreferredSize().width,middleSegment.getPreferredSize().height));
                //ownerGUI.getRightSplitPane().setRightComponent(response);
                GraphicalHttpListener threadListener = new GraphicalHttpListener(ownerGUI.getRightSplitPane(), colorManager);
                if (url.getText().toLowerCase().startsWith("jurl")) {
                    try {
                        MainConsole.main((System.getProperty("user.dir") + " " + url.getText().substring(5)).split(" "), threadListener);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(ownerGUI.getMainFrame(),ex.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
                    }
                }
                else{
                    try {
                        MainConsole.main(makeCommand(url.getText(),(RequestType)requestType.getSelectedItem(),headerRows,bodyRows), threadListener);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(ownerGUI.getMainFrame(),ex.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        return middleSegment;
    }

    /**
     * Turns gui to args for console comands
     * @param url request URL
     * @param method request method
     * @param headerRows header key values
     * @param bodyRows body key values
     * @return console args
     */
    private String[] makeCommand(String url, RequestType method, ArrayList<InformationRow> headerRows, ArrayList<InformationRow> bodyRows) {
        ArrayList<String> args = new ArrayList<>();
        args.add(System.getProperty("user.dir"));
        args.add(url);
        args.add("-M");
        args.add(method.name().toUpperCase());

        String body = "";
        for (InformationRow bodyData : bodyRows) {
            if(bodyData==null)
                continue;
            if(!bodyData.isEnabled())
                continue;
            if(bodyData.getValueText().equals("")&&bodyData.getKeyText().equals(""))
                continue;
            body += "&" + bodyData.getKeyText() + "=" + bodyData.getValueText();
        }
        String header = "";
        for (InformationRow headerRow : headerRows) {
            if(headerRow==null)
                continue;
            if(!headerRow.isEnabled())
                continue;
            if(headerRow.getValueText().equals("")&&headerRow.getKeyText().equals(""))
                continue;
            header += ";" + headerRow.getKeyText() + ":" + headerRow.getValueText();
        }
        if(!header.equals("")) {
            header = header.substring(1);
            args.add("-H");
            args.add(header);
        }
        if (!body.equals("")) {
            body = body.substring(1);
            args.add("-d");
            args.add(body);

        }
        if(MainGUI.isFollowRedirect())
            args.add("-f");
        String[] argsArray=new String[args.size()];
        return args.toArray(argsArray);
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public RequestType getType() {
        return type;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }



    @Override
    public String toString() {
        return "[" + this.type.name() + "] " + this.name;
    }
}

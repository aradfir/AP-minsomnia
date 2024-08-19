package ap.gui;

import ap.console.MimeType;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;

/**
 * The type Graphical response, the right panel of the program.
 */
public class GraphicalResponse {

    private static String headerString="";

    /**
     * makes the JPanel of response using info
     * @param response the response to use
     * @param bodyInfo the byte array of body
     * @param timeInSeconds the request timer
     * @param savedBodyAddress the location the body is saved to, as file
     * @param mimeType the mime type of the response
     * @param colorManager the colorManager
     * @return the response JPanel
     * @throws IOException
     */
    public static JPanel getGraphicalResponse(HttpResponse<InputStream> response,byte[] bodyInfo,double timeInSeconds,String savedBodyAddress,String mimeType, ColorManager colorManager) throws IOException {
        JPanel panel=new JPanel(new BorderLayout());

        PaneledLabel status;
        if (response.statusCode()<200) status=new PaneledLabel(response.statusCode()+"",Color.pink,Color.WHITE);
        else if(200<=response.statusCode()&&response.statusCode()<300) status=new PaneledLabel(response.statusCode()+"",new Color(117,176,36),Color.WHITE);
        else if(300<=response.statusCode()&&response.statusCode()<400) status=new PaneledLabel(response.statusCode()+"",ColorCanvas.SECONDARY.getColor(),Color.WHITE);
        else if(400<=response.statusCode()&&response.statusCode()<600) status=new PaneledLabel(response.statusCode()+"",new Color(236,135,2),Color.WHITE);
        else status=new PaneledLabel(response.statusCode()+"",Color.darkGray,Color.WHITE);

        double size = bodyInfo.length;
        String postfixSize;
        if (size > 1024 * 1024 * 1024) {
            size /= (1024.0 * 1024 * 1024);
            postfixSize = "GB";
        } else if (size > 1024 * 1024) {
            size /= (1024.0 * 1024);
            postfixSize = "MB";
        } else if (size > 1024) {
            size /= 1024.0;
            postfixSize = "KB";
        } else {
            postfixSize = "B";
        }
        PaneledLabel time=new PaneledLabel(timeInSeconds+"s",new Color(224,224,224),new Color(102,102,102));
        PaneledLabel bodySize=new PaneledLabel(new DecimalFormat("#.##").format(size) +postfixSize,new Color(224,224,224),new Color(102,102,102));
        JPanel resultInfo=new JPanel();
        BoxLayout boxlayout=new BoxLayout(resultInfo,BoxLayout.X_AXIS);
        resultInfo.setLayout(boxlayout);
        resultInfo.setPreferredSize(new Dimension(20,45));
        colorManager.colorComponent(resultInfo, ColorCanvas.TERTIARY, ColorCanvas.TERTIARY_TEXT_COLOR);
        resultInfo.add(Box.createHorizontalStrut(20));
        time.setMaximumSize(time.getPreferredSize());
        status.setMaximumSize(status.getPreferredSize());
        bodySize.setMaximumSize(bodySize.getPreferredSize());
        resultInfo.add(status);
        resultInfo.add(Box.createHorizontalStrut(20));
        resultInfo.add(time);
        resultInfo.add(Box.createHorizontalStrut(20));
        resultInfo.add(bodySize);
        resultInfo.add(Box.createHorizontalStrut(50));
        JButton copyToClipboard=new JButton("Copy to clipboard");
        copyToClipboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringSelection stringSelection = new StringSelection(headerString);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);

            }
        });
        colorManager.colorComponent(copyToClipboard, ColorCanvas.SECONDARY, ColorCanvas.SECONDARY_TEXT_COLOR);
        resultInfo.add(copyToClipboard);
        panel.add(resultInfo,BorderLayout.NORTH);
        JTabbedPane pane =new JTabbedPane();
        JPanel header=new JPanel();
        BoxLayout layout=new BoxLayout(header,BoxLayout.Y_AXIS);
        colorManager.colorComponent(header, ColorCanvas.PRIMARY, ColorCanvas.PRIMARY_TEXT_COLOR);
        colorManager.colorComponent(pane, ColorCanvas.TERTIARY, ColorCanvas.TERTIARY_TEXT_COLOR);
        header.setLayout(layout);
        JScrollPane scrollPane=new JScrollPane(header);
        for(String headerKey:response.headers().map().keySet()) {
            String value=response.headers().map().get(headerKey).toString();
            value=value.substring(1,value.length()-1 );
            header.add(new InformationRow(colorManager, headerKey,value ));
            headerString+=";"+headerKey+":"+value;
        }
        if(headerString.length()!=0)
            headerString=headerString.substring(1);
        pane.add("Header",scrollPane);
        JPanel body=new JPanel(new BorderLayout());
        colorManager.colorComponent(body, ColorCanvas.PRIMARY, ColorCanvas.PRIMARY_TEXT_COLOR);
        JTextPane raw = new JTextPane();
        raw.setText(new String(bodyInfo));
        JTextPane visualPreview = new JTextPane();
        if(mimeType.contains("text"))
            visualPreview.setPage(new File(savedBodyAddress).toURI().toURL());
        else if(mimeType.contains("image"))
            visualPreview.insertIcon(new ImageIcon(savedBodyAddress));
        else
            visualPreview.setText("No preview available! You can view this file in : "+savedBodyAddress);
        colorManager.colorComponent(raw, ColorCanvas.PRIMARY, ColorCanvas.SECONDARY_TEXT_COLOR);
        raw.setEditable(false);
        JScrollPane rawScrollPane = new JScrollPane(raw);

        //colorManager.colorComponent(visualPreview, ColorCanvas.PRIMARY, ColorCanvas.SECONDARY_TEXT_COLOR);
        visualPreview.setEditable(false);

        JScrollPane visualPreviewScrollPane = new JScrollPane(visualPreview);

       // visualPreview.setText(new String(bodyInfo));
        JComboBox responseTypes=new JComboBox(new String[]{"Raw","Visual Representation"});
        JPanel cards = new JPanel(new CardLayout());
        //raw.setText(new String(bodyInfo));

        cards.add(rawScrollPane, "Raw");
        cards.add(visualPreviewScrollPane, "Visual Representation");
//        visualPreview.setPage(response.request().uri().toURL());
        responseTypes.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                CardLayout cl = (CardLayout)(cards.getLayout());
                cl.show(cards, (String)e.getItem());
            }
        });
        body.add(cards,BorderLayout.CENTER);
        colorManager.colorComponent(responseTypes, ColorCanvas.PRIMARY, ColorCanvas.PRIMARY_TEXT_COLOR);
        body.add(responseTypes,BorderLayout.NORTH);
        pane.add("Body",body);
        panel.add(pane,BorderLayout.CENTER);

        return panel;
    }
}

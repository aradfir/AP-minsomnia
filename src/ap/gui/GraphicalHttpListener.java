package ap.gui;

import ap.ThreadCompletionListener;
import ap.console.FileUtils;
import ap.console.HttpExecutor;
import ap.console.MimeType;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpResponse;
import java.nio.channels.UnresolvedAddressException;

public class GraphicalHttpListener implements ThreadCompletionListener {
    private JSplitPane rightSplitPane;
    private ColorManager colorManager;

    public GraphicalHttpListener(JSplitPane rightSplitPane, ColorManager colorManager) {
        this.rightSplitPane = rightSplitPane;
        this.colorManager = colorManager;
    }

    @Override
    public void finishedSuccessfully(Thread thread)  {
        if(!(thread instanceof HttpExecutor))
        {
            throw new IllegalArgumentException("The thread must be a HttpExecutor!");
        }

        HttpExecutor ex=(HttpExecutor)thread;
        String mimeType = "";
        HttpResponse<InputStream> response=ex.getResponse();
        byte[] byteArray= new byte[0];
        try {
            byteArray = response.body().readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response.headers().firstValue("content-type").isPresent())
            mimeType = response.headers().firstValue("content-type").get().split(";")[0];
        else if (response.headers().firstValue("Content-Type").isPresent())
            mimeType = response.headers().firstValue("Content-Type").get().split(";")[0];
        String outputExtension = MimeType.getExtension(mimeType);
        String outputDir = System.getProperty("user.dir") + "/saved/responses/";
        String saveDir=null;
        try {
            saveDir=FileUtils.saveToFile(outputDir, null, outputExtension, byteArray);
            System.out.println("File saved to: " + saveDir);
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(rightSplitPane,"Error in saving response to file.","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        try {
            JPanel responsePanel=GraphicalResponse.getGraphicalResponse(ex.getResponse(),byteArray,ex.getTimeInSeconds(),saveDir,mimeType,colorManager);
            responsePanel.setPreferredSize(rightSplitPane.getRightComponent().getPreferredSize());
            rightSplitPane.setRightComponent(responsePanel);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(rightSplitPane,"Unknown IO error!.","ERROR",JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }

    @Override
    public void finishedException(Thread thread, Exception ex, String errorSegmentDescriptor) {
        if(ex instanceof FileNotFoundException)
            JOptionPane.showMessageDialog(rightSplitPane,"The upload file does not exist [anymore]","ERROR",JOptionPane.ERROR_MESSAGE);
        else if(ex instanceof UnresolvedAddressException)
            JOptionPane.showMessageDialog(rightSplitPane,"This URL does not exist","ERROR",JOptionPane.ERROR_MESSAGE);
        else if(ex instanceof ConnectException)
            JOptionPane.showMessageDialog(rightSplitPane,"Error in connecting to server, Please check your internet connection.","ERROR",JOptionPane.ERROR_MESSAGE);
        else if(ex instanceof SocketException)
            JOptionPane.showMessageDialog(rightSplitPane,"There was a problem with sending a connection, Please check your internet connection.","ERROR",JOptionPane.ERROR_MESSAGE);
        else if(ex instanceof HttpConnectTimeoutException)
            JOptionPane.showMessageDialog(rightSplitPane,"Connection timeout.","ERROR",JOptionPane.ERROR_MESSAGE);
        else
            JOptionPane.showMessageDialog(rightSplitPane,"Unknown error. Message : "+ex.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void finished(Thread thread) {

    }
}

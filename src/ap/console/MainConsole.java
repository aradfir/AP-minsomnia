package ap.console;

import ap.ThreadCompletionListener;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class MainConsole {
    public static void main(String[] args) throws URISyntaxException {
        ArrayList<HttpRequestInfo> requestInfosToSend = CommandParser.parseRequest(args);
        if (requestInfosToSend == null)
            return;
        for (HttpRequestInfo requestInfo : requestInfosToSend) {
            if (requestInfo.isSaved()) {
                requestInfo.setSaved(false);
                try {
                    System.out.println("Request saved to " + FileUtils.saveRequest(requestInfo));
                }
                catch (IOException ex)
                {
                    System.err.println("Error in saving request file. this shouldnt happen. the request will still be sent.");
                }

            }
            HttpExecutor exec = new HttpExecutor(requestInfo);
            exec.addListener(new HttpCompletionListener());
            exec.start();
        }
    }
    public static void main(String[] args, ThreadCompletionListener listener) throws URISyntaxException {
        ArrayList<HttpRequestInfo> requestInfosToSend = CommandParser.parseRequest(args);
        if (requestInfosToSend == null)
            return;
        for (HttpRequestInfo requestInfo : requestInfosToSend) {
            if (requestInfo.isSaved()) {
                requestInfo.setSaved(false);
                try {
                    System.out.println("Request saved to " + FileUtils.saveRequest(requestInfo));
                }
                catch (IOException ex)
                {
                    System.err.println("Error in saving request file. this shouldnt happen. the request will still be sent.");
                }

            }
            HttpExecutor exec = new HttpExecutor(requestInfo);
            exec.addListener(listener);
            exec.start();
        }
    }
}
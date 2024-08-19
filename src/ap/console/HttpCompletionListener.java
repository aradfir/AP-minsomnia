package ap.console;

import ap.ThreadCompletionListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpResponse;
import java.nio.channels.UnresolvedAddressException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class HttpCompletionListener implements ThreadCompletionListener {

    private static String isUTF8(final byte[] inputBytes) {
        final String converted = new String(inputBytes, StandardCharsets.UTF_8);
        final byte[] outputBytes = converted.getBytes(StandardCharsets.UTF_8);
        return Arrays.equals(inputBytes, outputBytes) ? converted : null;
    }

    @Override
    public void finishedSuccessfully(Thread thread) {
        if (!(thread instanceof HttpExecutor)) {
            throw new IllegalArgumentException("the Thread is not a HttpExecutor!");
        }
        HttpResponse<InputStream> response = ((HttpExecutor) thread).getResponse();
        HttpRequestInfo requestInfo = ((HttpExecutor) thread).getReqInfo();
        System.out.println("Code : " + response.statusCode());
        System.out.println("Time: " + ((HttpExecutor) thread).getTimeInSeconds() + "s");
        if (requestInfo.isHeaderShown()) {
            System.out.println("HEADERS\n");
            for (String header : response.headers().map().keySet()) {
                System.out.println(header + "=" + response.headers().map().get(header).toString());
            }
        }
        if (requestInfo.isShowResponse()) {
            byte[] byteArray = null;
            try {
                byteArray = response.body().readAllBytes();
            } catch (Exception ex) {
                System.err.println("Error in reading the response's body! Your request was sent beforehand");
                System.exit(-1);
            }
            double size = byteArray.length;
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
            System.out.println("Body size: " + size + postfixSize);
            String responseText = isUTF8(byteArray.clone());
            if (responseText == null)
                responseText = "Body is not text!";
            System.out.println("Body:\n" + responseText);
            String outputName = requestInfo.getOutputName();
            String mimeType = "";
            if (response.headers().firstValue("content-type").isPresent())
                mimeType = response.headers().firstValue("content-type").get().split(";")[0];
            else if (response.headers().firstValue("Content-Type").isPresent())
                mimeType = response.headers().firstValue("Content-Type").get().split(";")[0];
            String outputExtension = MimeType.getExtension(mimeType);
            String outputDir = requestInfo.getCodeLocation() + "/saved/responses/";
            try {
                System.out.println("File saved to: " + FileUtils.saveToFile(outputDir, outputName, outputExtension, byteArray));
            } catch (Exception ex) {
                System.err.println("Error in saving body to file!");
                System.exit(-1);
            }
        }
    }

    @Override
    public void finishedException(Thread thread, Exception ex, String errorSegmentDescriptor) {
        if(ex instanceof FileNotFoundException)
            System.err.println("The upload file does not exist [anymore]");
        else if(ex instanceof UnresolvedAddressException)
            System.err.println("This URL does not exist");
        else if(ex instanceof ConnectException)
            System.err.println("Error in connecting to server, Please check your internet connection.");
        else if(ex instanceof SocketException)
            System.err.println("There was a problem with sending a connection, Please check your internet connection.");
        else if(ex instanceof HttpConnectTimeoutException)
            System.err.println("Connection timeout.");
        else
            System.err.println("Unknown error. Message : "+ex.getMessage());


    }

    @Override
    public void finished(Thread thread) {

    }
}

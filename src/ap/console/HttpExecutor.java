package ap.console;

import ap.ThreadCompletionListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;

/**
 * The type Http executor.
 */
public class HttpExecutor extends Thread {
    private HttpClient redirectClient;
    private HttpClient nonRedirectClient;
    private HttpRequestInfo requestInfo;
    /**
     * The Listeners.
     */
    ArrayList<ThreadCompletionListener> listeners = new ArrayList<>();
    private String exceptionDescriptor;
    private double timeInSeconds;
    private HttpResponse<InputStream> response;

    /**
     * Gets response.
     *
     * @return the response
     */
    public HttpResponse<InputStream> getResponse() {
        return response;
    }

    /**
     * Sets response.
     *
     * @param response the response
     */
    public void setResponse(HttpResponse<InputStream> response) {
        this.response = response;
    }

    /**
     * Gets request info.
     *
     * @return the request info
     */
    public HttpRequestInfo getRequestInfo() {
        return requestInfo;
    }

    /**
     * Sets request info.
     *
     * @param requestInfo the request info
     */
    public void setRequestInfo(HttpRequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }


    /**
     * Gets time in seconds.
     *
     * @return the time in seconds
     */
    public double getTimeInSeconds() {
        return timeInSeconds;
    }

    /**
     * Sets time in seconds.
     *
     * @param timeInSeconds the time in seconds
     */
    public void setTimeInSeconds(double timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }

    /**
     * Add listener.
     *
     * @param listener the listener
     */
    public void addListener(ThreadCompletionListener listener) {
        listeners.add(listener);
    }

    /**
     * Remove listener.
     *
     * @param listener the listener
     */
    public void removeListener(HttpCompletionListener listener) {
        listeners.remove(listener);
    }

    private void notifyListenersSuccess() {
        for (ThreadCompletionListener listener : listeners) {
            listener.finishedSuccessfully(this);
        }
    }

    private void notifyListenersFinish() {
        for (ThreadCompletionListener listener : listeners) {
            listener.finished(this);
        }
    }

    private void notifyListenersException(Exception ex) {
        for (ThreadCompletionListener listener : listeners) {
            listener.finishedException(this, ex, exceptionDescriptor);
        }
    }

    /**
     * Gets request info.
     *
     * @return the request info
     */
    public HttpRequestInfo getReqInfo() {
        return requestInfo;
    }

    /**
     * Sets request info.
     *
     * @param requestInfo the request info
     */
    public void setReqInfo(HttpRequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    /**
     * Gets redirect client.
     *
     * @return the redirect client
     */
    public HttpClient getRedirectClient() {
        return redirectClient;
    }

    /**
     * Gets non redirect client.
     *
     * @return the non redirect client
     */
    public HttpClient getNonRedirectClient() {
        return nonRedirectClient;
    }

    /**
     * Instantiates a new Http executor.
     */
    public HttpExecutor() {
        redirectClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .connectTimeout(Duration.ofMinutes(2))
                .build();
        nonRedirectClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NEVER)
                .connectTimeout(Duration.ofMinutes(2))
                .build();
    }

    /**
     * Instantiates a new Http executor.
     *
     * @param requestInfo the request info
     */
    public HttpExecutor(HttpRequestInfo requestInfo) {
        this();
        this.requestInfo = requestInfo;
    }

    /**
     * Executes the current requestInfo
     */
    private void executeCommand() throws IOException, InterruptedException {
        exceptionDescriptor = "toRequestError";
        HttpRequest request = requestInfo.toRequest();
        exceptionDescriptor = "requestSendError";
        Date beforeRequest = new Date();
        if (requestInfo.isFollowRequest())
            response = redirectClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
        else
            response = nonRedirectClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
        Date afterRequest = new Date();
        timeInSeconds = (afterRequest.getTime() - beforeRequest.getTime()) / 1000.0;
    }

    @Override
    public void run() {
        try {
            executeCommand();
            notifyListenersSuccess();
        } catch (Exception ex) {
            notifyListenersException(ex);
        } finally {
            notifyListenersFinish();
        }
    }
}

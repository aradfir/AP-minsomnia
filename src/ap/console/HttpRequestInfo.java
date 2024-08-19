package ap.console;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpRequest;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;

/**
 * The type Http request info.
 */
public class HttpRequestInfo implements Serializable {
    private URI uri;
    private String method;
    private String[] headers;
    //private String bodyInfo;
    private BodyTypes type;
    private boolean showResponse;
    private boolean followRequest;
    private boolean saved;
    private boolean isHeaderShown;
    private String outputName;
    private String codeLocation;
    private String groupName;
    private ArrayList<byte[]> byteBodyArray;

    @Override
    public String toString() {
        String res = "URL : " + uri.toString() + " | Method : " + method + " | Group :" + (groupName == null ? "NaN" : groupName) + " | Headers :";
        for (int i = 0; i < headers.length; i += 2)
            res += headers[i] + " = " + headers[i + 1] + " ; ";
        res += " | Body type : " + type.name() + " | Follow request? : " + (followRequest ? "Yes" : "No") + " | Show Response headers? " + (isHeaderShown ? "Yes" : "No") + " | Save response body as: " + (showResponse ? (outputName==null?"No name given":outputName ): "Dont save");
        return res;
    }

    /**
     * Gets code location.
     *
     * @return the code location
     */
    public String getCodeLocation() {
        return codeLocation;
    }

    /**
     * Sets code location.
     *
     * @param codeLocation the code location
     */
    public void setCodeLocation(String codeLocation) {
        this.codeLocation = codeLocation;
    }

    /**
     * Gets group name.
     *
     * @return the group name
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Sets group name.
     *
     * @param groupName the group name
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * Instantiates a new Http request info.
     *
     * @param uri           the uri
     * @param method        the method
     * @param headers       the headers
     * @param type          the type
     * @param showResponse  the show response
     * @param followRequest the follow request
     * @param saved         the saved
     * @param isHeaderShown the is header shown
     * @param outputName    the output name
     * @param codeLocation  the code location
     * @param byteBodyArray the byte body array
     * @param groupName     the group name
     */
    public HttpRequestInfo(URI uri, String method, String[] headers, BodyTypes type, boolean showResponse, boolean followRequest, boolean saved, boolean isHeaderShown, String outputName, String codeLocation, ArrayList<byte[]> byteBodyArray, String groupName) {
        this.uri = uri;
        this.method = method;
        this.headers = headers;
        this.type = type;
        this.showResponse = showResponse;
        this.followRequest = followRequest;
        this.saved = saved;
        this.isHeaderShown = isHeaderShown;
        this.outputName = outputName;
        this.codeLocation = codeLocation;
        this.byteBodyArray = byteBodyArray;
        this.groupName = groupName;
    }

    /**
     * turns HttpRequestInfo to executable HttpRequest.
     *
     * @return the http request
     * @throws FileNotFoundException the file not found exception, for raw file upload
     */
    public HttpRequest toRequest() throws FileNotFoundException {
        HttpRequest.BodyPublisher body;
        switch (type) {

            case NONE:
                body = HttpRequest.BodyPublishers.noBody();
                break;
            case FILE:
                body = HttpRequest.BodyPublishers.ofFile(Paths.get(new String(byteBodyArray.get(0))));
                break;
            case FORM:
                body = HttpRequest.BodyPublishers.ofByteArrays(byteBodyArray);
                break;
            case JSON:
                body = HttpRequest.BodyPublishers.ofString(new String(byteBodyArray.get(0)));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        return HttpRequest.newBuilder(uri).headers(headers).timeout(Duration.ofMinutes(2)).method(method, body).build();
    }

    /**
     * Gets uri.
     *
     * @return the uri
     */
    public URI getUri() {
        return uri;
    }

    /**
     * Sets uri.
     *
     * @param uri the uri
     */
    public void setUri(URI uri) {
        this.uri = uri;
    }

    /**
     * Gets method.
     *
     * @return the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * Sets method.
     *
     * @param method the method
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * Get headers string [ ].
     *
     * @return the string [ ]
     */
    public String[] getHeaders() {
        return headers;
    }

    /**
     * Sets headers.
     *
     * @param headers the headers
     */
    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public BodyTypes getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(BodyTypes type) {
        this.type = type;
    }

    /**
     * Gets output name.
     *
     * @return the output name
     */
    public String getOutputName() {
        return outputName;
    }

    /**
     * Sets output name.
     *
     * @param outputName the output name
     */
    public void setOutputName(String outputName) {
        this.outputName = outputName;
    }

    /**
     * Is header shown boolean.
     *
     * @return the boolean
     */
    public boolean isHeaderShown() {
        return isHeaderShown;
    }

    /**
     * Sets header shown.
     *
     * @param headerShown the header shown
     */
    public void setHeaderShown(boolean headerShown) {
        isHeaderShown = headerShown;
    }

    /**
     * Is show response boolean.
     *
     * @return the boolean
     */
    public boolean isShowResponse() {
        return showResponse;
    }

    /**
     * Sets show response.
     *
     * @param showResponse the show response
     */
    public void setShowResponse(boolean showResponse) {
        this.showResponse = showResponse;
    }

    /**
     * Is follow request boolean.
     *
     * @return the boolean
     */
    public boolean isFollowRequest() {
        return followRequest;
    }

    /**
     * Sets follow request.
     *
     * @param followRequest the follow request
     */
    public void setFollowRequest(boolean followRequest) {
        this.followRequest = followRequest;
    }

    /**
     * Is saved boolean.
     *
     * @return the boolean
     */
    public boolean isSaved() {
        return saved;
    }

    /**
     * Sets saved.
     *
     * @param saved the saved
     */
    public void setSaved(boolean saved) {
        this.saved = saved;
    }


}

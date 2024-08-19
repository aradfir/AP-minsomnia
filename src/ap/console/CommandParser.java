package ap.console;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * The type Command parser.
 */
public class CommandParser {
    private final static String[] validMethods = new String[]{"GET", "POST", "PUT", "DELETE", "PATCH"};

    private static final boolean DEBUG_MODE = false;

    /**
     * Formats the body of a multipart form
     * @param data the map of key values
     * @param boundary the boundary between key values
     * @return arraylist of bytes that the body
     */
    private static ArrayList<byte[]> formatMultiPart(Map<String, String> data, String boundary) throws IOException {
        ArrayList<byte[]> bytes = new ArrayList<byte[]>();
        String lineSep = "\r\n";
        byte[] separator = ("--" + boundary + "\r\nContent-Disposition: form-data; name=")
                .getBytes(StandardCharsets.UTF_8);
        for (String key : data.keySet()) {
            String value = data.get(key);
            bytes.add(separator);
            if (value.startsWith("@")) {
                Path fileAddress = Paths.get(value.substring(1));
                String mimeType = null;
                try {
                    mimeType = Files.probeContentType(fileAddress);
                } catch (IOException e) {
                    mimeType = "application/octet-stream";
                }
                bytes.add(("\"" + key + "\"; filename=\"" + fileAddress.getFileName()
                        + "\"" + lineSep + "Content-Type: " + mimeType + lineSep + lineSep).getBytes(StandardCharsets.UTF_8));

                    bytes.add(Files.readAllBytes(fileAddress));

                bytes.add(lineSep.getBytes(StandardCharsets.UTF_8));
            } else {
                bytes.add(("\"" + key + "\"" + lineSep + lineSep + value + lineSep).getBytes(StandardCharsets.UTF_8));
            }
        }
        bytes.add(("--" + boundary + "--").getBytes(StandardCharsets.UTF_8));
        return bytes;
    }

    private final static char[] MULTIPART_CHARS =
            "_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    .toCharArray();

    /**
     * Generate boundary string.
     *
     * @return the randomly generated boundary
     */
    protected static String generateBoundary() {
        StringBuilder buffer = new StringBuilder();
        Random rand = new Random();
        int count = rand.nextInt(6) + 1; // a random size from 1 to 6
        for (int i = 0; i < count; i++) {
            buffer.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
        }
        return "--------" + buffer.toString();
    }

    /**
     * Gets value for arguments that have to supply a value , like -M. removes the keys from args
     * @param args full args given to program
     * @param keys all the possible names for the argument we are looking for
     * @return the value provided, is null if empty
     */
    private static String getValueForOption(ArrayList<String> args, String... keys) {
        String res = null;
        for (String key : keys)
            if (args.contains(key)) {
                res = args.get(args.indexOf(key) + 1);
                args.remove(args.indexOf(key) + 1);
                args.remove(key);
                break;
            }
        return res;
    }

    /**
     * Gets value for arguments that shouldn't to supply a value , like -i . removes the flags from args
     * @param args full args given to program
     * @param flags all the possible names for the argument we are looking for
     * @return true if exists
     */
    static private boolean checkForFlagWithoutValue(ArrayList<String> args, String... flags) {
        boolean isTrue = false;
        for (String flag : flags) {
            isTrue = isTrue || args.contains(flag);
            while (args.contains(flag)) {
                args.remove(flag);
            }
        }

        return isTrue;
    }

    private static void addDefaultHeaders(ArrayList<String> headers) {
        headers.add("accept");
        headers.add("*/*");
        headers.add("user-agent");
        headers.add("jurl-client/0.1");
    }
    /**
     * Gets value for arguments that might or might not supply a value , like -O or -S . removes the flags from args
     * @param args full args given to program
     * @param keys all the possible names for the argument we are looking for
     * @return the value if it exists, null if it doesnt
     */
    private static String getValueForFlagWithOptionalParameter(ArrayList<String> args, String... keys) {
        String res = null;
        int possibleNameIndex = -1;
        for (String key : keys)
            while (args.contains(key)) {
                possibleNameIndex = args.indexOf(key);
                args.remove(key);
            }
        try {
            res = args.get(possibleNameIndex).toCharArray()[0] == '-' ? null : args.get(possibleNameIndex);
            if (res != null)
                args.remove(possibleNameIndex);
        } finally {
            return res;
        }
    }

    /**
     * List all of the requests saved for this location
     * @param codeLocation the location of the code
     */
    private static void listAll(String codeLocation) {
        int i = 1;
        FileUtils.FileReaderSequential rq = new FileUtils.FileReaderSequential(codeLocation);
        while (rq.hasNext()) {
            System.out.println(i++ + " | " + rq.next().toString());
        }
    }
    /**
     * List all of the requests saved for this location, within the group
     * @param codeLocation the location of the code
     * @param groupName the group to look through
     */
    private static void listGroup(String codeLocation, String groupName) {
        int i = 1;
        FileUtils.FileReaderSequential rq = new FileUtils.FileReaderSequential(codeLocation);
        while (rq.hasNext()) {
            HttpRequestInfo info = (HttpRequestInfo) rq.next();
            if (info.getGroupName() != null && info.getGroupName().equals(groupName))
                System.out.println(i++ + " | " + info.toString());
        }
    }

    /**
     * Finds the requests that are going to be fired
     * @param codeLocation the location of the code
     * @param groupName the name if the group to search through, null if search through all.
     * @param numbersToFind the indices to look for.
     * @return an arraylist of HttpRequestInfos to fire
     */
    private static ArrayList<HttpRequestInfo> fireFinder(String codeLocation, String groupName, ArrayList<Integer> numbersToFind) {
        int i = 0;
        ArrayList<HttpRequestInfo> foundRequests = new ArrayList<>();
        FileUtils.FileReaderSequential rq = new FileUtils.FileReaderSequential(codeLocation);
        while (rq.hasNext()) {
            HttpRequestInfo info = (HttpRequestInfo) rq.next();
            if (groupName == null)
                i++;
            else if (info.getGroupName() != null && info.getGroupName().equals(groupName))
                i++;
            if (numbersToFind.contains(i))
                foundRequests.add(info);
        }
        return foundRequests;
    }

    /**
     * Parse request args.
     *
     * @param argsArray the args array
     * @return the array list of requests to send
     * @throws URISyntaxException the uri syntax exception for issues with the URl
     */
    public static ArrayList<HttpRequestInfo> parseRequest(String... argsArray) throws URISyntaxException {

        ArrayList<String> args = new ArrayList<>();
        Collections.addAll(args, argsArray);
        if (args.contains("-h") || args.contains("--help")) {
            System.out.println("HELP MENU\n_________________________\nNOTES:[ ] shows an optional parameter.  \nNOTES:arguments that have | show all of the possible values for that argument.");
            System.out.println("[-U] %url%                                       : Sets the url to send the to. must add query parameters to the end of the url.");
            System.out.println("[-H|--headers \"%key%:%value%;%key2%:%value2%\"]   : Send headers with key value pairs.");
            System.out.println("[-d|--data \"%key%=%value%&key2%=%value2%\"]       : Send body as multipart form with key value pairs. You can send files by giving the value as @absolutePath .");
            System.out.println("[-j|--json %jsonData%]                           : Send body as a json text");
            System.out.println("[--upload \"%absolutePath%\"]                      : Send body as a raw file , located in absolutePath.");
            System.out.println("[-M|--method  GET|POST|PUT|PATCH|DELETE]         : Sets the type of the request. It is set to GET if not inputted.");
            System.out.println("[-S|--save] [groupname]                          : Saves the requests. You can add groupname to add this request to a grouping.");
            System.out.println("[-i]                                             : Shows the headers of the response.");
            System.out.println("[-O|--output] [outputFileName]                   : Saves the body of the output to a file. if outputFileName is provided, it will be the name of the output file. It also prints it out if its text.");
            System.out.println("[-f]                                             : Follows the redirects of the server.");
            System.out.println("[--help|-h]                                      : Shows this menu.");
            System.out.println("_________________________");
            System.out.println("list [group name]                                : Lists all of the requests");
            System.out.println("fire [group name] [indices to fire]              : Sends the requests that were saved.");
            return null;
        }

        if (args.contains("list")) {
            String groupName = getValueForFlagWithOptionalParameter(args, "list");   //args : code location (added in bash) / list / listnames
            if (groupName == null)
                listAll(args.get(0));
            else {
                listGroup(args.get(0), groupName);
            }
            return null;
        }
        ArrayList<HttpRequestInfo> res = new ArrayList<>();
        if (args.contains("fire")) {
            String fireGroupName = null;
            int fireNumStartIndex = -1;
            try {
                int firenum = Integer.parseInt(args.get(2));
                fireGroupName = null;
                fireNumStartIndex = 2;
            } catch (NumberFormatException ex) {
                fireGroupName = args.get(2);
                fireNumStartIndex = 3;
            } catch (IndexOutOfBoundsException ex) {
                System.err.println("Invalid fire command! you must give indices!");
                System.exit(-1);
            }
            ArrayList<Integer> indexesToFind = new ArrayList<>();
            for (int i = fireNumStartIndex; i < args.size(); i++) {
                try {
                    indexesToFind.add(Integer.parseInt(args.get(i)));
                } catch (Exception ex) {
                    System.err.println("Invalid fire command! you must give indices only after group name!");
                    System.exit(-1);
                }
            }
            ArrayList<HttpRequestInfo> foundRequests = fireFinder(args.get(0), fireGroupName, indexesToFind);
            if (foundRequests.size() != indexesToFind.size()) {
                System.err.println("Only " + foundRequests.size() + " of the requested requests were found. They will be fired.");
            }
            return foundRequests;

        }
        String boundary = generateBoundary();
        ArrayList<byte[]> bodyFinal = new ArrayList<>();
        String siteURL = null;
        String method = "GET";

        ArrayList<String> headers = new ArrayList<>();
        addDefaultHeaders(headers);

        String codeLocation = args.get(0);
        args.remove(0);

        boolean followRequests = checkForFlagWithoutValue(args, "-f");
        boolean save = args.contains("-S") || args.contains("--save");
        boolean headerOutput = checkForFlagWithoutValue(args, "-i");

        boolean bodyOutput = args.contains("-O") || args.contains("--output");

        String outputName = null;
        String groupName = null;

        String jsonBody = null;
        String dataBody = null;
        String uploadBody = null;
        BodyTypes bodyType = null;

        //find URL
        for (String arg : args) {
            if (arg.matches("(https?:\\/\\/)?(www\\.)[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,4}\\b([-a-zA-Z0-9@:%_\\+.~#?&\\/\\/=]*)|(https?:\\/\\/)?(www\\.)?(?!ww)[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,4}\\b([-a-zA-Z0-9@:%_\\+.~#?&\\/\\/=]*)")) {
                siteURL = arg;
                args.remove(arg);
                if (!siteURL.startsWith("http")) {
                    siteURL = "http://" + siteURL;
                }
                break;
            }
        }
        if (siteURL == null) {
            throw new IllegalArgumentException("Invalid URL!");
        }
        //CHECK FOR POSSIBLE OPTIONS

        if (bodyOutput) {
            outputName = getValueForFlagWithOptionalParameter(args, "-O", "--output");
        }
        if (save) {
            groupName = getValueForFlagWithOptionalParameter(args, "-S", "--save");
        }

        //Find Method and validate
        try {
            String tmp = getValueForOption(args, "-M", "--method");
            if (tmp != null)
                method = tmp;
        } catch (IndexOutOfBoundsException ex) {
            System.err.println("You forgot to mention your method!");
            System.exit(-1);
        }
        if (!Arrays.asList(validMethods).contains(method)) {
            try {
                throw new Exception("Invalid method!");
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
                System.exit(-1);
            }
        }
        //Find header value
        try {
            String headersRaw = getValueForOption(args, "-H", "--headers");
            if (headersRaw != null) {
                Collections.addAll(headers, headersRaw.split("[:;]"));
            }
        } catch (IndexOutOfBoundsException ex) {
            System.err.println("You forgot to mention your header!");
            System.exit(-1);
        }

        //Find each body method
        try {
            jsonBody = getValueForOption(args, "-j", "--json");
        } catch (IndexOutOfBoundsException ex) {
            System.err.println("You forgot to mention your json body!");
            System.exit(-1);
        }
        try {
            dataBody = getValueForOption(args, "-d", "--data");

        } catch (IndexOutOfBoundsException ex) {
            System.err.println("You forgot to mention your data body!");
            System.exit(-1);
        }
        try {
            uploadBody = getValueForOption(args, "--upload");
        } catch (IndexOutOfBoundsException ex) {
            System.err.println("You forgot to mention your file address!");
            System.exit(-1);
        }

        if (dataBody != null && uploadBody == null && jsonBody == null) {
            String[] keyValues = dataBody.split("&");
            HashMap<String, String> keyValuesMap = new HashMap<>();
            for (String keyValuePair : keyValues) {
                String[] keyValueSplitted = keyValuePair.split("=");
                keyValuesMap.put(keyValueSplitted[0], keyValueSplitted[1]);

            }
            try {
                bodyFinal = formatMultiPart(keyValuesMap, boundary);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Invalid file.");
            }
            bodyType = BodyTypes.FORM;
            headers.add("Content-Type");
            headers.add("multipart/form-data; boundary=" + boundary);
        } else if (dataBody == null && uploadBody != null && jsonBody == null) {
            headers.add("Content-Type");
            try {
                headers.add(Files.probeContentType(Paths.get(uploadBody)));
            } catch (Exception ex) {
                headers.add("application/octet-stream");
            }

            bodyFinal.add(uploadBody.getBytes());
            bodyType = BodyTypes.FILE;
        } else if (dataBody == null && uploadBody == null && jsonBody != null) {

            headers.add("Content-Type");
            headers.add(MimeType.getMimeType(".json"));
            bodyFinal.add(jsonBody.getBytes(StandardCharsets.UTF_8));
            bodyType = BodyTypes.JSON;
        } else if (dataBody == null && uploadBody == null && jsonBody == null) {
            bodyType = BodyTypes.NONE;

        } else {
            System.out.println("You've entered multiple bodies! exiting...");
            System.exit(-1);
        }

        if (headers.size() % 2 == 1) {
            System.err.println("The headers are missing some values!");
            System.exit(-1);
        }


        String[] finalHeaders = new String[headers.size()];
        String fullbody_text = "";
        for (byte[] stringSeg : bodyFinal) {
            fullbody_text += new String(stringSeg);
        }

        for (int i = 0; i < headers.size(); i++)
            finalHeaders[i] = headers.get(i);
        if (DEBUG_MODE) {
            System.out.println(siteURL);
            System.out.println(method);
            System.out.println("Save " + save);

            System.out.println("Display body : " + bodyOutput);
            System.out.println("file name:" + outputName);
            System.out.println("Display header : " + headerOutput);
            System.out.println("Follow " + followRequests);
            System.out.println("Headers : " + headers);
            if (bodyType != BodyTypes.NONE) {
                System.out.println("Body length: " + bodyFinal.size());
            }
            System.out.println("Code location" + codeLocation);
            System.out.println();
            System.out.println(Arrays.toString(finalHeaders));
            System.out.println(fullbody_text);
        }
        res.add(new HttpRequestInfo(new URI(siteURL), method, finalHeaders, bodyType, bodyOutput, followRequests, save, headerOutput, outputName, codeLocation, bodyFinal, groupName));
        return res;


    }
}


package ap.console;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * File utils for reading and writing to files.
 */
public class FileUtils {
    /**
     * Gets default file name.
     *
     * @return the default file name
     */
    public static String getDefaultFileName() {
        String pattern = "yyyy-MM-dd HH-mm-ss-SSS";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String date = simpleDateFormat.format(new Date());
        return "output_" + date;
    }

    /**
     * Save array of bytes as a file. Used for saving body response
     *
     * @param filePath  the file path
     * @param fileName  the file name
     * @param extension the extension
     * @param info      the information to be written to the file
     * @return the address of the file thats saved
     * @throws IOException throws error if theres a problem writing a file
     */
    public static String saveToFile(String filePath, String fileName, String extension, byte[] info) throws IOException {
        if (fileName == null)
            fileName = getDefaultFileName();
        if (!new File(filePath).exists())
            Files.createDirectories(Paths.get(filePath));
        String outputDir = filePath + fileName;
        String finalOutput = outputDir + extension;
        if (Paths.get(finalOutput).toFile().exists()) {

            int num = 2;
            while (new File(outputDir + "_" + num + extension).exists()) {
                num++;
            }
            finalOutput = outputDir + "_" + num + extension;
        }
        FileOutputStream fileWriter = new FileOutputStream(finalOutput);
        fileWriter.write(info);
        fileWriter.close();
        return new File(finalOutput).getAbsolutePath();
    }

    /**
     * Save request info.
     *
     * @param requestToSave the request to save
     * @return the absolute path of the saved file
     * @throws IOException throws error if theres a problem writing a file.
     */
    public static String saveRequest(HttpRequestInfo requestToSave) throws IOException {
        File saveDirectory = new File(requestToSave.getCodeLocation() + "/saved/requests/");
        if (!saveDirectory.exists())
            Files.createDirectories(saveDirectory.toPath());
        String name = (saveDirectory.list().length + 1) + ".req";
        FileOutputStream fos = new FileOutputStream(saveDirectory.getAbsolutePath() + File.separator + name);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeObject(requestToSave);
        oos.close();
        fos.close();
        return saveDirectory.getAbsolutePath() + File.separator + name;
    }

    /**
     * The type File reader sequential. Used to read files from /saved/requests/.
     */
    public static class FileReaderSequential implements Iterator {
        private int readNumber;
        private String requestsLocation;
        private static final String REQUEST_FILE_EXTENSION = ".req";

        /**
         * Instantiates a new File reader sequential.
         *
         * @param codeLocation the location of the code that is running this
         * @param readNumber   the number of files already read
         */
        public FileReaderSequential(String codeLocation, int readNumber) {
            this.readNumber = readNumber;
            this.requestsLocation = codeLocation + "/saved/requests/";
        }

        /**
         * Instantiates a new File reader sequential.
         *
         * @param codeLocation the location of the code that is running this
         */
        public FileReaderSequential(String codeLocation) {
            this(codeLocation, 0);
        }

        @Override
        public boolean hasNext() {

            return Paths.get(requestsLocation, Integer.toString(readNumber + 1) + REQUEST_FILE_EXTENSION).toFile().exists();
        }

        @Override
        public Object next() {
            readNumber++;

            try {
                ObjectInputStream oos = new ObjectInputStream(new FileInputStream(requestsLocation + readNumber + REQUEST_FILE_EXTENSION));
                return oos.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error in reading file " + requestsLocation + readNumber + REQUEST_FILE_EXTENSION);
            }
            return null;
        }
    }
}

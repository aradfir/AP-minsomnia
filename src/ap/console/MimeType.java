package ap.console;


import java.util.HashMap;

/**
 * The type Mime type.
 */
public class MimeType {
    private static HashMap<String,String> mimeTypeExtensions;

    /**
     * Gets mime type.
     *
     * @param extension the extension
     * @return the mime type
     */
    public static String getMimeType(String extension)
    {
        for(String mime:mimeTypeExtensions.keySet()){
            if(mimeTypeExtensions.get(mime).equals(extension)) {
                return mime;
            }
        }
        return "Auto";
    }

    /**
     * Gets extension.
     *
     * @param mimeType the mime type
     * @return the extension of the file to save
     */
    public static String getExtension(String mimeType)
    {
        return mimeTypeExtensions.getOrDefault(mimeType,null);
    }


    static{
        mimeTypeExtensions=new HashMap<>();
        mimeTypeExtensions.put("audio/aac",".aac");
        mimeTypeExtensions.put("application/x-abiword",".abw");
        mimeTypeExtensions.put("application/x-freearc",".arc");
        mimeTypeExtensions.put("video/x-msvideo",".avi");
        mimeTypeExtensions.put("application/vnd.amazon.ebook",".azw");
        mimeTypeExtensions.put("application/octet-stream",".bin");
        mimeTypeExtensions.put("image/bmp",".bmp");
        mimeTypeExtensions.put("application/x-bzip",".bz2");
        mimeTypeExtensions.put("application/x-csh","");
        mimeTypeExtensions.put("text/css",".css");
        mimeTypeExtensions.put("text/csv",".csv");
        mimeTypeExtensions.put("application/msword",".doc");
        mimeTypeExtensions.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document",".docx");
        mimeTypeExtensions.put("application/vnd.ms-fontobject",".eot");
        mimeTypeExtensions.put("application/epub+zip",".epub");
        mimeTypeExtensions.put("application/gzip",".gzi");
        mimeTypeExtensions.put("image/gif",".gif");
        mimeTypeExtensions.put("text/html",".htm");
        mimeTypeExtensions.put("image/vnd.microsoft.icon",".ico");
        mimeTypeExtensions.put("text/calendar",".ics");
        mimeTypeExtensions.put("application/java-archive",".jar");
        mimeTypeExtensions.put("image/jpeg",".jpg");
        mimeTypeExtensions.put("text/javascript",".js");
        mimeTypeExtensions.put("application/json",".json");
        mimeTypeExtensions.put("application/ld+json",".jsonld");
        mimeTypeExtensions.put("audio/midi",".midi");
        mimeTypeExtensions.put("audio/x-midi",".midi");
        mimeTypeExtensions.put("audio/mpeg",".mp3");
        mimeTypeExtensions.put("video/mpeg",".mpeg");
        mimeTypeExtensions.put("application/vnd.apple.installer+xml",".mpkg");
        mimeTypeExtensions.put("application/vnd.oasis.opendocument.presentation",".odp");
        mimeTypeExtensions.put("application/vnd.oasis.opendocument.spreadsheet",".ods");
        mimeTypeExtensions.put("application/vnd.oasis.opendocument.text",".odt");
        mimeTypeExtensions.put("audio/ogg",".oga");
        mimeTypeExtensions.put("video/ogg",".ogv");
        mimeTypeExtensions.put("application/ogg",".ogx");
        mimeTypeExtensions.put("audio/opus",".opus");
        mimeTypeExtensions.put("font/otf",".otf");
        mimeTypeExtensions.put("image/png",".png");
        mimeTypeExtensions.put("application/pdf",".pdf");
        mimeTypeExtensions.put("application/x-httpd-php",".php");
        mimeTypeExtensions.put("application/vnd.ms-powerpoint",".ppt");
        mimeTypeExtensions.put("application/vnd.openxmlformats-officedocument.presentationml.presentation",".pptx");
        mimeTypeExtensions.put("application/vnd.rar",".rar");
        mimeTypeExtensions.put("application/rtf",".rtf");
        mimeTypeExtensions.put("application/x-sh",".sh");
        mimeTypeExtensions.put("image/svg+xml",".svg");
        mimeTypeExtensions.put("application/x-shockwave-flash",".swf");
        mimeTypeExtensions.put("application/x-tar",".tar");
        mimeTypeExtensions.put("image/tiff",".tiff");
        mimeTypeExtensions.put("video/mp2t",".ts");
        mimeTypeExtensions.put("font/ttf",".ttf");
        mimeTypeExtensions.put("text/plain",".txt");
        mimeTypeExtensions.put("application/vnd.visio",".vsd");
        mimeTypeExtensions.put("audio/wav",".wav");
        mimeTypeExtensions.put("audio/webm",".weba");
        mimeTypeExtensions.put("video/webm",".webm");
        mimeTypeExtensions.put("image/webp",".webp");
        mimeTypeExtensions.put("font/woff",".woff");
        mimeTypeExtensions.put("font/woff2",".woff2");
        mimeTypeExtensions.put("application/xhtml+xml",".xhtml");
        mimeTypeExtensions.put("application/vnd.ms-excel",".xls");
        mimeTypeExtensions.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",".xlsx");
        mimeTypeExtensions.put("application/xml",".xml");
        mimeTypeExtensions.put("text/xml",".xml");
        mimeTypeExtensions.put("application/vnd.mozilla.xul+xml",".xul");
        mimeTypeExtensions.put("application/zip",".zip");
        mimeTypeExtensions.put("video/3gpp",".3gp");
        mimeTypeExtensions.put("audio/3gpp",".3gp");
        mimeTypeExtensions.put("video/3gpp2",".3g2");
        mimeTypeExtensions.put("audio/3gpp2",".3g2");
        mimeTypeExtensions.put("application/x-7z-compressed",".7z");
    }
}

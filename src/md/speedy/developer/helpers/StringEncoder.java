package md.speedy.developer.helpers;

import java.io.UnsupportedEncodingException;

/**
 * Created by Eduard Albu on 30.01.16 01 2016
 * project Speedy Backend
 */
public class StringEncoder {

    public static String decodeString(String string) {
        String parsedString = "";
        try {
            byte[] commentBytes = string.getBytes("UTF-8");
            parsedString = new String(commentBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return parsedString;
    }
}

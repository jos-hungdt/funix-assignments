package org.simple.mail.util;

import java.io.File;

public class FileUtil {
    public static boolean isFileExists(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }
}

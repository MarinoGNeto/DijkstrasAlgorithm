package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    private FileUtil() {
        // Empty constructor
    }

    public static List<String> readStringListFileByPath(String path) throws IOException {
        if (path == null || path.isBlank()) throw new IllegalArgumentException("The file path cannot be null or empty.");
        List<String> fileData = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8))) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                fileData.add(line);
            }

        } catch (IOException e) {
            throw new IOException("Error reading file: " + e.getMessage(), e);
        }

        return fileData;
    }
}

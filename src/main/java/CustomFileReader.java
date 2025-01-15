import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomFileReader {
    public List<String> readFiles(String[] filePaths) throws IOException {
        List<String> lines = new ArrayList<>();

        for (String filePath : filePaths) {
            try (LineIterator it = FileUtils.lineIterator(new File(filePath), "UTF-8")) {
                while (it.hasNext()) {
                    String line = it.nextLine();
                    if (!line.isEmpty()) { // игнорируем пустые строки
                        lines.add(line);
                    }
                }
            }
        }

        return lines;
    }
}
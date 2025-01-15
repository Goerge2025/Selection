package filterTask;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class DataFilter {
    private final Map<String, BufferedWriter> writers;
    private final boolean appendMode;
    private final String outputDir;
    private final String prefix;

    private int integerCount = 0;
    private double minInteger = Double.MAX_VALUE;
    private double maxInteger = Double.MIN_VALUE;
    private double sumIntegers = 0.0;

    private int floatCount = 0;
    private double minFloat = Double.MAX_VALUE;
    private double maxFloat = Double.MIN_VALUE;
    private double sumFloats = 0.0;

    private int stringCount = 0;
    private int shortestLength = Integer.MAX_VALUE;
    private int longestLength = 0;

    public DataFilter(boolean appendMode, String outputDir, String prefix) {
        this.appendMode = appendMode;
        this.outputDir = outputDir;
        this.prefix = prefix;
        this.writers = new HashMap<>();
    }

    public void processLines(List<String> lines) throws IOException {
        for (String line : lines) {
            classifyAndWrite(line);
        }
    }

    private void classifyAndWrite(String line) throws IOException {
        if (isInteger(line)) {
            writeToFile("integers.txt", line);
            updateIntegerStats(Integer.parseInt(line));
        } else if (isFloat(line)) {
            writeToFile("floats.txt", line);
            updateFloatStats(Double.parseDouble(line));
        } else {
            writeToFile("strings.txt", line);
            updateStringStats(line.length());
        }
    }

    private void updateIntegerStats(int value) {
        integerCount++;
        minInteger = Math.min(minInteger, value);
        maxInteger = Math.max(maxInteger, value);
        sumIntegers += value;
    }

    private void updateFloatStats(double value) {
        floatCount++;
        minFloat = Math.min(minFloat, value);
        maxFloat = Math.max(maxFloat, value);
        sumFloats += value;
    }

    private void updateStringStats(int length) {
        stringCount++;
        shortestLength = Math.min(shortestLength, length);
        longestLength = Math.max(longestLength, length);
    }

    private void writeToFile(String filename, String data) throws IOException {
        BufferedWriter writer = getWriter(filename);
        writer.write(data + System.lineSeparator());
    }

    private BufferedWriter getWriter(String filename) throws IOException {
        if (!writers.containsKey(filename)) {
            String fullPath = outputDir + "/" + prefix + filename;
            File file = new File(fullPath);
            File parentDir = file.getParentFile();
            if (!parentDir.exists() && !parentDir.mkdirs()) {
                throw new RuntimeException("Не удалось создать директорию: " + parentDir);
            }
            writers.put(filename, new BufferedWriter(new FileWriter(file, appendMode)));
        }
        return writers.get(filename);
    }

    public void closeWriters() throws IOException {
        for (BufferedWriter writer : writers.values()) {
            writer.close();
        }
    }

    public void printStatistics(boolean detailed) {
        DecimalFormat df = new DecimalFormat("#.##");

        System.out.println("\nСтатистика:");
        System.out.printf("Целые числа: %d\n", integerCount);
        if (detailed) {
            System.out.printf("Минимум: %s, Максимум: %s, Сумма: %s, Среднее: %s\n",
                    df.format(minInteger),
                    df.format(maxInteger),
                    df.format(sumIntegers),
                    df.format(integerCount == 0 ? 0 : sumIntegers / integerCount)
            );
        }

        System.out.printf("Вещественные числа: %d\n", floatCount);
        if (detailed) {
            System.out.printf("Минимум: %s, Максимум: %s, Сумма: %s, Среднее: %s\n",
                    df.format(minFloat),
                    df.format(maxFloat),
                    df.format(sumFloats),
                    df.format(floatCount == 0 ? 0 : sumFloats / floatCount)
            );
        }

        System.out.printf("Строки: %d\n", stringCount);
        if (detailed) {
            System.out.printf("Самая короткая: %d символов, Самая длинная: %d символов\n",
                    shortestLength,
                    longestLength
            );
        }
    }

    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isFloat(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
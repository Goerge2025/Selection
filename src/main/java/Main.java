import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@CommandLine.Command(name = "filter", mixinStandardHelpOptions = true, version = "File Filter 1.0")
public class Main implements Runnable {

    @Parameters(paramLabel = "FILES", description = "Файлы для фильтрации")
    private List<String> inputFiles = new ArrayList<>();

    @Option(names = {"-o", "--output-dir"}, description = "Путь к директории для выходных файлов")
    private String outputDir = ".";

    @Option(names = {"-p", "--prefix"}, description = "Префикс имен выходных файлов")
    private String prefix = "";

    @Option(names = {"-a", "--append"}, description = "Режим добавления в существующие файлы")
    private boolean appendMode = false;

    @Option(names = {"-s", "--short-statistics"}, description = "Краткая статистика")
    private boolean shortStatistics = false;

    @Override
    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Введите имена файлов (по одному в строке). Завершите ввод командой 'start':");

        String line;
        try {
            while (!(line = reader.readLine()).equalsIgnoreCase("start")) {
                inputFiles.add(line);
            }

            if (inputFiles.isEmpty()) {
                System.out.println("Вы не ввели ни одного файла.");
                return;
            }

            CustomFileReader customFileReader = new CustomFileReader();
            List<String> lines = customFileReader.readFiles(inputFiles.toArray(new String[0]));

            filterTask.OutputOptions options = new filterTask.OutputOptions(outputDir, prefix, appendMode);
            filterTask.DataFilter filter = new filterTask.DataFilter(options.isAppendMode(), options.getOutputDirectory(), options.getFilePrefix());
            filter.processLines(lines);
            filter.printStatistics(!shortStatistics);
            filter.closeWriters();
        } catch (IOException e) {
            System.err.println("Ошибка при обработке файлов: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        CommandLine.run(new Main(), args);
    }
}
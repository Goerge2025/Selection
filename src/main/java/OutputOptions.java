package filterTask;

public class OutputOptions {
    private String outputDirectory;
    private String filePrefix;
    private boolean appendMode;

    public OutputOptions(String outputDirectory, String filePrefix, boolean appendMode) {
        this.outputDirectory = outputDirectory;
        this.filePrefix = filePrefix;
        this.appendMode = appendMode;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public String getFilePrefix() {
        return filePrefix;
    }

    public boolean isAppendMode() {
        return appendMode;
    }
}
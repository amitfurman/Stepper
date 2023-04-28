package datadefinition.impl.file;

import java.io.File;

public class FileData {
    private String filePath;
    private File file;

    public FileData(String filePath) {
        this.filePath = filePath;
        file = new File(filePath);
    }

    public String toString() {
        return "\t" + filePath;
    }

}

package datadefinition.impl.file;

import java.io.File;
import java.io.Serializable;

public class FileData implements Serializable {
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

package DataDefinition;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class FileDataDefinition implements DataDefinitions {
    private String filePath;
    private Path path;
    private File file;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileDataDefinition)) //checking if the object o being compared is an instance of the FileDataDefinition class
            return false;                       //the "instanceof" operator is used to check if an object is an instance of a particular class or its subclass.
        FileDataDefinition other = (FileDataDefinition) o;
        // Compare relevant properties to determine equality
        return Objects.equals(this.fileName(), other.fileName());
    }

    public FileDataDefinition(String filePath) {
        setValue(filePath);
        file = new File(filePath);
    }

    public FileDataDefinition(FileDataDefinition other) {
        setValue(other.filePath);
        file =  new File(other.filePath);
    }

    public void setValue(String filePath) {
        this.filePath = filePath;
        this.path = Paths.get(filePath);
    }
    @Override
    public String getValue() {
        return filePath;
    }

    @Override
    public boolean isUserFriendly() {
        return false;
    }

    @Override
    public String name() {
        return "File";
    }

    public String fileName() {
        return file.getName();
    }

    @Override
    public String toString() {
        return filePath;
    }

    public boolean isExists() {
        return (file.exists()) ? true : false;
    }

    public boolean isFolder() {
        return (file.isDirectory()) ? true : false;
    }

    public File[] listOfFiles() {
        File[] files;
        files = file.listFiles();
        return files;
    }
    public boolean isFile() {
        return (file.isFile()) ? true : false;
    }

    @Override
    public String userPresentation() {
        return String.valueOf(filePath);
    }

    public DataDefinitions makeCopy() {
        return new FileDataDefinition(this.fileName());
    }

}
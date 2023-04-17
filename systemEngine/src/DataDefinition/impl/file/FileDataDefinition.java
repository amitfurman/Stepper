package DataDefinition.impl.file;

import DataDefinition.api.AbstractDataDefinition;
public class FileDataDefinition extends AbstractDataDefinition {

    public FileDataDefinition() {
        super("File", false, FileData.class);
    }
}

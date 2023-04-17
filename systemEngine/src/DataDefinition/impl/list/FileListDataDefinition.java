package DataDefinition.impl.list;

import DataDefinition.api.AbstractDataDefinition;

public class FileListDataDefinition extends AbstractDataDefinition {

    public FileListDataDefinition() {
        super("File List", false, FileListData.class);
    }
}

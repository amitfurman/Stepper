package datadefinition.impl.list;

import datadefinition.api.AbstractDataDefinition;

public class FileListDataDefinition extends AbstractDataDefinition {

    public FileListDataDefinition() {
        super("File List", false, FileListData.class);
    }
}

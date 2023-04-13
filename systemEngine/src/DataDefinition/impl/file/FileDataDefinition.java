package DataDefinition.impl.file;

import DataDefinition.api.AbstractDataDefinition;
import DataDefinition.api.DataDefinitions;
import DataDefinition.impl.relation.RelationData;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class FileDataDefinition extends AbstractDataDefinition {

    public FileDataDefinition() {
        super("File", false, FileData.class);
    }
}

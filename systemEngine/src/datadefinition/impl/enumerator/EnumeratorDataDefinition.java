package datadefinition.impl.enumerator;

import datadefinition.api.AbstractDataDefinition;
import datadefinition.impl.file.FileData;

public class EnumeratorDataDefinition  extends AbstractDataDefinition {
    public EnumeratorDataDefinition() {
        super("Enumerator", true, EnumeratorData.class);
    }

}

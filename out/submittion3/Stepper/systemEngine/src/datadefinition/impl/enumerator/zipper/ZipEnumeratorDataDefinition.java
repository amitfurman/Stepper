package datadefinition.impl.enumerator.zipper;

import datadefinition.api.AbstractDataDefinition;
import datadefinition.impl.enumerator.zipper.ZipEnumeratorData;

public class ZipEnumeratorDataDefinition extends AbstractDataDefinition {
    public ZipEnumeratorDataDefinition() {
        super("Enumerator", true, ZipEnumeratorData.class);
    }

}

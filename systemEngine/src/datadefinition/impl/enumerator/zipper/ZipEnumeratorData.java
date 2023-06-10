package datadefinition.impl.enumerator.zipper;

import datadefinition.impl.enumerator.StringEnumerator;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ZipEnumeratorData extends StringEnumerator {
    public ZipEnumeratorData() {
        add("ZIP");
        add("UNZIP");
    }
}

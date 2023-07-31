package datadefinition.impl.enumerator.method;

import datadefinition.impl.enumerator.StringEnumerator;

public class MethodEnumeratorData extends StringEnumerator {

    public MethodEnumeratorData(){
        add("GET");
        add("PUT");
        add("POST");
        add("DELETE");
    }


}

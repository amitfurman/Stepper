package datadefinition.impl.list;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class FileListData extends ListData<File>  {
    public FileListData(List<File> source) {
        super(source);
    }
}

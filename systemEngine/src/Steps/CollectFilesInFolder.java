package Steps;

import DataDefinition.FileDataDefinition;
import DataDefinition.ListDataDefinition;
import DataDefinition.NumberDataDefinition;
import DataDefinition.StringDataDefinition;

import java.io.File;
public class CollectFilesInFolder implements StepInterface{

    private StringDataDefinition FOLDER_NAME;
    private StringDataDefinition FILTER;
    private ListDataDefinition FILES_LIST = new ListDataDefinition<FileDataDefinition>();
    private NumberDataDefinition TOTAL_FOUND;

    @Override
    public boolean isReadOnly() {
        return true;
    }

    public CollectFilesInFolder (StringDataDefinition folderName)
    {
        FOLDER_NAME = folderName;
        TOTAL_FOUND = new NumberDataDefinition(0);
    }

    public CollectFilesInFolder (StringDataDefinition folderName , StringDataDefinition filter)
    {
        FOLDER_NAME = folderName;
        FILTER = filter;
        TOTAL_FOUND = new NumberDataDefinition(0);
    }

    public ListDataDefinition<FileDataDefinition> collector() {
        FileDataDefinition folder = new FileDataDefinition(FOLDER_NAME.getValue());
        if (!folder.isExists()) {
            System.out.println("Failed. The entered path is not exists.");
            return null;
        }
        if (!folder.isFolder()) {
            System.out.println("Failed. The entered path is not a folder.");
            return null;
        }

        //Before reading the files
        System.out.println("Reading folder " + FOLDER_NAME.getValue());
        if(!(FILTER == null)){
            System.out.println("content with filter " + FILTER.getValue()); }

        File[] files = folder.listOfFiles();

        if (files == null || files.length == 0) {
            System.out.println("Warning. The folder " + FOLDER_NAME + " does not contain any files.");
        }

        for (File file : files) {
            if (FILTER == null || file.getName().toLowerCase().endsWith(FILTER.getValue().toLowerCase())) {
                FILES_LIST.addElement(new FileDataDefinition(file.getPath()));
                TOTAL_FOUND.setValue(TOTAL_FOUND.getValue() + 1);
            }
        }

        //After reading the files
        System.out.println("Found " +  TOTAL_FOUND.getValue() +" files in folder matching the filter.");

        return FILES_LIST;
    }
}

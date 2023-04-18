
package Steps.impl;

import DataDefinition.DataDefinitionRegistry;
import DataDefinition.api.IO_NAMES;
import DataDefinition.impl.list.FileListData;
import Steps.api.AbstractStepDefinition;
import Steps.api.DataDefinitionDeclarationImpl;
import Steps.api.DataNecessity;
import Steps.api.StepResult;
import flow.execution.context.StepExecutionContext;

import java.io.File;

public class CollectFilesInFolder extends AbstractStepDefinition {

    public CollectFilesInFolder() {
        super("Collect Files In Folder", true);

        // step inputs
        addInput(new DataDefinitionDeclarationImpl("FOLDER_NAME", DataNecessity.MANDATORY, "Folder name to scan", DataDefinitionRegistry.STRING));
        addInput(new DataDefinitionDeclarationImpl("FILTER", DataNecessity.OPTIONAL, "Filter only these files", DataDefinitionRegistry.STRING));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("FILES_LIST", DataNecessity.NA, "Files list", DataDefinitionRegistry.FILE_LIST));
        addOutput(new DataDefinitionDeclarationImpl("TOTAL_FOUND ", DataNecessity.NA, "Total files found", DataDefinitionRegistry.NUMBER));
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        String folderPath = context.getDataValue(IO_NAMES.FOLDER_NAME, String.class);
        String filter = context.getDataValue(IO_NAMES.FILTER, String.class);
        FileListData FILES_LIST = null;
        int TotalFound = 0 ;
        File folder = new File(folderPath);

        if (!folder.exists()) {
            System.out.println("Failed. The entered path is not exists.");
            return StepResult.FAILURE;
        }

        if (!folder.isDirectory()) {
            System.out.println("Failed. The entered path is not a folder.");
            return StepResult.FAILURE;
        }

        //Before reading the files
        String log = ("Reading folder " + folderPath + "content with filter ");
        if(!(filter == null)) {log += filter; }
        else { log += "N/A" ;}

        File[] files = folder.listFiles();

        if( files.length == 0) {
            context.storeDataValue("FILES_LIST", null);
            context.storeDataValue("TOTAL_FOUND", TotalFound);
            System.out.println("Warning. The folder " + folder.getName() + " does not contain any files.");
            return StepResult.WARNING;
        }

        for (File file : files)  {
            if (filter == null || file.getName().toLowerCase().endsWith(filter.toLowerCase())) {
                FILES_LIST.getItems().add(file);
                TotalFound++;
            }
        }

        context.storeDataValue("FILES_LIST",FILES_LIST);
        context.storeDataValue("TOTAL_FOUND",TotalFound);

        //After reading the files
        System.out.println("Found " + TotalFound +" files in folder matching the filter.");

        return StepResult.SUCCESS;
    }



}


package Steps.impl;

import DataDefinition.DataDefinitionRegistry;
import DataDefinition.api.DataDefinitions;
import DataDefinition.api.IO_NAMES;
import DataDefinition.impl.file.FileDataDefinition;
import DataDefinition.impl.list.ListData;
import DataDefinition.impl.list.ListDataDefinition;
import DataDefinition.impl.NumberDataDefinition;
import DataDefinition.impl.StringDataDefinition;
import Steps.api.AbstractStepDefinition;
import Steps.api.DataDefinitionDeclarationImpl;
import Steps.api.DataNecessity;
import Steps.api.StepResult;
import flow.execution.context.StepExecutionContext;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CollectFilesInFolder extends AbstractStepDefinition {

    public CollectFilesInFolder() {
        super("Collect Files In Folder", true);

        // step inputs
        addInput(new DataDefinitionDeclarationImpl("FOLDER_NAME", DataNecessity.MANDATORY, "Folder name to scan", DataDefinitionRegistry.FILE));
        addInput(new DataDefinitionDeclarationImpl("FILTER", DataNecessity.OPTIONAL, "Filter only these files", DataDefinitionRegistry.STRING));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("FILES_LIST", DataNecessity.NA, "Files list", DataDefinitionRegistry.LIST));
        addOutput(new DataDefinitionDeclarationImpl("TOTAL_FOUND ", DataNecessity.NA, "Total files found", DataDefinitionRegistry.NUMBER));
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        File folder = context.getDataValue(IO_NAMES.FOLDER_NAME, File.class);
        String filter = context.getDataValue(IO_NAMES.FILTER, String.class);

        ListData FILES_LIST = new ListData();
       // List list = new ListDataDefinition<FileDataDefinition>();
        int TotalFound = 0 ;

        if (!folder.exists()) {
            System.out.println("Failed. The entered path is not exists.");
            return StepResult.FAILURE;
        }

        if (!folder.isDirectory()) {
            System.out.println("Failed. The entered path is not a folder.");
            return StepResult.FAILURE;
        }

        //Before reading the files
        String log = ("Reading folder " + folder.getName() + "content with filter ");
        if(!(filter == null)) {log += filter; }
        else { log += "N/A" ;}

        File[] files = folder.listFiles();

        if (files == null || files.length == 0) {
            context.storeDataValue("FILES_LIST", FILES_LIST);
            context.storeDataValue("TOTAL_FOUND", TotalFound);
            System.out.println("Warning. The folder " + folder.getName() + " does not contain any files.");
            return StepResult.WARNING;
        }

       /* for (File file : files) {
            if (filter == null || file.getName().toLowerCase().endsWith(filter.toLowerCase())) {
                DataDefinitionRegistry obj = DataDefinitionRegistry.FILE;

                FILES_LIST.list.add(DataDefinitionRegistry.FILE(file.getPath()));
                TotalFound++;

            }
        }

        context.storeDataValue("FILES_LIST",);
        context.storeDataValue("TOTAL_FOUND",TotalFound );
*/
        //After reading the files
        System.out.println("Found " + TotalFound +" files in folder matching the filter.");


        return StepResult.SUCCESS;
    }



}

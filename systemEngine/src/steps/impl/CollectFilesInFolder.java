
package steps.impl;

import datadefinition.DataDefinitionRegistry;
import datadefinition.api.IO_NAMES;
import datadefinition.impl.list.FileListData;
import steps.api.*;
import flow.execution.context.StepExecutionContext;

import java.io.File;
import java.time.Instant;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class CollectFilesInFolder extends AbstractStepDefinition {

    public CollectFilesInFolder() {
        super("Collect Files In Folder", true);

        // step inputs
        addInput(new DataDefinitionDeclarationImpl("FOLDER_NAME", DataNecessity.MANDATORY, "Folder name to scan", DataDefinitionRegistry.STRING));
        addInput(new DataDefinitionDeclarationImpl("FILTER", DataNecessity.OPTIONAL, "Filter only these files", DataDefinitionRegistry.STRING));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("FILES_LIST", DataNecessity.NA, "Files list", DataDefinitionRegistry.FILE_LIST));
        addOutput(new DataDefinitionDeclarationImpl("TOTAL_FOUND", DataNecessity.NA, "Total files found", DataDefinitionRegistry.NUMBER));
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        Instant start = Instant.now();
        String folderPath = context.getDataValue(IO_NAMES.FOLDER_NAME, String.class);
        String filter = context.getDataValue(IO_NAMES.FILTER, String.class);
        FileListData FILES_LIST = new FileListData(new ArrayList<File>());
        int TotalFound = 0 ;
        File folder = new File(folderPath);

        if (!folder.exists()) {
            context.storeLogLineAndSummaryLine("Step failed. The entered path is not exists.");
            context.storeStepTotalTime(start);
            return StepResult.FAILURE;
        }

        if (!folder.isDirectory()) {
            context.storeLogLineAndSummaryLine("Step failed. The entered path is not a folder.");
            context.storeStepTotalTime(start);
            return StepResult.FAILURE;
        }

        //Before reading the files
        String log = ("Reading folder " + folderPath + "content with filter ");
        if(!(filter == null)) {log += filter; }
        else { log += "N/A." ;}
        context.storeLogLine(log);

        File[] files = folder.listFiles();

        if( files.length == 0) {
            context.storeDataValue( "FILES_LIST", FILES_LIST);
            context.storeDataValue("TOTAL_FOUND", TotalFound);
            context.storeLogLineAndSummaryLine("Warning! The folder " + folder.getName() + " does not contain any files.");
            context.storeStepTotalTime(start);
            return StepResult.WARNING;
        }

        for (File file : files)  {
            if (filter == null || file.getName().toLowerCase().endsWith(filter.toLowerCase())) {
                FILES_LIST.getItems().add(file);
                TotalFound++;
            }
        }

        Duration totalTime = Duration.between(start, Instant.now());
        context.storeDataValue("FILES_LIST",FILES_LIST);
        context.storeDataValue("TOTAL_FOUND",TotalFound);

        //After reading the files
        context.storeLogLineAndSummaryLine("Found " + TotalFound +" files in folder matching the filter.");
        context.storeStepTotalTime(start);
        return StepResult.SUCCESS;
    }

}

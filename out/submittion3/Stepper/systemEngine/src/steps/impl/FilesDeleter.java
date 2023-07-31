package steps.impl;

import datadefinition.DataDefinitionRegistry;
import datadefinition.api.IO_NAMES;
import datadefinition.impl.list.*;
import datadefinition.impl.mapping.NumberMappingData;
import steps.api.AbstractStepDefinition;
import steps.api.DataDefinitionDeclarationImpl;
import steps.api.DataNecessity;
import steps.api.StepResult;
import flow.execution.context.StepExecutionContext;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

public class FilesDeleter extends AbstractStepDefinition {

    public FilesDeleter() {
        super("Files Deleter", false);

        // step inputs
        addInput(new DataDefinitionDeclarationImpl("FILES_LIST", DataNecessity.MANDATORY, "Files to delete", DataDefinitionRegistry.FILE_LIST));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("DELETED_LIST", DataNecessity.NA, "Files failed to be deleted", DataDefinitionRegistry.STRING_LIST));
        addOutput(new DataDefinitionDeclarationImpl("DELETION_STATS", DataNecessity.NA, "Deletion summary results", DataDefinitionRegistry.MAPPING2NUMBERS));
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        Instant start = Instant.now();
        FileListData filesToDelete = context.getDataValue(IO_NAMES.FILES_LIST, FileListData.class);

        StringListData DELETED_LIST = new StringListData(new ArrayList<String>());
        NumberMappingData DELETION_STATS = new NumberMappingData(new HashMap<>());
        DELETION_STATS.setItems(new HashMap<Number, Number>()); // initialize to empty map
        DELETION_STATS.getItems().put(0, 0);
        DELETION_STATS.getItems().put(1, 0);
        int totalFiles = filesToDelete.getItems().size(), deleteCount = 0;

        context.storeLogLine("About to start delete " + totalFiles + " files");

        if (totalFiles == 0) {
            context.storeLogLineAndSummaryLine("The list of files to delete is empty, so there are no files to delete.");
            context.storeStepTotalTime(start);
            context.storeDataValue("DELETED_LIST",DELETED_LIST);
            context.storeDataValue("DELETION_STATS",DELETION_STATS);
            return StepResult.SUCCESS;
        }

        for(File file : filesToDelete.getItems()){
            if (!file.delete()) {
                DELETED_LIST.getItems().add(file.getPath());
                context.storeLogLine("Failed to delete file" + file.getName());
            }
        }
        deleteCount = totalFiles - DELETED_LIST.getItems().size();

        DELETION_STATS.getItems().put(0, deleteCount); // Update value for key 0
        DELETION_STATS.getItems().put(1, (totalFiles - deleteCount)); // Update value for key 1

        context.storeDataValue("DELETED_LIST",DELETED_LIST);
        context.storeDataValue("DELETION_STATS",DELETION_STATS);

        if (DELETED_LIST.getItems().isEmpty()) {
            context.storeSummaryLine("All files were deleted successfully.");
            context.storeStepTotalTime(start);
            return StepResult.SUCCESS;
        }
        else if(totalFiles == DELETED_LIST.getItems().size()){
            context.storeLogLineAndSummaryLine("Step failed. All the files failed to be deleted.");
            context.storeStepTotalTime(start);
            return StepResult.FAILURE;
        }
        else {
            context.storeLogLineAndSummaryLine(deleteCount + " files deleted, " + (totalFiles - deleteCount) + " files failed to be deleted.\n" +
                    "List of files that failed to be deleted: \n" + DELETED_LIST );
            context.storeStepTotalTime(start);
            return StepResult.WARNING;
        }
    }
}

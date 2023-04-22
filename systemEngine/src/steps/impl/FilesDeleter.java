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
        FileListData filesToDelete = context.getDataValue(IO_NAMES.FILES_LIST, FileListData.class);
        StringListData DELETED_LIST = null;
        NumberMappingData DELETION_STATS = null;
        int totalFiles = filesToDelete.getItems().size(), deleteCount = 0;
        DELETION_STATS.getItems().put(0,0); //initialize

        context.storeLogLine("About to start delete " + totalFiles + " files");
/*

        FileListData TEMP_FILES_LIST;
        for (int i = 0; i < filesToDelete.getItems().size(); i++) {
           // TEMP_FILES_LIST.getItems().add(filesToDelete.getItems().stream())(new FileDataDefinition((FileDataDefinition) FILES_LIST.getElement(i)));
        }
*/

        for(File file : filesToDelete.getItems()){

            if (!file.delete()) {
                DELETED_LIST.getItems().add(file.getPath());
                context.storeLogLine("Failed to delete file" + file.getName());
            }
        }
        deleteCount = totalFiles - DELETED_LIST.getItems().size();

        DELETION_STATS.getItems().put(deleteCount , (totalFiles - deleteCount));
        //DELETION_STATS.getItems().put(0, deleteCount); // Update value for key 0
        //DELETION_STATS.getItems().put(1, (totalFiles - deleteCount)); // Update value for key 1

        context.storeDataValue("DELETED_LIST",DELETED_LIST);
        context.storeDataValue("DELETION_STATS",DELETION_STATS);

        if (DELETED_LIST.getItems().isEmpty()) {
            context.storeLogLine("All files were deleted successfully.");
            /////בדף לא רשום להוסיף לוג במקרה של הצלחה רק שורת סיכום הוספתי בכל מקרה
            return StepResult.SUCCESS;
        }
        else if(totalFiles == DELETED_LIST.getItems().size()){
            context.storeLogLine("Step failed. All the files failed to be deleted.");
            return StepResult.FAILURE;
        }
        else {
            context.storeLogLine(deleteCount + " files deleted, " + (totalFiles - deleteCount) + " files failed to be deleted.\n" +
                    "List of files that failed to be deleted: \n" + DELETED_LIST );
            return StepResult.WARNING;
        }

    }

}


/*        Iterator<FileDataDefinition> iterator = FILES_LIST.iterator();
        FileDataDefinition file = iterator.next();
        while (FILES_LIST.numberOfElements() >=1 || iterator.hasNext()) {

            if (!FILES_LIST.deleteElement(file)) {
                DELETED_LIST.addElement(file);
                System.out.println("Failed to delete file" + file.fileName());
            }

            if (FILES_LIST.numberOfElements() != 1) {
                file = iterator.next();
            }
        }

        FILES_LIST.forEach(fileDataDefinition -> {
            FileDataDefinition file = (FileDataDefinition) fileDataDefinition;

            if (!FILES_LIST.deleteElement(file)) {
                DELETED_LIST.addElement(file);
                System.out.println("Failed to delete file" + file.fileName());
            }
        });
*//*


        */
/*
        Iterator<FileDataDefinition> iterator = FILES_LIST.iterator();
        FileDataDefinition file = iterator.next();
        FileDataDefinition next;
        while (iterator.hasNext()) {
           next = iterator.next();

            if (!FILES_LIST.deleteElement(file)) {
                DELETED_LIST.addElement(file);
                System.out.println("Failed to delete file" + file.fileName());
                file = next;
            }
        }
        iterator = (Iterator<FileDataDefinition>) iterator.next();
        FILES_LIST = TEMP_FILES_LIST;
        FileDataDefinition file = (FileDataDefinition) fileDataDefinition;
        file = new FileDataDefinition(tempFile);
*/


package Steps.impl;

import DataDefinition.DataDefinitionRegistry;
import DataDefinition.api.IO_NAMES;
import DataDefinition.impl.list.FileListData;
import DataDefinition.impl.relation.RelationData;
import Steps.api.AbstractStepDefinition;
import Steps.api.DataDefinitionDeclarationImpl;
import Steps.api.DataNecessity;
import Steps.api.StepResult;
import flow.execution.context.StepExecutionContext;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilesRenamer extends AbstractStepDefinition {

    public FilesRenamer() {
        super("Files Renamer", false);

        // step inputs
        addInput(new DataDefinitionDeclarationImpl("FILES_TO_RENAME", DataNecessity.MANDATORY, "Files to rename", DataDefinitionRegistry.FILE_LIST));
        addInput(new DataDefinitionDeclarationImpl("PREFIX", DataNecessity.OPTIONAL, "Add this prefix", DataDefinitionRegistry.STRING));
        addInput(new DataDefinitionDeclarationImpl("SUFFIX", DataNecessity.OPTIONAL, "Append this suffix", DataDefinitionRegistry.STRING));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("RENAME_RESULT", DataNecessity.NA, "Rename operation summary", DataDefinitionRegistry.RELATION));

    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        FileListData filesToRename = context.getDataValue(IO_NAMES.FILES_TO_RENAME, FileListData.class);
        String prefix = context.getDataValue(IO_NAMES.PREFIX, String.class);
        String suffix = context.getDataValue(IO_NAMES.SUFFIX, String.class);

        List<String> columns = new ArrayList<>(Arrays.asList("Serial Number", "Original file's name", "file's name after change"));
        RelationData relation = new RelationData(columns);

        String log = "About to start renaming " + filesToRename.getItems().size() + " files. Adding prefix: " + prefix + "; adding suffix: " + suffix;

        if (filesToRename.getItems().isEmpty()) {
            String summaryLine1 = "FILES_TO_RENAME is empty, so there are no files to rename.";
            context.storeDataValue("RENAME_RESULT", relation);
            return StepResult.SUCCESS;
        }
        String failedFiles = null;
        int serialNumber = 1;
        for (File file : filesToRename.getItems()) {
            String originalFileName = file.getName();
            String fileNameAfterChange = originalFileName;

            if (prefix != null) { fileNameAfterChange = prefix + fileNameAfterChange;}

            if (suffix != null) {
                int lastIndex = fileNameAfterChange.lastIndexOf(".");  //If there are dots in the file name as well
                if (lastIndex != -1) {
                    fileNameAfterChange = fileNameAfterChange.substring(0, lastIndex) + suffix + fileNameAfterChange.substring(lastIndex);
                } else { fileNameAfterChange += suffix;}
            }

            boolean renamed = file.renameTo(new File(file.getParent(), fileNameAfterChange)); ////check the data's file
            serialNumber++;
            if (renamed) {
                relation.addRow(new ArrayList<>(Arrays.asList(String.valueOf(serialNumber), originalFileName, fileNameAfterChange)));
            } else {
                relation.addRow(new ArrayList<>(Arrays.asList(String.valueOf(serialNumber), originalFileName, originalFileName)));
                System.out.println("Problem renaming file " + originalFileName);
                failedFiles += (originalFileName + '\n');
            }
        }
        context.storeDataValue("RENAME_RESULT", relation);
        ////check
        if(relation.numOfRows() != filesToRename.getItems().size()) {
            String summaryLine2 = "There was a failure in converting the names of the following files: " + failedFiles + "\nThat's why the step ends with a warning";
            return StepResult.WARNING;
        }
        return StepResult.SUCCESS;
    }
}

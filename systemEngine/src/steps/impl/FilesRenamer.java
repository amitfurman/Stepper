package steps.impl;

import datadefinition.DataDefinitionRegistry;
import datadefinition.api.IO_NAMES;
import datadefinition.impl.list.FileListData;
import datadefinition.impl.relation.RelationData;
import steps.api.AbstractStepDefinition;
import steps.api.DataDefinitionDeclarationImpl;
import steps.api.DataNecessity;
import steps.api.StepResult;
import flow.execution.context.StepExecutionContext;

import java.io.File;
import java.time.Instant;
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
        Instant start = Instant.now();
        FileListData filesToRename = context.getDataValue(IO_NAMES.FILES_TO_RENAME, FileListData.class);
        String prefix = context.getDataValue(IO_NAMES.PREFIX, String.class);
        String suffix = context.getDataValue(IO_NAMES.SUFFIX, String.class);

        List<String> columns = new ArrayList<>(Arrays.asList("Serial Number", "Original file's name", "file's name after change"));
        RelationData relation = new RelationData(columns);

        context.storeLogLine("About to start rename " + filesToRename.getItems().size() + " files. Adding prefix: " + prefix + "; adding suffix: " + suffix);

        if (filesToRename.getItems().isEmpty()) {
            context.storeSummaryLine("FILES_TO_RENAME is empty, so there are no files to rename.");
            context.storeDataValue("RENAME_RESULT", relation);
            context.storeStepTotalTime(start);
            return StepResult.SUCCESS;
        }
        StringBuilder failedFiles = new StringBuilder();
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
                context.storeLogLine("Problem renaming file " + originalFileName);
                failedFiles.append(originalFileName).append('\n');
            }
        }
        context.storeDataValue("RENAME_RESULT", relation);

        if(relation.numOfRows() != filesToRename.getItems().size()) {
            context.storeSummaryLine("There was a failure in converting the names of the following files: " + failedFiles + "\nThat's why the step ends with a warning");
            context.storeStepTotalTime(start);
            return StepResult.WARNING;
        }
        context.storeStepTotalTime(start);
        return StepResult.SUCCESS;
    }
}

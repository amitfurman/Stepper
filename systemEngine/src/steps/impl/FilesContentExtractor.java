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

import java.io.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilesContentExtractor extends AbstractStepDefinition {

    public FilesContentExtractor() {
        super("Files Content Extractor",true);

        // step inputs
        addInput(new DataDefinitionDeclarationImpl("FILES_LIST", DataNecessity.MANDATORY, "Files to extract", DataDefinitionRegistry.FILE_LIST));
        addInput(new DataDefinitionDeclarationImpl("LINE", DataNecessity.MANDATORY, "Line number to extract", DataDefinitionRegistry.NUMBER));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("DATA", DataNecessity.NA, "Data extraction", DataDefinitionRegistry.RELATION));
    }
    @Override
    public StepResult invoke(StepExecutionContext context) {
        Instant start = Instant.now();
        FileListData filesToExtract = context.getDataValue(IO_NAMES.FILES_TO_RENAME, FileListData.class);
        int line = context.getDataValue(IO_NAMES.LINE,Integer.class);

        //List<String> columns = new ArrayList<>(Arrays.asList("Serial Number", "Original file's name", "Data file line") );
        List<String> columns = Arrays.asList("Serial Number", "Original file's name", "Data file line");
        RelationData relation = new RelationData(columns);

        if (filesToExtract.getItems().isEmpty()) {
            context.storeLogLineAndSummaryLine("The list of files to extract is empty, so there are no files to rename.");
        }
        else {
            for (File file : filesToExtract.getItems()) {
                context.storeLogLine("About to start work on file " + file.getName());
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));) {
                    String newLine;
                    int serialNumber = 1, currentLine = 1;
                    boolean foundLine = false;
                    while ((newLine = reader.readLine()) != null) {
                        if (currentLine == line) {
                            relation.addRow(new ArrayList<>(Arrays.asList(String.valueOf(serialNumber), file.getName(), newLine)));
                            foundLine = true;
                            serialNumber++;
                            break;
                        }
                        currentLine++;
                    }
                    if (!foundLine) {
                        context.storeLogLine("Problem extracting line number " + line +
                                " from file " + file.getName());
                    }
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        context.storeDataValue("DATA", relation);
        context.storeSummaryLine("The data extraction was created successfully from the files list.");
        context.storeStepTotalTime(start);
        return StepResult.SUCCESS;
    }
}

package Steps.impl;

import DataDefinition.DataDefinitionRegistry;
import DataDefinition.api.IO_NAMES;
import DataDefinition.impl.relation.RelationData;
import Steps.api.AbstractStepDefinition;
import Steps.api.DataDefinitionDeclarationImpl;
import Steps.api.DataNecessity;
import Steps.api.StepResult;
import flow.execution.context.StepExecutionContext;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilesContentExtractor extends AbstractStepDefinition {

    public FilesContentExtractor() {
        super("Files Content Extractor",true);

        // step inputs
        addInput(new DataDefinitionDeclarationImpl("FILES_LIST", DataNecessity.MANDATORY, "Files to extract", DataDefinitionRegistry.LIST));
        addInput(new DataDefinitionDeclarationImpl("LINE", DataNecessity.MANDATORY, "Line number to extract", DataDefinitionRegistry.NUMBER));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("DATA", DataNecessity.NA, "Data extraction", DataDefinitionRegistry.RELATION));

    }
    @Override
    public StepResult invoke(StepExecutionContext context) {
        List<File> filesToExtract = context.getDataValue(IO_NAMES.FILES_TO_RENAME, List.class);//or List<File>.class?????
        int line = context.getDataValue(IO_NAMES.LINE,Integer.class);

        List<String> columns = new ArrayList<>(Arrays.asList("Serial Number", "Original file's name", "Data file line") );
        RelationData relation = new RelationData(columns);

        if (filesToExtract.isEmpty()) {
            String summaryLine1 = "FILES_TO_RENAME is empty, so there are no files to rename.";
            context.storeDataValue("DATA", relation);
            return StepResult.SUCCESS;
        }
        int serialNumber =1;
        for (File file : filesToExtract) {
            String log = "About to start work on file " + file.getName();
            try (BufferedReader reader = new BufferedReader( new InputStreamReader( new FileInputStream(file))); ) {
                String newLine;
                int currentLine = 1;
                while ((newLine = reader.readLine()) != null) {
                    if (currentLine == line) {
                        relation.addRow(new ArrayList<>(Arrays.asList(String.valueOf(serialNumber), file.getName(), newLine)));
                        break;
                    }
                    currentLine++;
                    serialNumber++;
                }
                if (currentLine < line) {
                    System.out.println("Problem extracting line number " + line +
                            " from file " + file.getName() + ". Not such line found.");
                }

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }



        return null;
    }
}

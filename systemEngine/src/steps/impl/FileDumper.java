package steps.impl;

import datadefinition.DataDefinitionRegistry;
import datadefinition.api.IO_NAMES;
import steps.api.AbstractStepDefinition;
import steps.api.DataDefinitionDeclarationImpl;
import steps.api.DataNecessity;
import steps.api.StepResult;
import flow.execution.context.StepExecutionContext;

import java.io.*;
import java.time.Instant;

public class FileDumper extends AbstractStepDefinition {
    public FileDumper() {
        super("File Dumper", true);

        // step inputs
        addInput(new DataDefinitionDeclarationImpl("CONTENT", DataNecessity.MANDATORY, "Content", DataDefinitionRegistry.STRING));
        addInput(new DataDefinitionDeclarationImpl("FILE_NAME", DataNecessity.MANDATORY, "Target file path", DataDefinitionRegistry.STRING));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("RESULT", DataNecessity.NA, "File Creation Result", DataDefinitionRegistry.STRING));
    }
    
    @Override
    public StepResult invoke(StepExecutionContext context) {
        Instant start = Instant.now();
        String content = context.getDataValue(IO_NAMES.CONTENT, String.class);
        String fileName = context.getDataValue(IO_NAMES.FILE_NAME, String.class);
        File file = new File(fileName);

        if (file.exists()) {
            context.storeLogLineAndSummaryLine("Step failed because the target file path already exists.");
            context.storeDataValue("RESULT", StepResult.FAILURE.toString());
            context.storeStepTotalTime(start);
            return StepResult.FAILURE;
        }

        context.storeLogLine("About to create file named " + fileName);
        try (Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8")))
        { out.write(content);}
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (content.isEmpty()) {
            context.storeLogLineAndSummaryLine("Warning! Content is empty. File will be created empty.");
            context.storeDataValue("RESULT", StepResult.SUCCESS.toString());
            context.storeStepTotalTime(start);
            return StepResult.WARNING;
        }

        context.storeSummaryLine("The text file was created successfully at " + fileName);
        context.storeDataValue("RESULT", StepResult.SUCCESS.toString());
        context.storeStepTotalTime(start);
        context.storeStepTotalTime(start);
        return StepResult.SUCCESS;
    }
}

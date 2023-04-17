package Steps.impl;

import DataDefinition.DataDefinitionRegistry;
import DataDefinition.api.IO_NAMES;
import Steps.api.AbstractStepDefinition;
import Steps.api.DataDefinitionDeclarationImpl;
import Steps.api.DataNecessity;
import Steps.api.StepResult;
import flow.execution.context.StepExecutionContext;

import java.io.*;

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
        String content = context.getDataValue(IO_NAMES.CONTENT, String.class);
        String fileName = context.getDataValue(IO_NAMES.FILE_NAME, String.class);
        File file = new File(fileName);

        if (content.isEmpty()) {
            String log = "Warning: Content is empty. File will be created empty.";
            context.storeDataValue("RESULT", StepResult.SUCCESS.toString());
            return StepResult.WARNING;
        }

        // Check if file already exists
        if (file.exists()) {
            String log = StepResult.FAILURE + " because file already exists.";
            context.storeDataValue("RESULT", log);
            return StepResult.FAILURE;
        }

        System.out.println("About to create file named " + fileName + " before starting to work on the file");

        try (Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8")))
        { out.write(content);}
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        context.storeDataValue("RESULT", StepResult.SUCCESS.toString());
        return StepResult.SUCCESS;
    }
}

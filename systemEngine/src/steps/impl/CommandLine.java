package steps.impl;

import datadefinition.DataDefinitionRegistry;
import datadefinition.api.IO_NAMES;
import datadefinition.impl.list.FileListData;
import flow.execution.context.StepExecutionContext;
import steps.api.AbstractStepDefinition;
import steps.api.DataDefinitionDeclarationImpl;
import steps.api.DataNecessity;
import steps.api.StepResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandLine  extends AbstractStepDefinition {

    public CommandLine() {
        super("Command Line", false);

        // step inputs
        addInput(new DataDefinitionDeclarationImpl("COMMAND", DataNecessity.MANDATORY, "Command", DataDefinitionRegistry.STRING));
        addInput(new DataDefinitionDeclarationImpl("ARGUMENTS", DataNecessity.OPTIONAL, "Command arguments", DataDefinitionRegistry.STRING));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("RESULT", DataNecessity.NA, "Command output", DataDefinitionRegistry.STRING));
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        Instant start = Instant.now();
        String commandName = context.getDataValue(IO_NAMES.COMMAND, String.class);
        String arguments = context.getDataValue(IO_NAMES.ARGUMENTS, String.class);
        StringBuilder result = new StringBuilder();

        //before starting the operation
        context.storeLogLine("About to invoke"+ commandName + arguments);

        List<String> commandWithArgs = new ArrayList<>();
        commandWithArgs.add("cmd");     // You're running a cmd command
        commandWithArgs.add("/c");      // The /c argument makes cmd execute the following string
        commandWithArgs.add(commandName);

        if (arguments != null) {
            String[] args = arguments.split(" ");
            commandWithArgs.addAll(Arrays.asList(args));
        }

        ProcessBuilder processBuilder = new ProcessBuilder(commandWithArgs);

        processBuilder.redirectOutput(ProcessBuilder.Redirect.PIPE);

        try {
            Process process = processBuilder.start();
            process.waitFor();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line).append(System.lineSeparator());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        context.storeDataValue("RESULT", result.toString());

        context.storeStepTotalTime(start);
        return StepResult.SUCCESS;
    }


    /*
    public StepResult invoke(StepExecutionContext context) {
        Instant start = Instant.now();
        String commandName = context.getDataValue(IO_NAMES.COMMAND, String.class);
        String arguments = context.getDataValue(IO_NAMES.ARGUMENTS, String.class);
        StringBuilder result = new StringBuilder();
        ProcessBuilder processBuilder = new ProcessBuilder();

        //before starting the operation
        context.storeLogLine("About to invoke"+ commandName + arguments);

        if(arguments!=null) {
            processBuilder.command(commandName, arguments);
        }else{
            processBuilder.command(commandName);
        }

//          In the context of ProcessBuilder, the redirectOutput() method is used
//          to specify where the output of the subprocess should be directed.
//          The ProcessBuilder.Redirect.PIPE argument indicates that the output should
//          be piped into the Java process, allowing you to read it.
        processBuilder.redirectOutput(ProcessBuilder.Redirect.PIPE);

        try {
            Process process = processBuilder.start();
            process.waitFor();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line).append(System.lineSeparator());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

///if result is null? will .toString throw exception?
        context.storeDataValue("RESULT", result.toString());

        context.storeStepTotalTime(start);
        return StepResult.SUCCESS;
    }
    */
}

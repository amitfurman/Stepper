package steps.impl;

import datadefinition.DataDefinitionRegistry;
import datadefinition.api.IO_NAMES;
import datadefinition.impl.enumerator.zipper.ZipEnumeratorData;
import flow.execution.context.StepExecutionContext;
import steps.api.AbstractStepDefinition;
import steps.api.DataDefinitionDeclarationImpl;
import steps.api.DataNecessity;
import steps.api.StepResult;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Zipper extends AbstractStepDefinition {
    public Zipper() {
        super("Zipper", false);

        // step inputs
        addInput(new DataDefinitionDeclarationImpl("SOURCE", DataNecessity.MANDATORY, "Source", DataDefinitionRegistry.STRING));
        addInput(new DataDefinitionDeclarationImpl("OPERATION", DataNecessity.MANDATORY, "Operation type", DataDefinitionRegistry.STRING));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("RESULT", DataNecessity.NA, "Zip operation result", DataDefinitionRegistry.STRING));
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        Instant start = Instant.now();
        String source = context.getDataValue(IO_NAMES.SOURCE, String.class);
        String operation = context.getDataValue(IO_NAMES.OPERATION, String.class);

        System.out.println(source);
        System.out.println(operation);
        context.storeLogLine("About to perform operation " + operation + " on source " + source);

        if (!Files.exists(Paths.get(source))) {
            context.storeLogLineAndSummaryLine("Source does not exist.");
            context.storeDataValue("RESULT",StepResult.FAILURE.toString());
            context.storeStepTotalTime(start);
            return StepResult.FAILURE;
        }

        if (operation.equalsIgnoreCase("ZIP")) {
            return zip(source, context, start);
        } else if (operation.equalsIgnoreCase("UNZIP")) {
            return unzip(source, context, start);
        } else {
            context.storeLogLineAndSummaryLine("Invalid operation type.");
            context.storeDataValue("RESULT",StepResult.FAILURE.toString());
            context.storeStepTotalTime(start);
            return StepResult.FAILURE;
        }
    }

    private StepResult zip(String source, StepExecutionContext context, Instant start) {
        try {
            Path sourcePath = Paths.get(source);
            Path zipPath = sourcePath.resolveSibling(sourcePath.getFileName() + ".zip");

            if (Files.isDirectory(sourcePath)) {
                zipDirectory(sourcePath, zipPath);
            } else {
                zipFile(sourcePath, zipPath);
            }
            context.storeDataValue("RESULT",StepResult.SUCCESS.toString());
            context.storeStepTotalTime(start);
            return StepResult.SUCCESS;
        } catch (IOException e) {
            context.storeLogLineAndSummaryLine("Failed to perform ZIP operation: " + e.getMessage());
            context.storeDataValue("RESULT",StepResult.FAILURE.toString());
            context.storeStepTotalTime(start);
            return StepResult.FAILURE;
        }
    }

    private void zipDirectory(Path sourceDir, Path zipPath) throws IOException {
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipPath.toFile()))) {
            Files.walk(sourceDir)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(sourceDir.relativize(path).toString());
                        try {
                            zipOut.putNextEntry(zipEntry);
                            Files.copy(path, zipOut);
                            zipOut.closeEntry();
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
        }
    }

    private void zipFile(Path sourceFile, Path zipPath) throws IOException {
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipPath.toFile()))) {
            ZipEntry zipEntry = new ZipEntry(sourceFile.getFileName().toString());
            zipOut.putNextEntry(zipEntry);
            Files.copy(sourceFile, zipOut);
            zipOut.closeEntry();
        }
    }

    private StepResult unzip(String source, StepExecutionContext context, Instant start) {
        try {
            Path sourcePath = Paths.get(source);
            Path destinationDir = sourcePath.toAbsolutePath().getParent();

            if (!Files.isDirectory(destinationDir)) {
                context.storeLogLineAndSummaryLine("Destination directory does not exist.");
                context.storeDataValue("RESULT",StepResult.FAILURE.toString());
                context.storeStepTotalTime(start);
                return StepResult.FAILURE;
            }

            unzipFile(sourcePath, destinationDir);
            context.storeDataValue("RESULT",StepResult.SUCCESS.toString());
            context.storeStepTotalTime(start);
            return StepResult.SUCCESS;
        } catch (IOException e) {
            context.storeLogLineAndSummaryLine("Failed to perform UNZIP operation: " + e.getMessage());
            context.storeDataValue("RESULT",StepResult.FAILURE.toString());
            context.storeStepTotalTime(start);
            return StepResult.FAILURE;
        }
    }

    private void unzipFile(Path sourceFile, Path destinationDir) throws IOException {
        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(sourceFile.toFile()))) {
            ZipEntry entry = zipIn.getNextEntry();
            while (entry != null) {
                Path filePath = destinationDir.resolve(entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectories(filePath);
                } else {
                    Files.createDirectories(filePath.getParent());
                    Files.copy(zipIn, filePath, StandardCopyOption.REPLACE_EXISTING);
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        }
    }
}




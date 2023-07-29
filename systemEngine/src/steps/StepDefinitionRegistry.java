package steps;

import steps.api.StepDefinition;
import steps.impl.*;

public enum StepDefinitionRegistry {
    SPEND_SOME_TIME(new SpendSomeTime()),
    COLLECT_FILES_IN_FOLDER(new CollectFilesInFolder()),
    FILES_DELETER(new FilesDeleter()),
    FILES_RENAMER(new FilesRenamer()),
    FILES_CONTENT_EXTRACTOR(new FilesContentExtractor()),
    CSV_EXPORTER(new CSVExporter()),
    PROPERTIES_EXPORTER(new Properties()),
    FILE_DUMPER(new FileDumper()),
    ZIPPER(new Zipper()),
    COMMAND_LINE(new CommandLine()),
    HTTP_CALL(new HTTPCall()),
    TO_JSON(new ToJson()),
    JSON_DATA_EXTRACTOR(new JsonDataExtractor())

    ;

    private final StepDefinition stepDefinition;

    StepDefinitionRegistry(StepDefinition stepDefinition) {
        this.stepDefinition = stepDefinition;
    }

    public StepDefinition getStepDefinition() {
        return stepDefinition;
    }
}
package Steps;

import Steps.api.StepDefinition;
import Steps.impl.*;
import com.sun.org.apache.bcel.internal.generic.NEW;

public enum StepDefinitionRegistry {
    SPEND_SOME_TIME(new SpendSomeTime()),
    COLLECT_FILES_IN_FOLDER(new CollectFilesInFolder()),
    FILE_DELETER(new FilesDeleter()),
    FILE_RENAMER (new FilesRenamer()),
    FILES_CONTENT_EXTRACTOR(new FilesContentExtractor()),
    CSV_EXPORTER(new CSVExporter()),
    PROPERTIES_EXPORTER(new Properties()),
    FILE_DUMPER(new FileDumper()),
    ;

    private final StepDefinition stepDefinition;

    StepDefinitionRegistry(StepDefinition stepDefinition) {
        this.stepDefinition = stepDefinition;
    }

    public StepDefinition getStepDefinition() {
        return stepDefinition;
    }
}
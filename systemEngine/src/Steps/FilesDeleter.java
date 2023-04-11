package Steps;

import DataDefinition.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class FilesDeleter implements StepInterface{

    private ListDataDefinition<FileDataDefinition> FILES_LIST;
    private ListDataDefinition DELETED_LIST;
    private MappingDataDefinition <NumberDataDefinition,NumberDataDefinition> DELETION_STATS;
    NumberDataDefinition totalFiles;
    NumberDataDefinition deleteCount;

    @Override
    public boolean isReadOnly() {
        return false;
    }

    public FilesDeleter(ListDataDefinition filesList) {
        FILES_LIST =  filesList;
        NumberDataDefinition car = new NumberDataDefinition(0);
        NumberDataDefinition cdr = new NumberDataDefinition(0);
        DELETION_STATS = new MappingDataDefinition(car, cdr);
        totalFiles = new NumberDataDefinition(FILES_LIST.numberOfElements());
        deleteCount =  new NumberDataDefinition(0);
    }

    public void deleteFiles() {
        ListDataDefinition<StringDataDefinition> DELETED_LIST = new ListDataDefinition<>();
        //log before delete
        System.out.println("About to start delete " + totalFiles.getValue() + " files");

        ListDataDefinition<FileDataDefinition> TEMP_FILES_LIST = new ListDataDefinition<FileDataDefinition>();
        for (int i = 0; i < FILES_LIST.numberOfElements(); i++) {
            TEMP_FILES_LIST.addElement(new FileDataDefinition((FileDataDefinition) FILES_LIST.getElement(i)));
        }

        for(FileDataDefinition file : TEMP_FILES_LIST){

            if (!FILES_LIST.deleteElementByVal(file)) {
                DELETED_LIST.addElement(file);
                System.out.println("Failed to delete file" + file.fileName());
            }
        }

        deleteCount.setValue((totalFiles.getValue()-DELETED_LIST.numberOfElements()));

        if (DELETED_LIST.isEmpty()) {
            System.out.println("All files were deleted successfully.");
        }
        else if(totalFiles.getValue() == DELETED_LIST.numberOfElements()){
            System.out.println("Warning. All the files failed to be deleted.");
            return;
        }
        else {
            System.out.println(deleteCount.getValue() + " files deleted, " + (totalFiles.getValue() -deleteCount.getValue()) + " files failed to be deleted");
            System.out.println("Files failed to be deleted: \n" + DELETED_LIST.userPresentation());
        }
        DELETION_STATS.setCar(deleteCount);
        DELETION_STATS.setCdr(new NumberDataDefinition(totalFiles.getValue() - deleteCount.getValue()));
    }
}

/*        Iterator<FileDataDefinition> iterator = FILES_LIST.iterator();
        FileDataDefinition file = iterator.next();
        while (FILES_LIST.numberOfElements() >=1 || iterator.hasNext()) {

            if (!FILES_LIST.deleteElement(file)) {
                DELETED_LIST.addElement(file);
                System.out.println("Failed to delete file" + file.fileName());
            }

            if (FILES_LIST.numberOfElements() != 1) {
                file = iterator.next();
            }
        }

        FILES_LIST.forEach(fileDataDefinition -> {
            FileDataDefinition file = (FileDataDefinition) fileDataDefinition;

            if (!FILES_LIST.deleteElement(file)) {
                DELETED_LIST.addElement(file);
                System.out.println("Failed to delete file" + file.fileName());
            }
        });
*/

        /*
        Iterator<FileDataDefinition> iterator = FILES_LIST.iterator();
        FileDataDefinition file = iterator.next();
        FileDataDefinition next;
        while (iterator.hasNext()) {
           next = iterator.next();

            if (!FILES_LIST.deleteElement(file)) {
                DELETED_LIST.addElement(file);
                System.out.println("Failed to delete file" + file.fileName());
                file = next;
            }
        }
        iterator = (Iterator<FileDataDefinition>) iterator.next();
        FILES_LIST = TEMP_FILES_LIST;
        FileDataDefinition file = (FileDataDefinition) fileDataDefinition;
        file = new FileDataDefinition(tempFile);
*/

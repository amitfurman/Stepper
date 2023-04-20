package DataDefinition.api;

import DataDefinition.DataDefinitionRegistry;

import java.util.HashMap;
import java.util.Map;

public class IO_NAMES {
   public final static HashMap<String, DataDefinitions> name2DataDefinition = new HashMap<>();
   public final static String TIME_TO_SPEND = "TIME_TO_SPEND";
   public final static String FOLDER_NAME = "FOLDER_NAME";
   public final static String FILTER = "FILTER";
   public final static String FILES_LIST = "FILES_LIST";
   public final static String TOTAL_FOUND = "TOTAL_FOUND";
   public final static String DELETED_FILES = "DELETED_FILES";
   public final static String DELETION_STATS = "DELETION_STATS";
   public final static String FILES_TO_RENAME = "FILES_TO_RENAME";
   public final static String PREFIX = "PREFIX";
   public final static String SUFFIX = "SUFFIX";
   public final static String RENAME_RESULT = "RENAME_RESULT";
   public final static String SOURCE = "SOURCE";
   public final static String RESULT = "RESULT";
   public final static String CONTENT = "CONTENT";
   public final static String FILE_NAME = "FILE_NAME";
   public final static String LINE = "LINE";
   public final static String DATA = "DATA";

   static {
    name2DataDefinition.put(TIME_TO_SPEND, DataDefinitionRegistry.NUMBER);
    name2DataDefinition.put(FOLDER_NAME, DataDefinitionRegistry.STRING);
    name2DataDefinition.put(FILTER, DataDefinitionRegistry.STRING);
    name2DataDefinition.put(FILES_LIST, DataDefinitionRegistry.LIST);
    name2DataDefinition.put(TOTAL_FOUND, DataDefinitionRegistry.NUMBER);
    name2DataDefinition.put(DELETED_FILES, DataDefinitionRegistry.LIST);
    name2DataDefinition.put(DELETION_STATS, DataDefinitionRegistry.MAPPING);
    name2DataDefinition.put(FILES_TO_RENAME, DataDefinitionRegistry.LIST);
    name2DataDefinition.put(PREFIX, DataDefinitionRegistry.STRING);
    name2DataDefinition.put(SUFFIX, DataDefinitionRegistry.STRING);
    name2DataDefinition.put(RENAME_RESULT, DataDefinitionRegistry.RELATION);
    name2DataDefinition.put(SOURCE, DataDefinitionRegistry.RELATION);
    name2DataDefinition.put(RESULT, DataDefinitionRegistry.STRING);
    name2DataDefinition.put(CONTENT, DataDefinitionRegistry.STRING);
    name2DataDefinition.put(FILE_NAME, DataDefinitionRegistry.STRING);
    name2DataDefinition.put(LINE, DataDefinitionRegistry.NUMBER);
    name2DataDefinition.put(DATA, DataDefinitionRegistry.RELATION);
   }
}
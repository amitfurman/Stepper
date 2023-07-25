package util;


import com.google.gson.Gson;

public class Constants {

    // global constants
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public final static String JHON_DOE = "<Anonymous>";
    public final static int REFRESH_RATE = 2000;
    public final static String CHAT_LINE_FORMATTING = "%tH:%tM:%tS | %.10s: %s%n";

    // fxml locations
    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "javafx/login/login.fxml";

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/Servlets_Web_exploded";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/login";
    public final static String USERS_LIST = FULL_SERVER_PATH + "/userslist";
    public final static String ROLES_LIST = FULL_SERVER_PATH + "/roles";
    public final static String UPLOAD_FILE = FULL_SERVER_PATH + "/upload-file";
    public final static String NEW_ROLE = FULL_SERVER_PATH + "/new-role";
    public final static String UPDATE_ROLE = FULL_SERVER_PATH + "/update-role";
    public final static String ROLES_LIST_PER_USER = FULL_SERVER_PATH + "/roles-list-per-user";
    public final static String FLOWS_IN_ROLES_SERVLET = FULL_SERVER_PATH + "/flows-in-roles";
    public final static String ALL_FLOWS_SERVLET = FULL_SERVER_PATH + "/all-flows";
    public final static String FREE_INPUTS_BY_FLOW_NAME = FULL_SERVER_PATH + "/free-inputs-by-flow-name";
    public final static String ACTIVATE_FLOW = FULL_SERVER_PATH + "/activate-flow";
    public final static String USER_INFO_SERVLET = FULL_SERVER_PATH + "/user-info";
    public final static String GET_DTO_FLOW_EXECUTION_SERVLET = FULL_SERVER_PATH + "/flow-execution-data";
    public final static String UPDATE_USER = FULL_SERVER_PATH + "/update-user";

    public final static String IS_MANAGER_SERVLET = FULL_SERVER_PATH + "/is-manager";

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();
}

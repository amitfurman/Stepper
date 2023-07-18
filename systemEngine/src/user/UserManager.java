package user;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UserManager {

    private final Set<String> usersSet;
    private final Set<UserDefinition> usersDefinitionSet;


    public UserManager() {
        usersSet = new HashSet<>();
        usersDefinitionSet = new HashSet<>();
    }

    public synchronized void addUser(String username) {
        usersSet.add(username);
        usersDefinitionSet.add(new UserDefinition(username, false));
    }

    public synchronized void removeUser(String username) {
        usersSet.remove(username);
    }

    public synchronized Set<String> getUsersNames() {
        return Collections.unmodifiableSet(usersSet);
    }

    public synchronized Set<UserDefinition> getUsers() {
        return Collections.unmodifiableSet(usersDefinitionSet);
    }

    public boolean isUserExists(String username) {
        return usersSet.contains(username);
    }
}

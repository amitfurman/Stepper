package user;

import dto.DTORole;
import dto.DTOUserInfo;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public DTOUserInfo getUserInfo(String userName) {
        UserDefinition user =  usersDefinitionSet.stream().filter(user1 -> user1.getUsername().equals(userName)).findFirst().get();

        Set<DTORole> roles = new HashSet<DTORole>();

        user.getRoles().stream().forEach(role ->{
            roles.add(new DTORole( role.getName(), role.getFlowsInRole()));

        });
        return new DTOUserInfo(user.getUsername(),roles, user.getExecutedFlows());
    }

    public Boolean getIfUserIsManager(String userName) {
        UserDefinition user =  usersDefinitionSet.stream().filter(user1 -> user1.getUsername().equals(userName)).findFirst().get();
        return user.isManager();
    }
}

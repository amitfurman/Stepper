package user;

import java.util.HashSet;
import java.util.Set;

public class UserDefinition {
    String username;
    boolean isManager;
    Set<String> roles;

    public UserDefinition(String username, boolean isManager) {
        this.username = username;
        this.isManager = isManager;
        this.roles = new HashSet<>();
    }

    public String getUsername() {return username;}
    public boolean isManager() { return isManager;}
    public void addRole(String role) {roles.add(role);}
    public Set<String> getRoles(){return roles;};
}

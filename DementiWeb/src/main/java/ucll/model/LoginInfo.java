package ucll.model;

import java.util.UUID;

public class LoginInfo {

    private String username;
    private String Password;
    private UUID personID;


    private ROLE role;

    public LoginInfo(String username, String password, ROLE role){
        setPassword(password);
        setUsername(username);
        this.role = role;
    }

    public static LoginInfo LoginInfomaker(String username, String password, ROLE role, UUID id){
        LoginInfo loginf = new LoginInfo(username, password, role);
        loginf.setPersonID(id);
        return loginf;
    }
    public ROLE getRole() {
        return role;
    }

    public void setRole(ROLE role) {
        this.role = role;
    }

    public UUID getPersonID() {
        return personID;
    }

    public void setPersonID(UUID personID) {
        this.personID = personID;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public boolean equals(LoginInfo info){
        if(info.getUsername().equals(getUsername()) && info.getPassword().equals(getPassword())) return true;
        else return false;
    }
}

package ucll.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class LoginInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public UUID id;

    private String username;
    private String Password;
    private UUID personID;


    private ROLE role;

    public LoginInfo() {
    }

    public LoginInfo(String username, String password, ROLE role){
        setId(null);
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}

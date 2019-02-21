package ucll.model;

import java.util.UUID;

public class LoginInfo {

    private String username;
    private String Password;
    private UUID patientID;

    public LoginInfo(String username, String password){
        setPassword(password);
        setUsername(username);
    }

    public static LoginInfo LoginInfomaker(String username, String password, UUID id){
        LoginInfo loginf = new LoginInfo(username, password);
        loginf.setPatientID(id);
        return loginf;
    }

    public UUID getPatientID() {
        return patientID;
    }

    public void setPatientID(UUID patientID) {
        this.patientID = patientID;
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

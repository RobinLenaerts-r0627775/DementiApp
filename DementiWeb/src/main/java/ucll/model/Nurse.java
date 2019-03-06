package ucll.model;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
@Entity
public class Nurse implements Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public UUID nurseID;
    public String firstName,lastName;
    @Column(length = 600)
    public String password;
    public ROLE role;
    @Column(length = 600)
    public String hash;

    public Nurse(){}

    public Nurse(String firstName, String lastName, String password){
        setFirstName(firstName);
        setLastName(lastName);
        setPassword(password);
        this.role = ROLE.NURSE;
    }

    private String hashPassword(String password){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return generatedPassword;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    public UUID getNurseID() {
        return nurseID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        setHash(hashPassword(password));
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}

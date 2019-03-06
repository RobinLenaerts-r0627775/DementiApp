package ucll.model;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

@Entity
public class Patient implements Person{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public UUID patientId;
    public String firstName,lastName;
    //public String birthDate;
    //public int dementiaLevel;

    public UUID profilePicture;
    @Column(length = 600)
    private String password;
    @Column(length = 600)
    public String hash;

    public ROLE role;

    public Patient(){}

    public Patient(UUID patientId, String firstName, String lastName, /*String birthDate, int dementiaLevel,*/ UUID profilePicture, String password) {
        setPatientId(patientId);
        setFirstName(firstName);
        setLastName(lastName);
        //setBirthDate(birthDate);
        //setDementiaLevel(dementiaLevel);
        setProfile(profilePicture);
        setPassword(password);
        this.role = ROLE.PATIENT;
    }

    private String hashPassword(String password){
        if (password != null) {
            String generatedPassword = null;
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-512");
                byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < bytes.length; i++) {
                    sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
                }
                generatedPassword = sb.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return generatedPassword;
        } else {
            return password;
        }
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    /*public String getBirthDate() {
        return birthDate;
    }*/

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /*public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }*/

    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientID) {
        this.patientId = patientID;
    }

    /*public int getDementiaLevel() {
        return dementiaLevel;
    }

    public void setDementiaLevel(int dementiaLevel) {
        this.dementiaLevel = dementiaLevel;
    }*/

    public UUID getProfilePicture() {
        return profilePicture;
    }

    public void setProfile(UUID profile) {
        this.profilePicture = profile;
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

package ucll.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Entity
public class Patient implements Person{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public UUID patientId;
    public String firstName,lastName;
    public String birthDate;
    public int dementiaLevel;

    public UUID profilePicture;
    public String password;


    public Patient(){}

    public Patient(UUID patientId, String firstName, String lastName, String birthDate, int dementiaLevel, UUID profilePicture, String password) {
        setPatientId(patientId);
        setFirstName(firstName);
        setLastName(lastName);
        setBirthDate(birthDate);
        setDementiaLevel(dementiaLevel);
        setProfile(profilePicture);
        setPassword(password);
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getBirthDate() {
        return birthDate;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientID) {
        this.patientId = patientID;
    }

    public int getDementiaLevel() {
        return dementiaLevel;
    }

    public void setDementiaLevel(int dementiaLevel) {
        this.dementiaLevel = dementiaLevel;
    }

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
    }
}

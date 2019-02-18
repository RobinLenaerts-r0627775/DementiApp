package ucll.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
public class Patient implements Person{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public UUID patientId;
    public String firstName,lastName;
    public Date birthDate;
    public int dementiaLevel;
    public File profile;
    public String password;


    public Patient(){}

    public Patient(UUID patientId, String firstName, String lastName, Date birthDate, int dementiaLevel, File photo, String password) {
        setPatientId(patientId);
        setFirstName(firstName);
        setLastName(lastName);
        setBirthDate(birthDate);
        setDementiaLevel(dementiaLevel);
        setProfile(photo);
        setPassword(password);
    }

    public String getProfilePath(){
        return profile.getPath();
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
    public Date getBirthDate() {
        return birthDate;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthDate(Date birthDate) {
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

    public File getProfile() {
        return profile;
    }

    public void setProfile(File profile) {
        this.profile = profile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

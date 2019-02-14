package ucll.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
public class Patient implements Person{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public UUID patientID;
    public String firstName,lastName;
    public Date birthDate;
    public int dementiaLevel;
    public List<UUID> mediaFiles;


    public Patient(){}

    public Patient(UUID patientID, String firstName, String lastName, Date birthDate, int dementiaLevel, List<UUID> mediaFiles) {
        setPatientID(patientID);
        setFirstName(firstName);
        setLastName(lastName);
        setBirthDate(birthDate);
        setDementiaLevel(dementiaLevel);
        setMediaFiles(mediaFiles);
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

    public UUID getPatientID() {
        return patientID;
    }

    public void setPatientID(UUID patientID) {
        this.patientID = patientID;
    }

    public int getDementiaLevel() {
        return dementiaLevel;
    }

    public void setDementiaLevel(int dementiaLevel) {
        this.dementiaLevel = dementiaLevel;
    }

    public List<UUID> getMediaFiles() {
        return mediaFiles;
    }

    public void setMediaFiles(List<UUID> mediaFiles) {
        if (mediaFiles == null) mediaFiles = new ArrayList<>();
        this.mediaFiles = mediaFiles;
    }
}

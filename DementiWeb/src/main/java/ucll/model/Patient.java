package ucll.model;

import javax.persistence.*;
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
    @Column (length = 200000)
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

    private String getProfilePictureBase64(){
        if (profile.getPath().length() > 52){
            String result =  profile.getPath().substring(51);
            result = result.replace("\n", "")/*.replace("\r", "")*/;
            return result;
        }

        return profile.getPath();
    }

    public String getProfilePicture(){
        String result = profile.getPath();
        if (result.length() > 52){
            switch (result.charAt(52)){
                case 's':
                    result = result.substring(58); //TODO
                    break;

                case 'd':
                    result = getProfilePictureBase64();
                    System.out.println(result);
                    break;
            }
        }
        return result;
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

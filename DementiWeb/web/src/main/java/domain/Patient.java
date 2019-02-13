package domain;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Patient {
    public UUID patientId;
    public String firstName, lastName;
    public Date birthYear;
    public int dementiaLevel;

    public Patient(UUID patientId, String firstName, String lastName, Date birthYear, int dementiaLevel) {
        setBirthYear(birthYear);
        setDementiaLevel(dementiaLevel);
        setFirstName(firstName);
        setLastName(lastName);
        setPatientId(patientId);
    }

    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) throw new DomainException("Invalid firstname");
        else this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) throw new DomainException("Invalid lastname");
        else this.lastName = lastName;
    }

    public Date getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Date birthYear) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (birthYear == null || birthYear.getYear() < currentYear - 150) throw new DomainException("Invalid birth year");
        else this.birthYear = birthYear;
    }

    public int getDementiaLevel() {
        return dementiaLevel;
    }

    public void setDementiaLevel(int dementiaLevel) {
        this.dementiaLevel = dementiaLevel;
    }
}

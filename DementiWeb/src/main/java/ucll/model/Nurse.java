package ucll.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;
@Entity
public class Nurse implements Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public UUID nurseID;
    public String firstName,lastName;
    public String password;
    public ROLE role;

    public Nurse(){}

    public Nurse(String firstName, String lastName, String password){
        setFirstName(firstName);
        setLastName(lastName);
        setPassword(password);
        this.role = ROLE.NURSE;
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
    }
}

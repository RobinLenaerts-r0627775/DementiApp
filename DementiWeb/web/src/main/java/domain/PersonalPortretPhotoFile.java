package domain;

import java.io.File;
import java.util.UUID;

public class PersonalPortretPhotoFile extends PersonalPhotoFile {
    public String firstName,lastName, relation;

    public PersonalPortretPhotoFile(UUID patientId, UUID fileId, File file, String firstName, String lastName, String relation) {
        super(patientId, fileId, file);
        this.firstName = firstName;
        this.lastName = lastName;
        this.relation = relation;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}

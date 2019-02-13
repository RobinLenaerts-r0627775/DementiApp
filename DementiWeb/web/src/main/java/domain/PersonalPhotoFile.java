package domain;

import java.io.File;
import java.util.UUID;

public abstract class PersonalPhotoFile extends PersonalFile{

    public PersonalPhotoFile(UUID patientId, UUID fileId, File file) {
        super(patientId, fileId, file);
    }
}

package domain;

import java.io.File;
import java.util.UUID;

public class PersonalVideoFile extends PersonalFile{
    public PersonalVideoFile(UUID patientId, UUID fileId, File file) {
        super(patientId, fileId, file);
    }
}

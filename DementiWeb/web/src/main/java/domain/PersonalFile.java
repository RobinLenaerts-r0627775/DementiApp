package domain;

import java.io.File;
import java.util.UUID;

public abstract class PersonalFile {
    public UUID patientId, fileId;
    public File file;

    public PersonalFile(UUID patientId, UUID fileId, File file) {
        this.patientId = patientId;
        this.fileId = fileId;
        this.file = file;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public UUID getFileId() {
        return fileId;
    }

    public void setFileId(UUID fileId) {
        this.fileId = fileId;
    }
}

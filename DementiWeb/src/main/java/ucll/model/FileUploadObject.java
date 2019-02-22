package ucll.model;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class FileUploadObject {
    public byte[] file;
    public UUID patientId;
    public String extension;

    public FileUploadObject() {
    }

    public FileUploadObject(byte[] file, UUID patientId, String extension) {
        this.file = file;
        this.patientId = patientId;
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }
}

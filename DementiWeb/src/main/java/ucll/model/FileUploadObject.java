package ucll.model;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class FileUploadObject {
    public byte[] file;
    public UUID patientId;
    public String extension;
    public String description, category;

    public FileUploadObject() {
    }

    public FileUploadObject(byte[] file, UUID patientId, String extension, String description, String category) {
        this.file = file;
        this.patientId = patientId;
        this.extension = extension;
        this.description = description;
        this.category = category;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

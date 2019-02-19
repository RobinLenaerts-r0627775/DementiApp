package ucll.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.File;
import java.util.UUID;

@Entity
public class MediaFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public UUID mediaId;

    public UUID patientId;
    public File file;
    //public boolean playableType; // Video True, Image False

    public MediaFile(){}

    public MediaFile(UUID mediaId, UUID patientId, File file) {
        this.mediaId = mediaId;
        this.patientId = patientId;
        this.file = file;
    }

    public UUID getMediaId() {
        return mediaId;
    }

    public void setMediaId(UUID mediaId) {
        this.mediaId = mediaId;
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
}

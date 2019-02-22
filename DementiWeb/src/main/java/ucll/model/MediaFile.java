package ucll.model;

import javax.persistence.*;
import java.io.File;
import java.util.UUID;

@Entity
public class MediaFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public UUID mediaId;

    public UUID patientId;
    @Column(length = 200000)
    public File file;
    //public boolean playableType; // Video True, Image False

    public MediaFile(){}

    public MediaFile(UUID mediaId, UUID patientId, File file) {
        this.mediaId = mediaId;
        this.patientId = patientId;
        this.file = file;
    }

    /*private String getProfilePictureBase64(){
        if (file.getPath().length() > 52){
            String result =  file.getPath().substring(51);
            result = result.replace("\n", "").replace("\r", "");
            return result;
        }

        return file.getPath();
    }

    public String getProfilePicture(){
        String result = file.getPath();
        if (result.length() > 52){
            switch (result.charAt(52)){
                case 's':
                    result = result.substring(58); //TODO
                    break;

                case 'd':
                    result = getProfilePictureBase64();
                    break;
            }
        }
        return result;
    }*/

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

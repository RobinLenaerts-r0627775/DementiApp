package ucll.model;

import javax.persistence.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
    public String description;
    public String category;

    public MediaFile(){
    }


    public MediaFile(UUID mediaId, UUID patientId, File file, String description, String category) {
        this.mediaId = mediaId;
        this.patientId = patientId;
        this.file = file;
        this.description = description;
        setCategory(category);
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

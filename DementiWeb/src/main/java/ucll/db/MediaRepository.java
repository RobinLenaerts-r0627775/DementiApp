package ucll.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ucll.model.MediaFile;

import java.util.ArrayList;
import java.util.UUID;
@Repository
public interface MediaRepository extends CrudRepository<MediaFile, UUID> {
    public ArrayList<MediaFile> getAllByPatientId(UUID patientId);
    public ArrayList<MediaFile> getAllByPatientIdAndCategory(UUID patientId, String category);
    public MediaFile getFirstByPatientId(UUID patientId); // returns the default profile picture if id == null
}

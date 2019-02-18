package ucll.db;

import org.springframework.data.repository.CrudRepository;
import ucll.model.MediaFile;

import java.util.UUID;

public interface MediaRepository extends CrudRepository<MediaFile, UUID> {
}

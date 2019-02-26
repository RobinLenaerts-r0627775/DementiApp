package ucll.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ucll.model.MediaFile;

import java.util.UUID;
@Repository
public interface MediaRepository extends CrudRepository<MediaFile, UUID> {
}

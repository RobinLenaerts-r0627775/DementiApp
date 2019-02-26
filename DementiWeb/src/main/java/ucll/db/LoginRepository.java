package ucll.db;

import org.springframework.data.repository.CrudRepository;
import ucll.model.LoginInfo;
import ucll.model.MediaFile;

import java.util.UUID;

public interface LoginRepository extends CrudRepository<LoginInfo, UUID> {
}

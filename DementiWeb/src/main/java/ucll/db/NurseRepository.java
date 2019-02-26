package ucll.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ucll.model.Nurse;
import ucll.model.Patient;

import java.util.UUID;
@Repository
public interface NurseRepository extends CrudRepository <Nurse, UUID>{
}

package ucll.db;
import org.springframework.data.repository.CrudRepository;
import ucll.model.Patient;

import java.util.UUID;

public interface PatientRepository extends CrudRepository <Patient, UUID>{
}

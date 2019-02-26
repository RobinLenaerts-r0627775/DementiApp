package ucll.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ucll.model.Patient;

import java.util.UUID;
@Repository
public interface PatientRepository extends CrudRepository <Patient, UUID>{
}

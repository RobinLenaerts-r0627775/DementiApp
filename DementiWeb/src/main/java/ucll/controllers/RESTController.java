package ucll.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import ucll.db.PatientRepository;
import ucll.model.Patient;

import javax.naming.ServiceUnavailableException;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class RESTController {

    @Autowired //Inject?
    private PatientRepository patientRepository;

    @Autowired
    public RESTController(PatientRepository patientRepository){
        this.patientRepository = patientRepository;
        setTestData();
    }

    public void setTestData(){
        UUID knownUUID = UUID.fromString("6fb55fca-b947-4985-b0af-e96e832a2d66");
        System.out.println(knownUUID);
        Patient desire = new Patient(UUID.randomUUID(), "Désire", "Klaas", null, 1);
        Patient germain = new Patient(knownUUID, "Germain", "Van Hier", null, 1);
        Patient palmyr = new Patient(UUID.randomUUID(), "Palmyr", "Leysens", null, 2);

        patientRepository.save(desire);
        patientRepository.save(germain);
        patientRepository.save(palmyr);
    }

    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getPatients(){
        return ResponseEntity.ok(StreamSupport.stream(patientRepository.findAll().spliterator(), false)
                .peek(patient -> patient.setMediaFiles(null))
                .collect(Collectors.toList()));
    }

    @GetMapping("/patients/{patientId}")
    public ResponseEntity<Patient> getPatient(@PathVariable UUID patientId)throws RestClientException{
        Optional<Patient> op = patientRepository.findById(patientId);
        if (op.isPresent())
        {
            return ResponseEntity.ok(op.get());
        }

        else return ResponseEntity.notFound().build();
    }

    @PostMapping("/patients")
    @Transactional
    public ResponseEntity<Patient> postPatient(@RequestBody Patient patient) {
        Patient result = patientRepository.save(patient);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/patients/{patientId}")
    public ResponseEntity<Patient> putPatient(@PathVariable UUID patientId, @RequestBody Patient patient) {
        if (patientRepository.existsById(patientId))
            if (patientId.equals(patient.getPatientID())) {
                Patient result = patientRepository.save(patient);
                return ResponseEntity.ok(result);
            }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/patients/{patientId}")
    public boolean deletePatient(@PathVariable UUID patientId) throws RestClientException{
        Optional<Patient> op = patientRepository.findById(patientId);
        if (op.isPresent()) {
            patientRepository.delete(op.get());
            return true;
        }
        else return false;
    }
}

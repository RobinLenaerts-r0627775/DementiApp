package ucll.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import ucll.db.MediaRepository;
import ucll.db.PatientRepository;
import ucll.model.MediaFile;
import ucll.model.Patient;

import javax.naming.ServiceUnavailableException;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class RESTController {

    @Autowired //Inject?
    private PatientRepository patientRepository;

    @Autowired
    private MediaRepository mediaRepository;

    /*@Autowired
    public RESTController(PatientRepository patientRepository){
        this.patientRepository = patientRepository;
        setTestData();
    }*/

    public void setTestData(){
        if (patientRepository.findAll() == null || patientRepository.count() <= 0) {

            Patient desire = new Patient(UUID.randomUUID(), "Désire", "Klaas", null, 1);
            Patient germain = new Patient(UUID.randomUUID(), "Germain", "Van Hier", null, 1);
            Patient palmyr = new Patient(UUID.randomUUID(), "Palmyr", "Leysens", null, 2);

            patientRepository.save(desire);
            patientRepository.save(germain);
            patientRepository.save(palmyr);
        }

        UUID desId = null;
        UUID gerId = null;
        List<Patient> all = StreamSupport.stream(patientRepository.findAll().spliterator(), false).collect(Collectors.toList());
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getFirstName().equals("Germain")){
                gerId = all.get(i).patientId;
            }
            if (all.get(i).getLastName().equals("Klaas")){
                desId = all.get(i).patientId;
            }
        }

        if (mediaRepository.findAll() == null || mediaRepository.count() <= 0){
            System.out.println(desId);

            MediaFile desireFile1 = new MediaFile(UUID.randomUUID(), desId, null, false);
            MediaFile desireFile2 = new MediaFile(UUID.randomUUID(), desId, null, false);
            MediaFile germainFile1 = new MediaFile(UUID.randomUUID(), gerId, null, false);

            mediaRepository.save(desireFile1);
            mediaRepository.save(desireFile2);
            mediaRepository.save(germainFile1);
        }
    }

    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getPatients(){
        setTestData();
        return ResponseEntity.ok(StreamSupport.stream(patientRepository.findAll().spliterator(), false)
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

    @Transactional
    @PostMapping(value = "/patients")
    public ResponseEntity<Patient> postPatient(@RequestBody Patient patient) {
        Patient result = patientRepository.save(patient);
        return ResponseEntity.ok(result);
    }

    @PutMapping(value = "/patients/{patientId}")
    public ResponseEntity<Patient> putPatient(@PathVariable UUID patientId, @RequestBody Patient patient) {
        if (patientRepository.existsById(patientId))
            if (patientId.equals(patient.getPatientId())) {
                Patient result = patientRepository.save(patient);
                return ResponseEntity.ok(result);
            }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/patients/{patientId}")
    public ResponseEntity<Boolean> deletePatient(@PathVariable UUID patientId) throws RestClientException{
        Optional<Patient> op = patientRepository.findById(patientId);
        if (op.isPresent()) {
            patientRepository.delete(op.get());
            return ResponseEntity.ok(new Boolean(Boolean.TRUE));
        }
        else return ResponseEntity.ok(new Boolean(Boolean.FALSE));
    }

    @GetMapping("/media")
    public ResponseEntity<List<MediaFile>> getMediaFiles(){
        setTestData();
        return ResponseEntity.ok(StreamSupport.stream(mediaRepository.findAll().spliterator(), false)
                .collect(Collectors.toList()));
    }

    @GetMapping("/media/{patientId}")
    public ResponseEntity<List<MediaFile>> getMediaFileFor(@PathVariable UUID patientId){
        List<MediaFile> all = StreamSupport.stream(mediaRepository.findAll().spliterator(), false).collect(Collectors.toList());
        List<MediaFile> result = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getPatientId().equals(patientId)){
                result.add(all.get(i));
            }
        }

        return ResponseEntity.ok(result);
        /*return ResponseEntity.ok(StreamSupport.stream(mediaRepository.findAll().spliterator(), false)
                .filter(mediaFile -> mediaFile.getPatientId() == patientId)
                .collect(Collectors.toList()));*/
    }

    @GetMapping("/media/file/{mediaId}")
    public ResponseEntity<MediaFile> getMediaFile(@PathVariable UUID mediaId){
        Optional<MediaFile> op = mediaRepository.findById(mediaId);
        if (op.isPresent())
        {
            return ResponseEntity.ok(op.get());
        }

        else return ResponseEntity.notFound().build();
    }

    @Transactional
    @PostMapping("/media")
    public ResponseEntity<MediaFile> postMediaFile(@RequestBody MediaFile mediaFile) {
        MediaFile result = mediaRepository.save(mediaFile);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/media/{mediaId}")
    public ResponseEntity<MediaFile> putMediaFile(@PathVariable UUID mediaId, @RequestBody MediaFile mediaFile){
        if (mediaRepository.existsById(mediaId))
            if (mediaId.equals(mediaFile.getMediaId())) {
                MediaFile result = mediaRepository.save(mediaFile);
                return ResponseEntity.ok(result);
            }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/media/{mediaId}")
    public ResponseEntity<Boolean> deleteMediaFile(@PathVariable UUID mediaId){
        Optional<MediaFile> op = mediaRepository.findById(mediaId);
        if (op.isPresent()) {
            mediaRepository.delete(op.get());
            return ResponseEntity.ok(new Boolean(Boolean.TRUE));
        }
        else return ResponseEntity.ok(new Boolean(Boolean.FALSE));
    }
}

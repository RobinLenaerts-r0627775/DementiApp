package ucll.controllers;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import ucll.db.LoginRepository;
import ucll.db.MediaRepository;
import ucll.db.NurseRepository;
import ucll.db.PatientRepository;
import ucll.model.*;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class RESTController {
    //private static String fileDir = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images\\";  //local
    private static String fileDir = System.getProperty("user.dir") + "/build/resources/main/static/images/";    //server


    @Autowired //Inject?
    private PatientRepository patientRepository;

    @Autowired
    private NurseRepository nurseRepository;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private LoginRepository loginRepository;

    public RESTController(PatientRepository patientRepository, NurseRepository nurseRepository, MediaRepository mediaRepository, LoginRepository loginRepository) {
        this.patientRepository = patientRepository;
        this.nurseRepository = nurseRepository;
        this.mediaRepository = mediaRepository;
        this.loginRepository = loginRepository;
        setTestData();
    }

    public void setTestData(){

        Patient desire = new Patient(null, "Désire", "Klaes", null, 1, null, "sinter");
        Patient germain = new Patient(null, "Germain", "Van Hier", null, 1, null, "ucll");
        Patient palmyr = new Patient(null, "Palmyr", "Leysens", null, 2, null, "t");

        patientRepository.save(desire);
        patientRepository.save(germain);
        patientRepository.save(palmyr);


        UUID desId = desire.patientId;
        UUID gerId = germain.patientId;

        Nurse Tine = new Nurse("Tine", "Vandevelde", "nurse");
        nurseRepository.save(Tine);

        for (Patient p : patientRepository.findAll()) {
            LoginInfo tmp = LoginInfo.LoginInfomaker(p.firstName + "." + p.lastName, p.password,p.role, p.patientId);
            loginRepository.save(tmp);
        }
        for (Nurse n : nurseRepository.findAll()) {
            loginRepository.save(LoginInfo.LoginInfomaker(n.firstName + "." + n.lastName, n.password,n.role, n.nurseID));
        }
        /*List<Patient> all = StreamSupport.stream(patientRepository.findAll().spliterator(), false).collect(Collectors.toList());
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getFirstName().equals("Germain")){
                gerId = all.get(i).patientId;
            }
            if (all.get(i).getLastName().equals("Klaas")){
                desId = all.get(i).patientId;
            }
        }*/

        MediaFile desireFile1 = new MediaFile(UUID.randomUUID(), desId, new File(fileDir + "Temp1.jpg"), "Dit is de beschrijving voor deze foto", "test");
        MediaFile desireFile3 = new MediaFile(UUID.randomUUID(), desId, new File(fileDir + "jeanine.jpg"), "Dit is de beschrijving voor deze foto2", "test");
        MediaFile desireFile2 = new MediaFile(UUID.randomUUID(), desId, new File(fileDir + "Temp2.jpg"), "Dit is een beschrijving voor deze foto", "test2");
        MediaFile germainFile1 = new MediaFile(UUID.randomUUID(), gerId, new File(fileDir + "Temp3.jpg"), "Dit is de beschrijving voor de foto", "test");

        mediaRepository.save(desireFile1);
        mediaRepository.save(desireFile2);
        mediaRepository.save(desireFile3);
        mediaRepository.save(germainFile1);
    }

    private List<MediaFile> getForId(UUID id){
        List<MediaFile> all = StreamSupport.stream(mediaRepository.findAll().spliterator(), false).collect(Collectors.toList());
        List<MediaFile> result = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getPatientId().equals(id)){
                result.add(all.get(i));
            }
        }
        return result;
    }

    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getPatients(){
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
        if (patientRepository.existsById(patientId)) {
            if (patientId.equals(patient.getPatientId())) {
                Patient result = patientRepository.save(patient);
                return ResponseEntity.ok(result);
            }
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
        return ResponseEntity.ok(StreamSupport.stream(mediaRepository.findAll().spliterator(), false)
                .collect(Collectors.toList()));
    }

    @GetMapping("/media/{patientId}") //TODO
    public ResponseEntity<List<MediaFile>> getMediaFileFor(@PathVariable UUID patientId){
        return ResponseEntity.ok(getForId(patientId));
    }

    @GetMapping("/media/data/{mediaId}")
    public ResponseEntity<byte[]> getFile(@PathVariable UUID mediaId) throws IOException {
        Optional<MediaFile> op = mediaRepository.findById(mediaId);
        if (op.isPresent())
        {
            HttpHeaders headers = new HttpHeaders();
            InputStream in = new FileInputStream(op.get().file);
            byte[] media = IOUtils.toByteArray(in);
            headers.setCacheControl(CacheControl.noCache().getHeaderValue());

            ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
            return responseEntity;
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/media/file/{mediaId}")
    public ResponseEntity<MediaFile> getMediaFile(@PathVariable UUID mediaId){
        Optional<MediaFile> op = mediaRepository.findById(mediaId);
        if (op.isPresent())
        {
            return  ResponseEntity.ok(op.get());
        }
        else return ResponseEntity.notFound().build();
    }

    @GetMapping("media/{patientId}/{category}")
    public ResponseEntity<List<MediaFile>> getMediaFileByCat(@PathVariable UUID patientId, @PathVariable String category){
        List<MediaFile> all = getForId(patientId);
        List<MediaFile> result = new ArrayList<>();
        for (MediaFile file : all){
            if (file.category.equals(category)){
                result.add(file);
            }
        }
        return ResponseEntity.ok(result);
    }

    @Transactional
    @PostMapping("/media/file")
    public ResponseEntity<MediaFile> postMediaFile(@RequestBody FileUploadObject object){
        //Create MediaFile
        MediaFile result = new MediaFile(null, object.patientId, null, object.description, object.category);
        //Save MediaFile to get an ID
        result = mediaRepository.save(result);
        //Create a path
        Path path = Paths.get(fileDir, result.mediaId.toString() + object.extension);
        //Create File with the name of the ID
        File file = new File(fileDir + result.mediaId.toString() + object.extension);
        //Write the data to the file
        try {
            Files.write(path, object.file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Set File in MediaFile
        result.setFile(file);
        //Save it
        result = mediaRepository.save(result);

        return ResponseEntity.ok(result);
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

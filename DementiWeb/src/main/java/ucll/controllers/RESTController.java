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
    private static String fileDir = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images\\";  //local
    //private static String fileDir = System.getProperty("user.dir") + "/resources/main/static/images/";    //server


    @Autowired //Inject?
    private PatientRepository patientRepository;

    @Autowired
    private NurseRepository nurseRepository;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private LoginRepository loginRepository;

    /**
     * Constructor
     *
     * @param patientRepository
     * @param nurseRepository
     * @param mediaRepository
     * @param loginRepository
     */
    public RESTController(PatientRepository patientRepository, NurseRepository nurseRepository, MediaRepository mediaRepository, LoginRepository loginRepository) {
        this.patientRepository = patientRepository;
        this.nurseRepository = nurseRepository;
        this.mediaRepository = mediaRepository;
        this.loginRepository = loginRepository;
        setTestData();
    }

    /**
     * TestData
     */
    public void setTestData(){
        MediaFile defaultProfilePicture = new MediaFile(null, null, new File(fileDir + "Profile.png"), "Default Profile picture", "default");
        mediaRepository.save(defaultProfilePicture);

        Patient desire = new Patient(null, "Désire", "Klaes", defaultProfilePicture.mediaId, "sinter");
        Patient germain = new Patient(null, "Germain", "Van Hier", defaultProfilePicture.mediaId, "ucll");
        Patient palmyr = new Patient(null, "Palmyr", "Leysens", defaultProfilePicture.mediaId, "t");

        patientRepository.save(desire);
        patientRepository.save(germain);
        patientRepository.save(palmyr);


        UUID desId = desire.patientId;
        UUID gerId = germain.patientId;
        UUID palId = palmyr.patientId;

        Nurse Tine = new Nurse("Tine", "Vandevelde", "nurse");
        nurseRepository.save(Tine);

        for (Patient p : patientRepository.findAll()) {
            LoginInfo tmp = LoginInfo.LoginInfomaker(p.firstName + "." + p.lastName, p.getPassword(),p.role, p.patientId);
            loginRepository.save(tmp);
        }
        for (Nurse n : nurseRepository.findAll()) {
            loginRepository.save(LoginInfo.LoginInfomaker(n.firstName + "." + n.lastName, n.password,n.role, n.nurseID));
        }

        String lipsum = "zwdzqAWHvzAsxpedzqVo CPULTAVsdzqfDMKivPlx  jWPdzqsjXagGiOubdzqROPqmPRgUKdzqaAIFWD JLEqdzqoaGaDiusondzqidlddKYtGZ dzqbhYKjzIiHAd zfhqijfzkjzfjqzfjhqz zhjfhqzkjfhqjzhf kqjzhfkqjhfkqjhfkqzhf IOHOIUGFOZIHFI";

        MediaFile desireFile1 = new MediaFile(null, desId, new File(fileDir + "Temp1.jpg"), "Dit is de beschrijving voor deze foto", "test");
        MediaFile desireFile3 = new MediaFile(null, desId, new File(fileDir + "jeanine.jpg"), "Dit is de beschrijving voor deze foto2", "test");
        MediaFile desireFile2 = new MediaFile(null, desId, new File(fileDir + "Temp2.jpg"), lipsum, "test2");
        MediaFile germainFile1 = new MediaFile(null, gerId, new File(fileDir + "Temp3.jpg"), "Dit is de beschrijving voor de foto", "test");
        MediaFile palmyrWed1 = new MediaFile(null, palId, new File(fileDir + "Wed1.jpg"), "Dit is de beschrijving voor de foto Wed 1", "Wedding");
        MediaFile palmyrWed2 = new MediaFile(null, palId, new File(fileDir + "Wed2.jpg"), "Dit is de beschrijving voor de foto Wed 2", "Wedding");
        MediaFile palmyrWed3 = new MediaFile(null, palId, new File(fileDir + "Wed3.jpg"), "Dit is de beschrijving voor de foto Wed 3", "Wedding");
        MediaFile palmyrWed4 = new MediaFile(null, palId, new File(fileDir + "Wed4.jpg"), "Dit is de beschrijving voor de foto Wed 2", "Wedding");
        MediaFile palmyrWed5 = new MediaFile(null, palId, new File(fileDir + "Wed5.jpg"), "Dit is de beschrijving voor de foto Wed 3", "Wedding");
        MediaFile palmyrWed6 = new MediaFile(null, palId, new File(fileDir + "Wed6.jpg"), "Dit is de beschrijving voor de foto Wed 2", "Wedding");
        MediaFile palmyrWed7 = new MediaFile(null, palId, new File(fileDir + "Wed7.jpg"), "Dit is de beschrijving voor de foto Wed 3", "Wedding");
        MediaFile palmyrWed8 = new MediaFile(null, palId, new File(fileDir + "Wed8.jpg"), "Dit is de beschrijving voor de foto Wed 2", "Wedding");
        MediaFile palmyrWed9 = new MediaFile(null, palId, new File(fileDir + "Wed9.jpg"), "Dit is de beschrijving voor de foto Wed 3", "Wedding");
        MediaFile palmyrWed10 = new MediaFile(null, palId, new File(fileDir + "Wed10.jpg"), "Dit is de beschrijving voor de foto Wed 3", "Wedding");
        MediaFile palmyrWed11 = new MediaFile(null, palId, new File(fileDir + "Wed11.jpg"), "Dit is de beschrijving voor de foto Wed 2", "Wedding");
        MediaFile palmyrWed12 = new MediaFile(null, palId, new File(fileDir + "Wed12.jpg"), "Dit is de beschrijving voor de foto Wed 3", "Wedding");
        MediaFile palmyrSchool1 = new MediaFile(null, palId, new File(fileDir + "School1.jpg"), "Dit is de beschrijving voor de foto School 1", "School");
        MediaFile palmyrSchool2 = new MediaFile(null, palId, new File(fileDir + "School2.jpg"), "Dit is de beschrijving voor de foto School 2", "School");

        mediaRepository.save(desireFile1);
        mediaRepository.save(desireFile2);
        mediaRepository.save(desireFile3);
        mediaRepository.save(germainFile1);
        mediaRepository.save(palmyrWed1);
        mediaRepository.save(palmyrWed2);
        mediaRepository.save(palmyrWed3);
        mediaRepository.save(palmyrWed4);
        mediaRepository.save(palmyrWed5);
        mediaRepository.save(palmyrWed6);
        mediaRepository.save(palmyrWed7);
        mediaRepository.save(palmyrWed8);
        mediaRepository.save(palmyrWed9);
        mediaRepository.save(palmyrWed10);
        mediaRepository.save(palmyrWed11);
        mediaRepository.save(palmyrWed12);
        mediaRepository.save(palmyrSchool1);
        mediaRepository.save(palmyrSchool2);

        palmyr.setProfile(palmyrWed1.mediaId);
        patientRepository.save(palmyr);
    }

    /**
     * private function that returns the list of MediaFile of a person with patientId
     *
     * @param id
     * @return
     */
    private List<MediaFile> getForId(UUID id){
        List<MediaFile> all = StreamSupport.stream(mediaRepository.findAll().spliterator(), false).collect(Collectors.toList());
        List<MediaFile> result = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getPatientId() != null && all.get(i).getPatientId().equals(id)){
                result.add(all.get(i));
            }
        }
        return result;
    }

    /**
     * returns the List of Patient objects
     *
     * @return
     */
    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getPatients(){
        return ResponseEntity.ok(StreamSupport.stream(patientRepository.findAll().spliterator(), false)
                .peek(patient -> patient.setPassword(null))
                .collect(Collectors.toList()));
    }

    /**
     * returns the Patient object that matches with the given patientId
     *
     * @param patientId
     * @return
     * @throws RestClientException
     */
    @GetMapping("/patients/{patientId}")
    public ResponseEntity<Patient> getPatient(@PathVariable UUID patientId)throws RestClientException{
        Optional<Patient> op = patientRepository.findById(patientId);
        if (op.isPresent())
        {
            Patient result = op.get();
            result.setPassword(null);
            return ResponseEntity.ok(result);
        }

        else return ResponseEntity.notFound().build();
    }

    /**
     * returns a List of strings with all the categories for the given patient with patientId
     *
     * @param patientId
     * @return
     */
    @GetMapping("/patients/category/{patientId}")
    public ResponseEntity<List<String>> getCategoriesFor(@PathVariable UUID patientId){
        Optional<Patient> op = patientRepository.findById(patientId);
        if (op.isPresent())
        {
            List<String> result = new ArrayList<>();
            for (MediaFile file : getForId(patientId)){
                if (!result.contains(file.category)){
                    result.add(file.category);
                }
            }
            return ResponseEntity.ok(result);
        } else return ResponseEntity.notFound().build();
    }

    /**
     * returns the List of MediaFile objects
     *
     * @return
     */
    @GetMapping("/media")
    public ResponseEntity<List<MediaFile>> getMediaFiles(){
        return ResponseEntity.ok(StreamSupport.stream(mediaRepository.findAll().spliterator(), false)
                .peek(mediaFile -> mediaFile.setFile(null))
                .collect(Collectors.toList()));
    }

    /**
     * returns the list of MediaFile of a patient with patientId
     *
     * @param patientId
     * @return
     */
    @GetMapping("/media/{patientId}") //TODO
    public ResponseEntity<List<MediaFile>> getMediaFileFor(@PathVariable UUID patientId){
        return ResponseEntity.ok(getForId(patientId));
    }

    /**
     * returns the data of a image as a byte[]
     *
     * @param mediaId
     * @return
     * @throws IOException
     */
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

    /**
     * returns the details of a mediaFile
     * @param mediaId
     * @return
     */
    @GetMapping("/media/file/{mediaId}")
    public ResponseEntity<MediaFile> getMediaFile(@PathVariable UUID mediaId){
        Optional<MediaFile> op = mediaRepository.findById(mediaId);
        if (op.isPresent())
        {
            //Security?
            return  ResponseEntity.ok(op.get());
        }
        else return ResponseEntity.notFound().build();
    }

    /**
     * returns the list of MediaFile of a person with patientId for the given category
     *
     * @param patientId
     * @param category
     * @return
     */
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

    /**
     * Checks if the credentials are valid and returns the patientId if so.
     *
     * @param loginObject
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<UUID> login(@RequestBody LoginObject loginObject){
        Optional<LoginInfo> op = loginRepository.getFirstByUsernameAndPassword(loginObject.user, loginObject.password);
        if (op.isPresent()) {
            LoginInfo result = op.get();

            if (result.getRole() == ROLE.NURSE) {
                return null;
            }
            return result != null ? ResponseEntity.ok(result.getPersonID()) : null;
        } else {
            return null;
        }
    }
}

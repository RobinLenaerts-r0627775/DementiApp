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
    private static String fileDir = System.getProperty("user.dir") + "/resources/main/static/images/";    //server


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

        Patient desire = new Patient(null, "Désire", "Klaes", defaultProfilePicture.mediaId, "t");
        Patient germain = new Patient(null, "Germain", "Van Hier", defaultProfilePicture.mediaId, "t");
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
            LoginInfo tmp = LoginInfo.LoginInfomaker(p.firstName + "." + p.lastName, p.hash,p.role, p.patientId);
            loginRepository.save(tmp);
        }
        for (Nurse n : nurseRepository.findAll()) {
            loginRepository.save(LoginInfo.LoginInfomaker(n.firstName + "." + n.lastName, n.hash,n.role, n.nurseID));
        }

        String lipsum = "Reminiscentie is een begrip dat verschillende ladingen dekt, al naar gelang de context waarin het wordt gebruikt. Het kan in algemene zin worden gebruikt ( zie Van Dale), het kan in therapeutische zin worden gebruikt (zie Bohlmeijer), het kan binnen de (ouderen)zorg worden gebruikt eveneens als therapie, maar ook als niet-therapeutische interventie (zie Hamburger).";

        MediaFile desireFile1 = new MediaFile(null, desId, new File(fileDir + "Temp1.jpg"), "Ons Jeanine is gelukkig bij het horen dat ze overgrootmoeder wordt", "Jeanine");
        MediaFile desireFile3 = new MediaFile(null, desId, new File(fileDir + "jeanine.jpg"), "Jeanine", "Jeanine");
        MediaFile desireFile2 = new MediaFile(null, desId, new File(fileDir + "Temp2.jpg"), lipsum, "Desire");
        MediaFile germainFile1 = new MediaFile(null, gerId, new File(fileDir + "Temp3.jpg"), "Wanneer de lector een grap vertelt en je hebt de credits nodig.", "Categorie");
        MediaFile germainFile2 = new MediaFile(null, gerId, new File(fileDir + "Fam1.jpg"), "Een familiefoto", "Categorie");
        MediaFile germainFile3 = new MediaFile(null, gerId, new File(fileDir + "Fam2.jpg"), "Een familiefoto", "Categorie");
        MediaFile germainFile4 = new MediaFile(null, gerId, new File(fileDir + "Fam3.jpg"), "Een familiefoto", "Categorie");
        MediaFile germainFile5 = new MediaFile(null, gerId, new File(fileDir + "Fam4.jpg"), "Een familiefoto", "Categorie");
        MediaFile germainFile6 = new MediaFile(null, gerId, new File(fileDir + "Fam5.jpg"), "Een familiefoto", "Categorie");
        MediaFile germainFile7 = new MediaFile(null, gerId, new File(fileDir + "Fam6.jpg"), "Een familiefoto", "Categorie");
        MediaFile germainFile8 = new MediaFile(null, gerId, new File(fileDir + "Fam7.jpg"), "Een familiefoto", "Categorie");
        MediaFile germainFile9 = new MediaFile(null, gerId, new File(fileDir + "Fam8.jpg"), "Een familiefoto", "Categorie");
        MediaFile germainFile10 = new MediaFile(null, gerId, new File(fileDir + "Fam9.jpg"), "Een familiefoto", "Categorie");
        MediaFile germainFile11 = new MediaFile(null, gerId, new File(fileDir + "Fam10.jpg"), "Een familiefoto", "Categorie");
        MediaFile palmyrWed1 = new MediaFile(null, palId, new File(fileDir + "Wed1.jpg"), "Dit is een foto genomen op jou 50e huwelijksverjaardag met Hugo. Op 6 oktober 2014 vierde we met heel de familie dat jij 50 jaar met Hugo hebt kunnen uithouden en samen een mooie familie hebben kunnen voortbrengen.", "Trouw");
        MediaFile palmyrWed2 = new MediaFile(null, palId, new File(fileDir + "Wed2.jpg"), "Deze foto had je genomen toen je op reis was door Laos met Hugo en de kinderen. Er was toen net een feest aan de gang waar de oudere zich opmaakte alsof ze gingen trouwen.", "Trouw");
        MediaFile palmyrWed3 = new MediaFile(null, palId, new File(fileDir + "Wed3.jpg"), "Deze foto had je genomen toen je op reis was door Laos met Hugo en de kinderen. Er was toen net een feest aan de gang waar de oudere zich opmaakte alsof ze gingen trouwen.", "Trouw");
        MediaFile palmyrWed4 = new MediaFile(null, palId, new File(fileDir + "Wed4.jpg"), "Dit is een trouwfoto van je dochter Julie met haar man Boris. Ze zijn getrouwd in 1978 op een mooie lentedag. De kip was Boris zijn idee.", "Trouw");
        MediaFile palmyrWed5 = new MediaFile(null, palId, new File(fileDir + "Wed5.jpg"), "Wat moet dat moet. Gelukkig kon je er nog om lachen.", "Trouw");
        MediaFile palmyrWed6 = new MediaFile(null, palId, new File(fileDir + "Wed6.jpg"), "Je kreeg je eerste echte 6-snaar, je kocht het in de 5 and dime. Je speelde tot je vingers bloedden, Het was de zomer van '69.", "Trouw");
        MediaFile palmyrWed7 = new MediaFile(null, palId, new File(fileDir + "Wed7.jpg"), "We zijn geen vreemden voor liefde, je kent de regels en ik ook. Een volledige toewijding is waar ik aan denk, je zou dit niet van een andere kerel krijgen", "Trouw");
        MediaFile palmyrWed8 = new MediaFile(null, palId, new File(fileDir + "Wed8.jpg"), "We kennen elkaar al zo lang. Je hart doet pijn, maar je bent te verlegen om het te zeggen. Binnen weten we allebei wat er aan de hand is, we kennen het spel en we gaan het spelen", "Trouw");
        MediaFile palmyrWed9 = new MediaFile(null, palId, new File(fileDir + "Wed9.jpg"), "Waar het begon, kan ik niet weten. Maar dan weet ik dat het sterk aan het groeien is. Het was in de lente, toen werd de lente de zomer. Wie had geloofd dat je zou meegaan?", "Trouw");
        MediaFile palmyrWed10 = new MediaFile(null, palId, new File(fileDir + "Wed10.jpg"), "Bijna de hemel, West Virginia, Blue Ridge Mountains, Shenandoah River. Het leven is daar oud, ouder dan de bomen jonger dan de bergen, blazend als een bries.", "Trouw");
        MediaFile palmyrWed11 = new MediaFile(null, palId, new File(fileDir + "Wed11.jpg"), "Het is negen uur op een zaterdag, de gewone menigte schuifelt erin. Er zit een oude man naast me liefde te maken met zijn tonic en gin.", "Trouw");
        MediaFile palmyrWed12 = new MediaFile(null, palId, new File(fileDir + "Wed12.jpg"), "Steve loopt voorzichtig over straat, met de rand naar beneden getrokken laag. Er is geen geluid maar het geluid van zijn voeten, machinegeweren klaar om te gaan.", "Trouw");
        MediaFile palmyrSchool1 = new MediaFile(null, palId, new File(fileDir + "School1.jpg"), "De klasfoto van je 6e leerjaar bij juf Godelieve op het Heilig-Hart.", "School");
        MediaFile palmyrSchool2 = new MediaFile(null, palId, new File(fileDir + "School2.jpg"), "Kijk eens hoe Geraard op deze foto staat. zijn tanden waren eruit gevallen toen hij tegen een boom was gereden met de fiets.", "School");

        mediaRepository.save(desireFile1);
        mediaRepository.save(desireFile2);
        mediaRepository.save(desireFile3);
        mediaRepository.save(germainFile1);
        mediaRepository.save(germainFile2);
        mediaRepository.save(germainFile3);
        mediaRepository.save(germainFile4);
        mediaRepository.save(germainFile5);
        mediaRepository.save(germainFile6);
        mediaRepository.save(germainFile7);
        mediaRepository.save(germainFile8);
        mediaRepository.save(germainFile9);
        mediaRepository.save(germainFile10);
        mediaRepository.save(germainFile11);
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
        palmyr.setPatientId(palId);
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
        Optional<LoginInfo> op = loginRepository.getFirstByUsernameAndHashedPassword(loginObject.user, loginObject.password.toLowerCase());
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

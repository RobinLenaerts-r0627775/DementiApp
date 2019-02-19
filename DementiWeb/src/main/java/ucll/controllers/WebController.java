package ucll.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ucll.db.MediaRepository;
import ucll.db.PatientRepository;
import ucll.model.MediaFile;
import ucll.model.Patient;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin
@Controller
public class WebController {

    private static String all = "overviewAllPatients";
    private static String form = "form";
    private static String index = "index";
    private static String greeting = "patientOverview";
    private static String album = "photoAlbum";

    @Value("${patients.apiaddress}")
    String patientApiAddress;

    @Value("${media.apiaddress}")
    String mediaApiAddress;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private MediaRepository mediaRepository;

    private void refreshPatientRepo() {
        if (patientApiAddress!=null){
            try {
                RestTemplate restTemplate = new RestTemplate();
                Patient[] response = restTemplate.getForObject(patientApiAddress,Patient[].class);
                for (Patient p : response) {
                    patientRepository.save(p);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Something went wrong with gathering patient data", e);
            }
        } else {
            throw new IllegalArgumentException("No api address for patients configured");
        }
        if (mediaApiAddress!=null){
            try {
                RestTemplate restTemplate = new RestTemplate();
                MediaFile[] response = restTemplate.getForObject(mediaApiAddress,MediaFile[].class);
                for (MediaFile p : response) {
                    mediaRepository.save(p);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Something went wrong with gathering media data", e);
            }
        } else {
            throw new IllegalArgumentException("No api address for media configured");
        }
    }

    @RequestMapping("/") //TODO
    public String index(Model model){return getAllPatientsPage(model);}

    @PostMapping("/addPhotos/{patientId}") //TODO
    public String addPhotos(@PathVariable UUID patientId, @RequestBody MediaFile mediaFile, Model model){
        if (mediaApiAddress!=null){
            try {
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<MediaFile> response = restTemplate.postForEntity(patientApiAddress, mediaFile, MediaFile.class);
            } catch (Exception e) {
                throw new IllegalArgumentException("Something went wrong with sending data", e);
            }
        } else {
            throw new IllegalArgumentException("No api addres configured");
        }
        refreshPatientRepo();
        return album;
    }

    @PostMapping("/setProfilePicture/{patientId}") //TODO
    public String setProfilePicture(@PathVariable UUID patientId){
        return  null;
    }

    @GetMapping("/media/{patientId}")
    public String getPhotos(@PathVariable UUID patientId, Model model){
        model.addAttribute("patientId", patientId);
        if (mediaApiAddress!=null){
            try {
                RestTemplate restTemplate = new RestTemplate();
                MediaFile[] result = restTemplate.getForObject(mediaApiAddress + "/" + patientId ,MediaFile[].class);
                model.addAttribute("photoAlbum", result);
            } catch (Exception e) {
                throw new IllegalArgumentException("Something went wrong with gathering media data", e);
            }
        } else {
            throw new IllegalArgumentException("No api address for media configured");
        }
        return album;
    }

    @GetMapping("/person/{patientId}")
    public String greeting(@PathVariable UUID patientId, Model model) {
        refreshPatientRepo();
        Optional<Patient> op = patientRepository.findById(patientId);
        if (op.isPresent()){
            model.addAttribute("patient", op.get());
            return greeting;
        }
        return index; //Fout
    }

    @GetMapping("/patients")
    public String getAllPatientsPage(Model model){
        refreshPatientRepo();
        model.addAttribute("patients", patientRepository.findAll());
        return all;
    }

    @GetMapping("/patients/{patientId}")
    public String getPatient (@PathVariable UUID patientId, Model model){
        refreshPatientRepo();
        Optional<Patient> op = patientRepository.findById(patientId);
        if (op.isPresent())
        {
            model.addAttribute("patient", op.get());
            return form;
        }

        else return all;
    }

    @GetMapping("/patients/new")
    public String newPatient(){
        return form;
    }

    @PostMapping(value = "/patients")
    public String postPatient(@RequestBody Patient patient, Model model){
        if (patient.profile == null) patient.setProfile(new File("static/images/Profile.png"));
        if (patientApiAddress!=null){
            try {
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<Patient> response = restTemplate.postForEntity(patientApiAddress, patient, Patient.class);
            } catch (Exception e) {
                throw new IllegalArgumentException("Something went wrong with sending data", e);
            }
        } else {
            throw new IllegalArgumentException("No api addres configured");
        }
        refreshPatientRepo();
        return all;
    }

    /*@PutMapping(value = "/patients/{patientId}")
    public String putPatient(@PathVariable UUID patientId, @RequestBody Patient patient, Model model){
        if (patientId == null || patient.patientId == null || !patient.patientId.equals(patientId)){
            throw new IllegalArgumentException("Error in the data");
        }
        if (apiAddres!=null){
            try {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.put(apiAddres, patient, Patient.class);
            } catch (Exception e) {
                throw new IllegalArgumentException("Something went wrong with sending data", e);
            }
        } else {
            throw new IllegalArgumentException("No api addres configured");
        }
        refreshPatientRepo();
        return all;
    }//*/
}

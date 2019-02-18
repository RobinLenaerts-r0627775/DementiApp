package ucll.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ucll.db.PatientRepository;
import ucll.model.Patient;
import ucll.model.PatientsPayload;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin
@Controller
public class WebController {

    private static String all = "overviewAllPatients";
    private static String form = "form";
    private static String index = "index";
    private static String greeting = "patientOverview";

    @Value("${patients.apiaddres}")
    String apiAddres;

    @Autowired
    private PatientRepository patientRepository;

    private void refreshPatientRepo() {
        if (apiAddres!=null){
            try {
                RestTemplate restTemplate = new RestTemplate();
                Patient[] response = restTemplate.getForObject(apiAddres,Patient[].class);
                for (Patient p : response) {
                    patientRepository.save(p);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Something went wrong with gathering data", e);
            }
        } else {
            throw new IllegalArgumentException("No api addres configured");
        }
    }

    @RequestMapping("/") //TODO
    public String index(Model model){return getAllPatientsPage(model);}

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
        System.out.println(patient.firstName);
        if (apiAddres!=null){
            try {
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<Patient> response = restTemplate.postForEntity(apiAddres, patient, Patient.class);
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

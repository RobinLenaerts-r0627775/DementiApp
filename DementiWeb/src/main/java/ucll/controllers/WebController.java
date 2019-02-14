package ucll.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import ucll.db.PatientRepository;
import ucll.model.Patient;
import ucll.model.PatientsPayload;

@CrossOrigin
@Controller
public class WebController {

    @Value("${patients.apiaddres}")
    String apiAddres;

    @Autowired
    private PatientRepository patientRepository;

    private void refreshPatientRepo() {
        if (apiAddres!=null){
            try {
                RestTemplate restTemplate = new RestTemplate();
                Patient[] response = restTemplate.getForObject(apiAddres,Patient[].class);
                System.out.println("DEBUG\n*\n*\n");
                System.out.println(response);
                for (Patient p : response) {
                    System.out.println(p.firstName);
                    patientRepository.save(p);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Something went wrong with gathering data", e);
            }
        } else {
            throw new IllegalArgumentException("No api addres configured");
        }
    }

    @RequestMapping("/")
    @GetMapping("/index")
    public String index(){return "index";}

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping("/patients")
    public String getAllPatientsPage(Model model){
        refreshPatientRepo();
        model.addAttribute("patients", patientRepository.findAll());
        return "overviewAllPatients";
    }
}

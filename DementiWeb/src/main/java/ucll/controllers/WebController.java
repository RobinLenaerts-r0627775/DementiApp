package ucll.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import ucll.db.PatientRepository;
import ucll.model.LoginInfo;
import ucll.model.Patient;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;

@CrossOrigin
@Controller
public class WebController {

    private static String all = "overviewAllPatients";
    private static String form = "form";
    private static String index = "index";
    private static String greeting = "patientOverview";
    private static String loginpage = "login";

    @Value("${patients.apiaddres}")
    String apiAddres;

    @Autowired
    private PatientRepository patientRepository;

    private List<LoginInfo> loginRepository = new ArrayList<>();

    public WebController(PatientRepository patientRepository){
        this.patientRepository = patientRepository;
        addExampleData();
    }

    public void addExampleData(){
        Patient desire = new Patient(UUID.randomUUID(), "Désire", "Klaas", null, 1, new File("static/images/Profile.png"), "sinter");
        Patient germain = new Patient(UUID.randomUUID(), "Germain", "Van Hier", null, 1, new File("static/images/Profile.png"), "ucll");
        Patient palmyr = new Patient(UUID.randomUUID(), "Palmyr", "Leysens", null, 2, new File("static/images/Profile.png"), "t");

        patientRepository.save(desire);
        patientRepository.save(germain);
        patientRepository.save(palmyr);
        for (Patient p : patientRepository.findAll()) {
            loginRepository.add(LoginInfo.LoginInfomaker(p.firstName + "." + p.lastName, p.password, p.patientId));
        }
    }

    @RequestMapping(value = "/login")
    public String toLogin(HttpServletRequest request, HttpServletResponse response){
        if(request.getSession(false) != null){
            return"redirect:/";
        }
        else {
            return "login";
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(Model model, HttpServletRequest request, HttpServletResponse response, @Valid @ModelAttribute("LoginInfo")LoginInfo info) throws IOException, ServletException {
        Map params = new HashMap<String, Object>();
        for(LoginInfo li : loginRepository){
            if (li.equals(info)) {
                HttpSession session = request.getSession();
                session.setAttribute("user", li.getUsername());
                session.setMaxInactiveInterval(60*60);
                Cookie id = new Cookie("patientid", patientRepository.findById(li.getPatientID()).get().getPatientId().toString());
                Cookie name = new Cookie("name", patientRepository.findById(li.getPatientID()).get().getFirstName());
                name.setMaxAge(60*60);
                id.setMaxAge(60*60);
                System.out.println("the cookie is: " + id.getValue());
                response.addCookie(id);
                response.addCookie(name);
                params.put("patients", patientRepository.findById(UUID.fromString(id.getValue())).get());
                return new ModelAndView("patientOverview", params );
            }
        }
        params.put("error", "failed to authenticate");
        return new ModelAndView("login", params);
    }

    @RequestMapping(value = "/")
    public ModelAndView getIndex() {
        Map params = new HashMap<String, Object>();
        params.put("patients", patientRepository.findAll());
        return new ModelAndView("overviewAllPatients", params);
    }


    /* Old controller before rework.
    private void refreshPatientRepo() {
        if (apiAddres!=null){
            try {
                RestTemplate restTemplate = new RestTemplate();
                Patient[] response = restTemplate.getForObject(apiAddres,Patient[].class);
                for (Patient p : response) {
                    patientRepository.save(p);
                    loginRepository.add(LoginInfo.LoginInfomaker(p.firstName + "." + p.lastName, p.password, p.patientId));
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Something went wrong with gathering data", e);
            }
        } else {
            throw new IllegalArgumentException("No api addres configured");
        }
    }

    @RequestMapping("/login")
    public void loginpage(Model model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getSession(false) != null){
            logout(model, request, response);
        }
        else {
            RequestDispatcher requestDispatcher= request.getRequestDispatcher("/toLogin");
            requestDispatcher.forward(request, response);
        }
    }

    @RequestMapping("/toLogin")
    public String toLogin(Model model, HttpServletRequest request, HttpServletResponse response){
        return loginpage;
    }



    @RequestMapping("/profile")
    public void profile(Model model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        for(Patient pat : patientRepository.findAll()){
            System.out.println(pat.patientId.toString());
        }
        if( request.getCookies().length != 0){
            for(Cookie c : request.getCookies()){
                System.out.println("============================== \n "+ c.getName() + " = " + c.getValue() + "\n ==============================");
                if(c.getName().equals("patientid")){
                    model.addAttribute("patient", patientRepository.findById(UUID.fromString(c.getValue())).get());
                    greeting(model, request, response);
                }
            }
        }
        else {
            loginpage(model, request, response);
        }
    }

    @RequestMapping(value = "/logout")
    public void logout(Model model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("========================= \n" + request.getSession(false) + "\n ===============");
        request.getSession().invalidate();
        for(Cookie c : request.getCookies()){
            if( c.getName().equals("patientid") || c.getName().equals("name")){
                response.setContentType("text/html");
                c.setMaxAge(1);
                System.out.println("cookie deleted: " + c.getName() +" " + c.getMaxAge() + " " + c.getValue());
                getAllPatientsPage(model, request, response);
            }
        }
    }

    @RequestMapping("/") //TODO
    public void index(Model model,HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {getAllPatientsPage(model, request, response);}

    @GetMapping("/person/{patientId}")
    public String greeting(Model model, HttpServletRequest request, HttpServletResponse response) {
        //refreshPatientRepo();
        UUID patientId = null;
        for(Cookie c : request.getCookies()){
            if( c.getName().equals("patientid")){
                patientId = UUID.fromString(c.getValue());
            }
        }
        Optional<Patient> op = patientRepository.findById(patientId);
        if (op.isPresent()){
            model.addAttribute("patient", op.get());
            return greeting;
        }
        return index; //Fout
    }

    @GetMapping("/patients")
    public void getAllPatientsPage(Model model,HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        refreshPatientRepo();
        model.addAttribute("patients", patientRepository.findAll());
        RequestDispatcher dd=request.getRequestDispatcher(all);
        dd.forward(request, response);
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

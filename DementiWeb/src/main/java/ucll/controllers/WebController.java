package ucll.controllers;

import com.mysql.cj.jdbc.exceptions.PacketTooBigException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ucll.db.MediaRepository;
import ucll.db.PatientRepository;
import ucll.model.FileUploadObject;
import ucll.model.MediaFile;
import ucll.model.Patient;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
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
    private static String album = "photoAlbum";

    @Value("${patients.apiaddress}")
    String patientApiAddress;

    @Value("${media.apiaddress}")
    String mediaApiAddress;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private MediaRepository mediaRepository;
    private List<LoginInfo> loginRepository = new ArrayList<>();

    /**
     * function that handles the default request call. it sends you to the patient overview page.
     *
     * @return
     */
    @RequestMapping(value = "/")
    public ModelAndView getIndex() {
        Map params = new HashMap<String, Object>();
        params.put("patients", patientRepository.findAll());
        return new ModelAndView("overviewAllPatients", params);
    }

    /**
     * constructor for the controller. sets up the repository and adds example data to it.
     *
     * @param patientRepository
     */
    public WebController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
        addExampleData();
    }

    /**
     * function that sets example data in the repository for testing purposes.
     */
    public void addExampleData() {
        Patient desire = new Patient(UUID.randomUUID(), "Désire", "Klaas", "01/06/1955", 1, null, "sinter");
        Patient germain = new Patient(UUID.randomUUID(), "Germain", "Van Hier", "01/06/1960", 1, null, "ucll");
        Patient palmyr = new Patient(UUID.randomUUID(), "Palmyr", "Leysens", "01/12/1950", 2, null, "t");

        patientRepository.save(desire);
        patientRepository.save(germain);
        patientRepository.save(palmyr);
        for (Patient p : patientRepository.findAll()) {
            loginRepository.add(LoginInfo.LoginInfomaker(p.firstName + "." + p.lastName, p.password, p.patientId));
        }
    }

    /**
     * function that handles the /login GET request. returns the login form if the user isnt already logged in. if it is, you get redirected to the index.
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/login")
    public ModelAndView toLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getSession(false) != null) {
            response.sendRedirect("/");
            return null;
        } else {
            return new ModelAndView("login", new HashMap<String, Object>());
        }
    }

    /**
     * function that handles the /login POST request. checks the credentials and adds session and relevant cookies to the response if the credentials are validated.
     * if not: an error message gets added and you get directed to the login page again.
     *
     * @param request
     * @param response
     * @param info
     * @return
     * @throws IOException
     * @throws ServletException
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response, @Valid @ModelAttribute("LoginInfo") LoginInfo info) throws IOException, ServletException {
        Map params = new HashMap<String, Object>();
        for (LoginInfo li : loginRepository) {
            if (li.equals(info)) {
                HttpSession session = request.getSession();
                session.setAttribute("user", li.getUsername());
                session.setMaxInactiveInterval(60 * 60);
                Cookie id = new Cookie("patientid", patientRepository.findById(li.getPatientID()).get().getPatientId().toString());
                Cookie name = new Cookie("name", patientRepository.findById(li.getPatientID()).get().getFirstName());
                name.setMaxAge(60 * 60);
                id.setMaxAge(60 * 60);
                response.addCookie(id);
                response.addCookie(name);
                response.sendRedirect("/profile");
            }
        }
        params.put("error", "failed to authenticate");
        return new ModelAndView("login", params);
    }

    /**
     * function that handles the /profile request. gets the userID from the right cookie and returns an overview of that users profile.
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/profile")
    public ModelAndView profile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map params = new HashMap<String, Object>();
        if (request.getSession(false) != null) {
            for (Cookie c : request.getCookies()) {
                if (c.getName().equals("patientid")) {
                    params.put("patient", patientRepository.findById(UUID.fromString(c.getValue())).get());
                }
            }
            return new ModelAndView("patientOverview", params);
        } else {
            response.sendRedirect("/");
            return null;
        }
    }

    /**
     * handles the users logout action. if an active session was found, the session gets invalidated and the relevant cookies get deleted by putting their age at 1.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().invalidate();
        for (Cookie c : request.getCookies()) {
            if (c.getName().equals("patientid") || c.getName().equals("name")) {
                response.setContentType("text/html");
                c.setMaxAge(1);
                System.out.println("cookie deleted: " + c.getName() + " " + c.getMaxAge() + " " + c.getValue());
            }
        }
        response.sendRedirect("/");
    }


    /**
     * Handles the /patients/new GET request. sends you to the new patient form.
     *
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/patients/new")
    public String newPatient(HttpServletRequest request, HttpServletResponse response) {
        return "form";
    }

    /**
     * handles the get request for /patients/{patientId}
     * gets the right patient and sends you to the form.
     *
     * @param patientId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("patients/{patientId}")
    public ModelAndView editPatient(@PathVariable UUID patientId, HttpServletRequest request, HttpServletResponse response) {
        Map params = new HashMap<String, Object>();
        params.put("patient", patientRepository.findById(patientId).get());
        return new ModelAndView("form", params);
    }

    /**
     * Handles the /patients POST request.
     *
     * @param patient
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/patients", method = RequestMethod.POST)
    public ModelAndView putPatient(@Valid @ModelAttribute("Patient") Patient patient, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map params = new HashMap<String, Object>();
        if (patient.patientId != null) {
            if (patientRepository.existsById(patient.patientId)) {
                Patient pat = patientRepository.findById(patient.patientId).get();
                pat.password = patient.password;
                pat.birthDate = patient.birthDate;
                pat.firstName = patient.firstName;
                pat.lastName = patient.lastName;
                pat.dementiaLevel = patient.dementiaLevel;
                pat.profilePicture = patient.profilePicture;
                patientRepository.save(pat);
            } else {
                params.put("error", "Something went wrong, please try again.");
                response.sendRedirect("/patients/new");
            }
        } else {
            Patient pat = new Patient(null, patient.firstName, patient.lastName, patient.birthDate, patient.dementiaLevel, null, patient.password);
            patientRepository.save(pat);
            loginRepository.add(LoginInfo.LoginInfomaker(patient.firstName + "." + patient.lastName, patient.password, pat.patientId));
        }

        params.put("confirm", "patient added/edited!");
        return new ModelAndView("form", params);

        /*if (patientId == null || patient.patientId == null || !patient.patientId.equals(patientId)){
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
            throw new IllegalArgumentException("No api address configured");
        }
        return all;*/
    }
}

    /* Old controller before rework.

    private void refreshPatientRepo() {
        if (patientApiAddress!=null){
            try {
                RestTemplate restTemplate = new RestTemplate();
                Patient[] response = restTemplate.getForObject(patientApiAddress,Patient[].class);
                for (Patient p : response) {
                    patientRepository.save(p);
                    loginRepository.add(LoginInfo.LoginInfomaker(p.firstName + "." + p.lastName, p.password, p.patientId));
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Something went wrong with sending data: " + e.getMessage());
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
                throw new IllegalArgumentException("Something went wrong with sending data: " + e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("No api address for media configured");
        }
    }

    private Patient postPatient(Patient patient) {
        Patient result = null;
        if (patientApiAddress != null){
            try {
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<Patient> response = restTemplate.postForEntity(patientApiAddress, patient, Patient.class);
                refreshPatientRepo();
                Optional<Patient> op = patientRepository.findById(response.getBody().getPatientId()); //Onnodig but Just to be sure
                if (op.isPresent()){
                    result = op.get();
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Something went wrong with sending data: " + e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("No api addres configured");
        }
        return result;
    }

    private MediaFile postMediaFile(MultipartFile file, UUID patientId, String description, String category) {
        MediaFile result = null;
        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
        FileUploadObject data = null;
        try {
            data = new FileUploadObject(file.getBytes(), patientId, extension, description, category);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mediaApiAddress != null){
            try {
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<MediaFile> response = restTemplate.postForEntity(mediaApiAddress + "/file", data, MediaFile.class);
                refreshPatientRepo();
                Optional<MediaFile> op = mediaRepository.findById(response.getBody().getMediaId()); //Onnodig but Just to be sure
                if (op.isPresent()) {
                    result = op.get();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new IllegalArgumentException("Something went wrong with sending data: " + e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("No api addres configured");
        }
        return result;
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


    @RequestMapping("/") //TODO
    public void index(Model model,HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {getAllPatientsPage(model, request, response);}

    @PostMapping("/addPhotos/{patientId}") //TODO
    public String addPhotos(@PathVariable UUID patientId, @RequestParam("files") MultipartFile[] files, @RequestParam("description") String description, @RequestParam("category") String category, Model model){
        for (MultipartFile file : files){
            MediaFile mf = postMediaFile(file, patientId, description, category);
        }

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
        return album; //TODO add albumphotos to model
    }

    @PostMapping("/setProfilePicture/{patientId}") //TODO
    public String setProfilePicture(@PathVariable UUID patientId, @RequestParam("files") MultipartFile file, @RequestParam("description") String description, @RequestParam("category") String category, Model model){
        refreshPatientRepo();
        Optional<Patient> op = patientRepository.findById(patientId);
        if (op.isPresent()){
            MediaFile mediaFile = postMediaFile(file, patientId, description, category);
            Patient patient = op.get();
            patient.setProfile(mediaFile.mediaId);
            patient = postPatient(patient);
            model.addAttribute("patient", patient);
            return greeting;
        }
        return index;//Fout
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


    @PostMapping(value = "/patients")
    public String postPatient(@RequestBody Patient patient, Model model){
        /*if (patient.profilePicture == null) {
            patient.setProfile(null); //TODO
        }
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

}*/

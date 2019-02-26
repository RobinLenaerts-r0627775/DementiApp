package ucll.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ucll.db.LoginRepository;
import ucll.db.MediaRepository;
import ucll.db.NurseRepository;
import ucll.db.PatientRepository;
import ucll.model.*;

import java.io.IOException;
import java.util.UUID;
import org.springframework.web.servlet.ModelAndView;
import ucll.model.Patient;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.validation.Valid;
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
    private NurseRepository nurseRepository;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private LoginRepository loginRepository;


    /**
     * constructor for the controller. sets up the repository and adds example data to it.
     *
     * @param patientRepository
     */
    public WebController(PatientRepository patientRepository, NurseRepository nurseRepository, MediaRepository mediaRepository, LoginRepository loginRepository) {
        this.patientRepository = patientRepository;
        this.nurseRepository = nurseRepository;
        this.mediaRepository = mediaRepository;
        this.loginRepository = loginRepository;
        //addExampleData();
    }

    /**
     * function that sets example data in the repository for testing purposes.
     */
    /*public void addExampleData() {
        Patient desire = new Patient(UUID.randomUUID(), "Désire", "Klaas", "01/06/1955", 1, null, "sinter");
        Patient germain = new Patient(UUID.randomUUID(), "Germain", "Van Hier", "01/06/1960", 1, null, "ucll");
        Patient palmyr = new Patient(UUID.randomUUID(), "Palmyr", "Leysens", "01/12/1950", 2, null, "t");
        patientRepository.save(desire);
        patientRepository.save(germain);
        patientRepository.save(palmyr);

        Nurse Tine = new Nurse("Tine", "Vandevelde", "nurse");
        nurseRepository.save(Tine);

        for (Patient p : patientRepository.findAll()) {
            LoginInfo tmp = LoginInfo.LoginInfomaker(p.firstName + "." + p.lastName, p.password,p.role, p.patientId);
            loginRepository.save(tmp);
        }
        for (Nurse n : nurseRepository.findAll()) {
            loginRepository.save(LoginInfo.LoginInfomaker(n.firstName + "." + n.lastName, n.password,n.role, n.nurseID));
        }
    }*/

    /**
     * function that handles the default request call. it sends you to the Login page.
     *
     */
    @RequestMapping(value = "/")
    public void getIndex(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getSession(false) != null) {
            if(((LoginInfo) request.getSession().getAttribute("user")).getRole() == ROLE.PATIENT){
                response.sendRedirect("/profile");
            }
            else if(((LoginInfo) request.getSession().getAttribute("user")).getRole() == ROLE.NURSE){
                response.sendRedirect("/patients");
            }
        }
        else {
            response.sendRedirect("/login");
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
            response.sendRedirect("/profile");
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
        for (LoginInfo li : loginRepository.findAll()) {
            if (li.equals(info)) {
                HttpSession session = request.getSession();
                session.setMaxInactiveInterval(60 * 60);
                session.setAttribute("user", li);
                Cookie id, name;
                if(li.getRole() == ROLE.PATIENT) {
                    id = new Cookie("patientid", patientRepository.findById(li.getPersonID()).get().getPatientId().toString());
                    name = new Cookie("name", patientRepository.findById(li.getPersonID()).get().getFirstName());
                }
                else {
                    id = new Cookie("patientid", nurseRepository.findById(li.getPersonID()).get().getNurseID().toString());
                    name = new Cookie("name", nurseRepository.findById(li.getPersonID()).get().getFirstName());
                }
                name.setMaxAge(60 * 60);
                id.setMaxAge(60 * 60);
                response.addCookie(id);
                response.addCookie(name);
                if(li.getRole() == ROLE.PATIENT) {
                    response.sendRedirect("/profile");
                }else if(li.getRole() == ROLE.NURSE){
                    response.sendRedirect("/patients");
                }
            }
        }
        params.put("error", "failed to authenticate");
        return new ModelAndView("login", params);
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
        if (request.getSession(false) != null) {
            request.getSession().invalidate();
            if (request.getCookies().length > 0) {
                for (Cookie c : request.getCookies()) {
                    if (c.getName().equals("patientid") || c.getName().equals("name")) {
                        response.setContentType("text/html");
                        c.setMaxAge(1);
                        System.out.println("cookie deleted: " + c.getName() + " " + c.getMaxAge() + " " + c.getValue());
                    }
                }
            }
        }
        response.sendRedirect("/");
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
            params.put("error", "please login.");
            return new ModelAndView("login", params);
        }
    }


    /**
     *handles the /patient/{patientId} request. checks if the user has required permissions and sends you to the profile page of the patient with given ID.
     *
     * @param request
     * @param response
     * @param patientId
     * @return
     */
    @RequestMapping(value= "/profile/{patientId}")
    public ModelAndView seeProfile(HttpServletRequest request, HttpServletResponse response, @PathVariable UUID patientId){
        Map params = new HashMap<>();
        if(request.getSession(false) == null || ((LoginInfo) request.getSession().getAttribute("user")).getRole() != ROLE.NURSE){
            return new ModelAndView("AccessException", params) ;
        }
        else{
            Patient pat = patientRepository.findById(patientId).get();
            params.put("patient", pat);
            return  new ModelAndView("patientOverview", params);
        }
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
        if(request.getSession(false) == null || ((LoginInfo) request.getSession().getAttribute("user")).getRole() != ROLE.NURSE){
            return "AccessException";
        }
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
     * handles the /patients requests. Sends you to an overview page of all the patients.
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/patients")
    public ModelAndView overview(HttpServletRequest request, HttpServletResponse response){
        Map params = new HashMap<String, Object>();
        params.put("patients", patientRepository.findAll());
        return new ModelAndView("overviewAllPatients", params);
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
            loginRepository.save(LoginInfo.LoginInfomaker(patient.firstName + "." + patient.lastName, patient.password,patient.role, pat.patientId));
        }

        return overview(request, response);
    }

    /**
     * Handles the /media/patientId request.
     * checks the permissions of the user and sends you to the photoalbum page of the selected profile.
     * @param request
     * @param response
     * @param patientId
     * @return
     */
    @RequestMapping(value = "/webmedia/{patientId}")
    public ModelAndView patientMedia(HttpServletRequest request, HttpServletResponse response, @PathVariable UUID patientId){
        Map params = new HashMap();
        if(request.getSession(false) == null || ((LoginInfo) request.getSession().getAttribute("user")).getRole() != ROLE.NURSE){
            return new ModelAndView("AccessException", params );
        }
        else{
            params.put("photoAlbum", mediaRepository.getAllByPatientId(patientId));
            params.put("patientId", patientId.toString());
            return new ModelAndView("photoAlbum", params);
        }
    }

    @RequestMapping(value = "/webmedia", method = RequestMethod.POST)
    public void postMedia(HttpServletRequest request, HttpServletResponse response, @Valid @ModelAttribute("MediaFile") MediaFile mediaFile) throws IOException {
        System.out.println("=====================\n" + mediaFile.patientId + " " + mediaFile.category + " " + mediaFile.description + " " + mediaFile.mediaId);
        mediaRepository.save(mediaFile);
        response.sendRedirect("WebMedia/" + mediaFile.patientId.toString());
    }

}



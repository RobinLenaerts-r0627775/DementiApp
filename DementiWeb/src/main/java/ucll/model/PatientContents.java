package ucll.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PatientContents {
    private List<Patient> patients;

    public List<Patient> getQuotes() {
        //System.out.println(patients);
        return patients;
    }

    public void setQuotes(List<Patient> quotes) {
        //System.out.println(patients);
        this.patients = quotes;
    }
}

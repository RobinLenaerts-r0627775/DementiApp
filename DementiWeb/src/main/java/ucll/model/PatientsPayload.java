package ucll.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PatientsPayload {
    private PatientContents contents;

    public PatientContents getContents() {
        //System.out.println(contents);
        return contents;
    }

    public void setContents(PatientContents contents) {
        //System.out.println(contents);
        this.contents = contents;
    }
}

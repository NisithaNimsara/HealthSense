package model.severity;

// Holds the peak case information for one hospital and disease.
public class SeverityRecord {
    String hospital;  // which hospital this record is for
    String disease;   // disease name
    int caseCount;    // number of cases (used as severity metric)

    // Constructor
    public SeverityRecord(String hospital, String disease, int caseCount) {
        this.hospital = hospital;
        this.disease = disease;
        this.caseCount = caseCount;
    }

    /* Simple severity label based on caseCount:
     *   0-20  : Mild
     *   21-50 : Moderate
     *   51+   : Severe
     */
    public String severityLabel() {
        if (caseCount <= 20) return "Mild";
        if (caseCount <= 50) return "Moderate";
        return "Severe";
    }

    //Shows disease, hospital, number of cases, and the severity label.
    @Override
    public String toString() {
        return String.format("%s (%s) cases: %d - %s", disease, hospital, caseCount, severityLabel());
    }
}

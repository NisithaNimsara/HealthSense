package model.disease;

public class DiseaseRecord {
    public String diseaseName;  // name of the disease
    public int weekNumber;      // which week this data is for
    public int caseCount;       // number of cases in that week

    // Constructor: make a new record with all values
    public DiseaseRecord(String diseaseName, int weekNumber, int caseCount) {
        this.diseaseName = diseaseName;
        this.weekNumber = weekNumber;
        this.caseCount = caseCount;
    }

    public boolean equals(DiseaseRecord other) {
        if (other == null) return false;
        return this.diseaseName.equals(other.diseaseName)
                && this.weekNumber == other.weekNumber
                && this.caseCount == other.caseCount;
    }
}

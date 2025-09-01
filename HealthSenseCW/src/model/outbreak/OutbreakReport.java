package model.outbreak;

//outbreak report

public class OutbreakReport {
    String region;    // region of the report
    String hospital;  // hospital of the report
    String summary;   // brief description

    // Constructor
    public OutbreakReport(String region, String hospital, String summary) {
        this.region = region;
        this.hospital = hospital;
        this.summary = summary;
    }

    //Shows hospital, region, and the summary message.
    @Override
    public String toString() {
        return String.format("[Hospital: %s | Region: %s] %s", hospital, region, summary);
    }
}

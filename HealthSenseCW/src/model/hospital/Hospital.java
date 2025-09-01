package model.hospital;

import model.disease.DiseaseRecordLinkedList;

//Represents a hospital with its name, region, and disease records history.
public class Hospital {
    public String name;  // hospital identifier/name
    public String region; // region or area the hospital belongs to
    public DiseaseRecordLinkedList diseaseHistory = new DiseaseRecordLinkedList(); // list of disease records for this hospital

    // Constructor
    public Hospital(String name, String region) {
        this.name = name;
        this.region = region;
    }
}

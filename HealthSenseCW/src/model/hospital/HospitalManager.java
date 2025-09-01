package model.hospital;

// Manages a collection of Hospital objects.
public class HospitalManager {
    private Hospital[] hospitals = new Hospital[100]; // array to hold hospitals
    private int count = 0; // how many hospitals are currently stored

    //Add a new hospital
    public void addHospital(String name, String region) {
        if (getHospitalByName(name) != null) return; // avoid duplicates by name
        if (count >= hospitals.length) {
            System.out.println("Max hospitals reached."); // no space left
            return;
        }
        hospitals[count++] = new Hospital(name, region); // add hospital and increment count
    }

    //Find a hospital by its name
    public Hospital getHospitalByName(String name) {
        for (int i = 0; i < count; i++) {
            if (hospitals[i].name.equalsIgnoreCase(name)) return hospitals[i];
        }
        return null; // no match found
    }

    // Return an array containing all hospitals currently stored.
    public Hospital[] getAllHospitals() {
        Hospital[] active = new Hospital[count];
        System.arraycopy(hospitals, 0, active, 0, count);
        return active;
    }
}

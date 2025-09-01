package cli;

//Entry point

public class Main {
    public static void main(String[] args) {
        SystemEngine engine = new SystemEngine();
        engine.seedSampleData(); // preload sample hospitals and disease records
        engine.runCLI(); // start user interaction loop
    }
}

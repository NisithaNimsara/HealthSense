package model.outbreak;

public class OutbreakQueueManager {
    // Simple pair class to hold region and its queue
    private static class RegionQueue {
        String region;
        ReportQueue queue;

        RegionQueue(String region) {
            this.region = region;
            this.queue = new ReportQueue();
        }
    }

    // Fixed size array to hold RegionQueue pairs
    private RegionQueue[] regionQueues = new RegionQueue[100];
    private int count = 0;

    // Find RegionQueue by region name
    private RegionQueue findRegionQueue(String region) {
        for (int i = 0; i < count; i++) {
            if (regionQueues[i].region.equalsIgnoreCase(region)) {
                return regionQueues[i];
            }
        }
        return null;
    }

    // Add report to region queue, create new queue if region doesn't exist
    public void enqueueReport(String region, OutbreakReport report) {
        RegionQueue rq = findRegionQueue(region);
        if (rq == null) {
            if (count >= regionQueues.length) {
                System.out.println("Max regions reached.");
                return;
            }
            rq = new RegionQueue(region);
            regionQueues[count++] = rq;
        }
        rq.queue.enqueue(report);
    }

    // Print all reports for a region
    public void printQueue(String region) {
        RegionQueue rq = findRegionQueue(region);
        if (rq == null) {
            System.out.println("No queue for region.");
            return;
        }
        System.out.println("Outbreak reports for " + region + ":");
        rq.queue.printAll();
    }
}

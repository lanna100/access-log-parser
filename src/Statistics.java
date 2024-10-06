import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Statistics {
    private int totalTraffic;
    private OffsetDateTime minTime;
    private OffsetDateTime maxTime;
    private HashSet<String> existingPages;
    private HashMap<String, Integer> osFrequency;
    private int totalOSCount;

    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = OffsetDateTime.MAX;
        this.maxTime = OffsetDateTime.MIN;
        this.existingPages = new HashSet<>();
        this.osFrequency = new HashMap<>();
    }

    public void addEntry(LogEntry logEntry) {
        totalTraffic += logEntry.getDataSize();
        OffsetDateTime requestTime = logEntry.getRequestTime();
        if (requestTime.isBefore(minTime)) {
            minTime = requestTime;
        }
        if (requestTime.isAfter(maxTime)) {
            maxTime = requestTime;
        }
        if (logEntry.getResponseCode() == 200) {
            existingPages.add(logEntry.getRequestPath());
        }

        String osName = logEntry.getUserAgent().getOsType();
        osFrequency.put(osName, osFrequency.getOrDefault(osName, 0) + 1);
        totalOSCount++;
    }

    public HashSet<String> getExistingPages() {
        return existingPages;
    }

    public Map<String, Double> getOperatingSystemStatistics() {
        HashMap<String, Double> osDistribution = new HashMap<>();

        for (Map.Entry<String, Integer> entry : osFrequency.entrySet()) {
            String os = entry.getKey();
            int count = entry.getValue();
            double proportion = (double) count / totalOSCount;
            osDistribution.put(os, proportion);
        }

        return osDistribution;
    }

    public double getTrafficRate() {
        if (minTime.isEqual(maxTime)) {
            return 0;
        }
        long hoursDiff = java.time.Duration.between(minTime, maxTime).toHours();
        if (hoursDiff <= 0) {
            return 0;
        } else {
            return (double) totalTraffic / hoursDiff;
        }
    }
}

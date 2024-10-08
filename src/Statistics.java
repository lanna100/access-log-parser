import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Statistics {
    private int totalTraffic;
    private OffsetDateTime minTime;
    private OffsetDateTime maxTime;
    private HashSet<String> existingPages;
    private HashSet<String> notExistingPages;
    private HashMap<String, Integer> osFrequency;
    private HashMap<String, Integer> browserFrequency;
    private int totalOSCount;
    private int totalBrowserCount;
    private int totalUserVisits;
    private int totalErrorResponses;
    private HashSet<String> uniqueUserIPs;

    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = OffsetDateTime.MAX;
        this.maxTime = OffsetDateTime.MIN;
        this.existingPages = new HashSet<>();
        this.notExistingPages = new HashSet<>();
        this.osFrequency = new HashMap<>();
        this.browserFrequency = new HashMap<>();
        this.totalUserVisits = 0;
        this.totalErrorResponses = 0;
        this.uniqueUserIPs = new HashSet<>();
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

        String userIP = logEntry.getIp();

        if (logEntry.getResponseCode() == 200) {
            existingPages.add(logEntry.getRequestPath());
            if (!logEntry.getUserAgent().getUserAgentString().toLowerCase().contains("bot")) {
                totalUserVisits++;
                uniqueUserIPs.add(userIP);
            }
        } else if (logEntry.getResponseCode() == 404) {
            notExistingPages.add(logEntry.getRequestPath());
        }

        if (logEntry.getResponseCode() >= 400 && logEntry.getResponseCode() < 600) {
            totalErrorResponses++;
        }

        String osName = logEntry.getUserAgent().getOsType();
        osFrequency.put(osName, osFrequency.getOrDefault(osName, 0) + 1);
        totalOSCount++;

        String browserName = logEntry.getUserAgent().getBrowser();
        browserFrequency.put(browserName, browserFrequency.getOrDefault(browserName, 0) + 1);
        totalBrowserCount++;
    }

    public HashSet<String> getExistingPages() {
        return existingPages;
    }

    public HashSet<String> getNotExistingPages() {
        return notExistingPages;
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

    public Map<String, Double> getBrowserStatistics() {
        HashMap<String, Double> browserDistribution = new HashMap<>();

        for (Map.Entry<String, Integer> entry : browserFrequency.entrySet()) {
            String browser = entry.getKey();
            int count = entry.getValue();
            double proportion = (double) count / totalBrowserCount;
            browserDistribution.put(browser, proportion);
        }

        return browserDistribution;
    }

    public double getAverageVisitsPerHour() {
        long hoursDiff = java.time.Duration.between(minTime, maxTime).toHours();
        if (hoursDiff <= 0) {
            return 0;
        }
        return (double) totalUserVisits / hoursDiff;
    }

    public double getAverageErrorResponsesPerHour() {
        long hoursDiff = java.time.Duration.between(minTime, maxTime).toHours();
        if (hoursDiff <= 0) {
            return 0;
        }
        return (double) totalErrorResponses / hoursDiff;
    }

    public double getAverageVisitsPerUser() {
        if (uniqueUserIPs.size() == 0) {
            return 0;
        }
        return (double) totalUserVisits / uniqueUserIPs.size();
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

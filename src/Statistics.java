import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class Statistics {
    private int totalTraffic;
    private OffsetDateTime minTime;
    private OffsetDateTime maxTime;

    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = OffsetDateTime.MAX;
        this.maxTime = OffsetDateTime.MIN;
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

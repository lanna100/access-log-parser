public class UserAgent {
    private String userAgentString;
    private final String osType;
    private final String browser;

    public UserAgent(String userAgentString) {
        this.osType = determineOSType(userAgentString);
        this.browser = determineBrowser(userAgentString);
        this.userAgentString = userAgentString;

    }


    public String getOsType() {
        return osType;
    }

    public String getBrowser() {
        return browser;
    }

    private String determineOSType(String userAgent) {
        if (userAgent.contains("Windows")) {
            return "Windows";
        } else if (userAgent.contains("Macintosh")) {
            return "macOS";
        } else if (userAgent.contains("Linux")) {
            return "Linux";
        }
        return "Other";
    }

    private String determineBrowser(String userAgent) {
        if (userAgent.contains("Firefox")) {
            return "Firefox";
        } else if (userAgent.contains("Chrome")) {
            return "Chrome";
        } else if (userAgent.contains("Opera")) {
            return "Opera";
        } else if (userAgent.contains("Edge")) {
            return "Edge";
        } else if (userAgent.contains("Safari")) {
            return "Safari";
        }
        return "Other";
    }

    public String getUserAgentString() {
        return userAgentString;
    }
}

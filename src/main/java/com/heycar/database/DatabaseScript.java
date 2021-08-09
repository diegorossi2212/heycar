package com.heycar.database;

public class DatabaseScript {

    private final int majorVersion;
    private final int minorVersion;
    private final int patchVersion;
    private final int buildVersion;
    private final String label;

    public DatabaseScript(int majorVersion, int minorVersion, int patchVersion, int buildVersion, String label) {
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.patchVersion = patchVersion;
        this.buildVersion = buildVersion;
        this.label = label;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public int getPatchVersion() {
        return patchVersion;
    }

    public int getBuildVersion() {
        return buildVersion;
    }

    public String getLabel() {
        return label;
    }
    
}

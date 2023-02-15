package org.ahamdah;

public class Distribution {
    private String name;
    private String version;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    private String state;

    Distribution(String name, String state,String version ) {
        this.name = name;
        this.state = state;
        this.version = version;
    }

    @Override
    public String toString() {
        return name + " (" + version + ") - " + state;
    }
}

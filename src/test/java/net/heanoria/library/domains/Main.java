package net.heanoria.library.domains;

import java.util.Map;

public class Main {
    private Double version;
    private Url url;
    private Map<String, Disco> versions;

    public Double getVersion() {
        return version;
    }

    public void setVersion(Double version) {
        this.version = version;
    }

    public Url getUrl() {
        return url;
    }

    public void setUrl(Url url) {
        this.url = url;
    }

    public Map<String, Disco> getVersions() {
        return versions;
    }

    public void setVersions(Map<String, Disco> versions) {
        this.versions = versions;
    }
}

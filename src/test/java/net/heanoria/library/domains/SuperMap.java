package net.heanoria.library.domains;

import java.util.Map;

public class SuperMap {

    private Double version;
    private Url url;
    private Map<String, String> size;

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

    public Map<String, String> getSize() {
        return size;
    }

    public void setSize(Map<String, String> size) {
        this.size = size;
    }
}

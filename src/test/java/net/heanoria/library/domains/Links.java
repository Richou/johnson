package net.heanoria.library.domains;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Links {

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;

    @JsonProperty("media_url")
    private String mediaUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }
}

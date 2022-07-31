package me.nerdoron.himyb.modules.youtube;

public class youtubeVideo {
    private final String title;
    private final String description;
    private final String videoID;
    private final String thumbnail;

    public youtubeVideo(String title, String description, String videoID, String thumbnail) {
        this.title = title;
        this.description = description;
        this.videoID = videoID;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoID() {
        return videoID;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}

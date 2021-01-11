package b2d.l.mahtmagandhi;

import java.io.Serializable;

public class Meeting implements Serializable {
    private String id;
    private String title;
    private String description;
    private String meeting_date;
    private String meeting_time;
    private String latitude;
    private String longitude;

    public Meeting() {
    }

    public Meeting(String id, String title, String description, String meeting_date, String meeting_time, String latitude, String longitude) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.meeting_date = meeting_date;
        this.meeting_time = meeting_time;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMeeting_date() {
        return meeting_date;
    }

    public void setMeeting_date(String meeting_date) {
        this.meeting_date = meeting_date;
    }

    public String getMeeting_time() {
        return meeting_time;
    }

    public void setMeeting_time(String meeting_time) {
        this.meeting_time = meeting_time;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}

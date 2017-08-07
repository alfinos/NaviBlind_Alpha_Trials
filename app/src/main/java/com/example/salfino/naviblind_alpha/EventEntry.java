package com.example.salfino.naviblind_alpha;

/**
 * Created by Saviour on 21/07/2017.
 */

public class EventEntry {

    private String levelnumber;
    private String floorplanid;
    private String floordescription;
    private String name;
    private String eventdescription;
    private String latitude;
    private String longitude;
    private String text;

    public String getLevelnumber() {
        return levelnumber;
    }

    public void setLevelnumber(String levelnumber) {
        this.levelnumber = levelnumber;
    }

    public String getFloorplanid() {
        return floorplanid;
    }

    public void setFloorplanid(String floorplanid) {
        this.floorplanid = floorplanid;
    }

    public String getFloordescription() {
        return floordescription;
    }

    public void setFloordescription(String floordescription) {
        this.floordescription = floordescription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEventdescription() {
        return eventdescription;
    }

    public void setEventdescription(String eventdescription) {
        this.eventdescription = eventdescription;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "EventEntry{" +
                "levelnumber='" + levelnumber + '\'' +
                ", floorplanid='" + floorplanid + '\'' +
                ", floordescription='" + floordescription + '\'' +
                ", name='" + name + '\'' +
                ", eventdescription='" + eventdescription + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}

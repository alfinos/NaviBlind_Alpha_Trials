package com.example.salfino.naviblind_alpha;

/**
 * Created by Saviour on 18/07/2017.
 */

public class WayPointEntry {

    private String levelnumber;
    private String floorplanid;
    private String floordescription;
    private String name;
    private String waypointdescription;
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

    public String getWaypointdescription() {
        return waypointdescription;
    }

    public void setWaypointdescription(String waypointdescription) {
        this.waypointdescription = waypointdescription;
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
        return "WayPointEntry{" +
                "levelnumber='" + levelnumber + '\'' +
                ", floorplanid='" + floorplanid + '\'' +
                ", floordescription='" + floordescription + '\'' +
                ", name='" + name + '\'' +
                ", waypointdescription='" + waypointdescription + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}

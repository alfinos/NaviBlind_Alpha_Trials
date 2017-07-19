package com.example.salfino.naviblind_110217;

/**
 * Created by Saviour on 16/07/2017.
 */

public class RoomWayPointEntry {

    private String levelnumber;
    private String floorplanid;
    private String floordescription;
    private String name;
    private String description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        return "RoomWayPointEntry{" +
                "levelnumber='" + levelnumber + '\'' +
                ", floorplanid='" + floorplanid + '\'' +
                ", floordescription='" + floordescription + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}

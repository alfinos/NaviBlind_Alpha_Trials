package com.example.salfino.naviblind_110217;

/**
 * Created by Saviour on 16/07/2017.
 */

public class RouteEntry {

    private String levelNo;
    private String floorPlanId;
    private String routedescription;
    private String startpointname;
    private String startpointtext;
    private String firstwaypointname;
    private String firstwaypointtext;
    private String secondwaypointname;
    private String secondwaypointtext;
    private String endpointname;
    private String endpointtext;
    private String firstconfirmation;
    private String secondconfirmation;

    public String getLevelNo() {
        return levelNo;
    }

    public void setLevelNo(String levelNo) {
        this.levelNo = levelNo;
    }

    public String getFloorPlanId() {
        return floorPlanId;
    }

    public void setFloorPlanId(String floorPlanId) {
        this.floorPlanId = floorPlanId;
    }

    public String getRoutedescription() {
        return routedescription;
    }

    public void setRoutedescription(String routedescription) {
        this.routedescription = routedescription;
    }

    public String getStartpointname() {
        return startpointname;
    }

    public void setStartpointname(String startpointname) {
        this.startpointname = startpointname;
    }

    public String getStartpointtext() {
        return startpointtext;
    }

    public void setStartpointtext(String startpointtext) {
        this.startpointtext = startpointtext;
    }

    public String getFirstwaypointname() {
        return firstwaypointname;
    }

    public void setFirstwaypointname(String firstwaypointname) {
        this.firstwaypointname = firstwaypointname;
    }

    public String getFirstwaypointtext() {
        return firstwaypointtext;
    }

    public void setFirstwaypointtext(String firstwaypointtext) {
        this.firstwaypointtext = firstwaypointtext;
    }

    public String getSecondwaypointname() {
        return secondwaypointname;
    }

    public void setSecondwaypointname(String secondwaypointname) {
        this.secondwaypointname = secondwaypointname;
    }

    public String getSecondwaypointtext() {
        return secondwaypointtext;
    }

    public void setSecondwaypointtext(String secondwaypointtext) {
        this.secondwaypointtext = secondwaypointtext;
    }

    public String getEndpointname() {
        return endpointname;
    }

    public void setEndpointname(String endpointname) {
        this.endpointname = endpointname;
    }

    public String getEndpointtext() {
        return endpointtext;
    }

    public void setEndpointtext(String endpointtext) {
        this.endpointtext = endpointtext;
    }

    public String getFirstconfirmation() {
        return firstconfirmation;
    }

    public void setFirstconfirmation(String firstconfirmation) {
        this.firstconfirmation = firstconfirmation;
    }

    public String getSecondconfirmation() {
        return secondconfirmation;
    }

    public void setSecondconfirmation(String secondconfirmation) {
        this.secondconfirmation = secondconfirmation;
    }

    @Override
    public String toString() {
        return "RouteEntry{" +
                "levelNo='" + levelNo + '\'' +
                ", floorPlanId='" + floorPlanId + '\'' +
                ", routedescription='" + routedescription + '\'' +
                ", startpointname='" + startpointname + '\'' +
                ", startpointtext='" + startpointtext + '\'' +
                ", firstwaypointname='" + firstwaypointname + '\'' +
                ", firstwaypointtext='" + firstwaypointtext + '\'' +
                ", secondwaypointname='" + secondwaypointname + '\'' +
                ", secondwaypointtext='" + secondwaypointtext + '\'' +
                ", endpointname='" + endpointname + '\'' +
                ", endpointtext='" + endpointtext + '\'' +
                ", firstconfirmation='" + firstconfirmation + '\'' +
                ", secondconfirmation='" + secondconfirmation + '\'' +
                '}';
    }
}

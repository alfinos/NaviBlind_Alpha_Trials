package com.example.salfino.naviblind_110217;

/**
 * Created by Saviour on 21/07/2017.
 */

public class ConfigEntry {
    private String defaultinterval;
    private String defaultdisplacement;
    private String defaultaccuracy;
    private String defaultcurrentdistance;
    private String defaultspeechrate;
    private String defaultpitch;

    public String getDefaultinterval() {
        return defaultinterval;
    }

    public void setDefaultinterval(String defaultinterval) {
        this.defaultinterval = defaultinterval;
    }

    public String getDefaultdisplacement() {
        return defaultdisplacement;
    }

    public void setDefaultdisplacement(String defaultdisplacement) {
        this.defaultdisplacement = defaultdisplacement;
    }


    public String getDefaultaccuracy() {
        return defaultaccuracy;
    }

    public void setDefaultaccuracy(String defaultaccuracy) {
        this.defaultaccuracy = defaultaccuracy;
    }

    public String getDefaultcurrentdistance() {
        return defaultcurrentdistance;
    }

    public void setDefaultcurrentdistance(String defaultcurrentdistance) {
        this.defaultcurrentdistance = defaultcurrentdistance;
    }

    public String getDefaultspeechrate() {
        return defaultspeechrate;
    }

    public void setDefaultspeechrate(String defaultspeechrate) {
        this.defaultspeechrate = defaultspeechrate;
    }

    public String getDefaultpitch() {
        return defaultpitch;
    }

    public void setDefaultpitch(String defaultpitch) {
        this.defaultpitch = defaultpitch;
    }

    @Override
    public String toString() {
        return "ConfigEntry{" +
                "defaultinterval='" + defaultinterval + '\'' +
                ", defaultdisplacement='" + defaultdisplacement + '\'' +
                ", defaultaccuracy='" + defaultaccuracy + '\'' +
                ", defaultcurrentdistance='" + defaultcurrentdistance + '\'' +
                ", defaultspeechrate='" + defaultspeechrate + '\'' +
                ", defaultpitch='" + defaultpitch + '\'' +
                '}';
    }
}
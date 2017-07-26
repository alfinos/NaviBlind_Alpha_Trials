package com.example.salfino.naviblind_110217;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Saviour on 25/07/2017.
 */
public class TextEntryTest {
    private TextEntry textEntry;
    //private ParseIndoorAtlas mParseIndoorAtlas;

    @Before
    public void setUp() throws Exception {
        textEntry = new TextEntry();
        //mParseIndoorAtlas = mock(ParseIndoorAtlas.class);

    }

    @Test
    public void getAndsetIntrotext_check() throws Exception {
        textEntry.setIntrotext("Intro");
        assertEquals(textEntry.getIntrotext(),"Intro");
    }

    @Test
    public void getAndsetLocationtext() throws Exception {
        textEntry.setLocationtext("Location");
        assertEquals(textEntry.getLocationtext(),"Location");

    }

    @Test
    public void getAndsetMenutext() throws Exception {
        textEntry.setMenutext("Menu");
        assertEquals(textEntry.getMenutext(),"Menu");
    }

    @Test
    public void getAndsetRoomtext() throws Exception {
        textEntry.setRoomtext("Room");
        assertEquals(textEntry.getRoomtext(),"Room");

    }

    @Test
    public void getAndsetWaypointtext() throws Exception {
        textEntry.setWaypointtext("Waypoint");
        assertEquals(textEntry.getWaypointtext(),"Waypoint");

    }

    @Test
    public void getAndsetConfirmationtext() throws Exception {
        textEntry.setConfirmationtext("Confirmation");
        assertEquals(textEntry.getConfirmationtext(),"Confirmation");

    }

    @Test
    public void getAndsetRepeattext() throws Exception {
        textEntry.setRepeattext("Repeat");
        assertEquals(textEntry.getRepeattext(),"Repeat");

    }

    @Test
    public void getAndsetServicetext() throws Exception {
        textEntry.setServicetext("Service");
        assertEquals(textEntry.getServicetext(),"Service");

    }

    @Test
    public void getAndsetEventsintrotext() throws Exception {
        textEntry.setEventsintrotext("Events");
        assertEquals(textEntry.getEventsintrotext(),"Events");

    }

    @Test
    public void getAndsetNoeventstext() throws Exception {
        textEntry.setNoeventstext("NoEvents");
        assertEquals(textEntry.getNoeventstext(),"NoEvents");

    }

    @Test
    public void getAndsetRoutetext() throws Exception {
        textEntry.setRoutetext("Route");
        assertEquals(textEntry.getRoutetext(),"Route");

    }

    @Test
    public void getAndsetNoroutetext() throws Exception {
        textEntry.setNoroutetext("NoRoute");
        assertEquals(textEntry.getNoroutetext(),"NoRoute");

    }

    @Test
    public void getAndsetResumetext() throws Exception {
        textEntry.setResumetext("Resume");
        assertEquals(textEntry.getResumetext(),"Resume");

    }

}
package com.example.salfino.naviblind_110217;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Saviour on 25/07/2017.
 */
public class ParseIndoorAtlasTest {

    private TextEntry myTextEntry;
    private RoomEntry myRoomEntry;
    private WayPointEntry myWaypointEntry;
    private RoomWayPointEntry myRoomWaypointEntry;
    private EventEntry myEventsEntry;
    private RouteEntry myRouteEntry;

    private ArrayList<TextEntry> textdata = new ArrayList<>();
    private ArrayList<RoomEntry> roomdata = new ArrayList<>();
    private ArrayList<WayPointEntry> waypointdata = new ArrayList<>();
    private ArrayList<RoomWayPointEntry> roomwaypointdata = new ArrayList<>();
    private ArrayList<EventEntry> eventdata = new ArrayList<>();
    private ArrayList<RouteEntry> routedata = new ArrayList<>();

    private ParseIndoorAtlas mParseIndoorAtlas = new ParseIndoorAtlas();


    @Test
    public void getRoomData() throws Exception {
        myRoomEntry = new RoomEntry();
        myRoomEntry.setLevelnumber("LevelNumber");
        myRoomEntry.setFloorplanid("FloorPlanID");
        myRoomEntry.setFloordescription("Description");
        myRoomEntry.setName("Name");
        myRoomEntry.setRoomdescription("Room");
        myRoomEntry.setLatitude("Latitude");
        myRoomEntry.setLongitude("Longitude");
        myRoomEntry.setText("Text");

        for (int i = 0; i < 20;i++){
            roomdata.add(i,myRoomEntry);
        }

        mParseIndoorAtlas.setRoomdata(roomdata);
        assertEquals(mParseIndoorAtlas.getRoomData(),roomdata);
    }

    @Test
    public void getRouteData() throws Exception {
        myRouteEntry = new RouteEntry();
        myRouteEntry.setLevelNo("A");
        myRouteEntry.setFloorPlanId("B");
        myRouteEntry.setRoutedescription("C");
        myRouteEntry.setStartpointname("D");
        myRouteEntry.setStartpointtext("E");
        myRouteEntry.setFirstwaypointname("F");
        myRouteEntry.setFirstwaypointtext("G");
        myRouteEntry.setFirstconfirmation("H");
        myRouteEntry.setSecondwaypointname("I");
        myRouteEntry.setSecondwaypointtext("J");
        myRouteEntry.setSecondconfirmation("K");
        myRouteEntry.setEndpointname("L");
        myRouteEntry.setEndpointtext("M");

        for (int i = 0; i < 20;i++){
            routedata.add(i,myRouteEntry);
        }

        mParseIndoorAtlas.setRoutedata(routedata);
        assertEquals(mParseIndoorAtlas.getRouteData(),routedata);

    }

    @Test
    public void getTextData() throws Exception {
        myTextEntry = new TextEntry();
        myTextEntry.setIntrotext("Intro");
        myTextEntry.setLocationtext("Location");
        myTextEntry.setMenutext("Menu");
        myTextEntry.setRoomtext("Room");
        myTextEntry.setWaypointtext("Waypoint");
        myTextEntry.setConfirmationtext("Confirmation");
        myTextEntry.setRepeattext("Repeat");
        myTextEntry.setServicetext("Service");
        myTextEntry.setEventsintrotext("Events");
        myTextEntry.setNoeventstext("NoEvents");
        myTextEntry.setRoutetext("Route");
        myTextEntry.setNoroutetext("NoRoute");
        myTextEntry.setResumetext("Resume");

        for (int i = 0; i < 20;i++){
            textdata.add(i,myTextEntry);
        }

        mParseIndoorAtlas.setTextdata(textdata);
        assertEquals(mParseIndoorAtlas.getTextData(),textdata);
    }

    @Test
    public void getWayPointData() throws Exception {

        myWaypointEntry = new WayPointEntry();
        myWaypointEntry.setLevelnumber("LevelNumber");
        myWaypointEntry.setFloorplanid("FloorPlanID");
        myWaypointEntry.setFloordescription("Description");
        myWaypointEntry.setName("Name");
        myWaypointEntry.setWaypointdescription("Waypoint");
        myWaypointEntry.setLatitude("Latitude");
        myWaypointEntry.setLongitude("Longitude");
        myWaypointEntry.setText("Text");

        for (int i = 0; i < 20;i++){
            waypointdata.add(i,myWaypointEntry);
        }

        mParseIndoorAtlas.setWaypointdata(waypointdata);
        assertEquals(mParseIndoorAtlas.getWayPointData(),waypointdata);

    }

    @Test
    public void getRoomWayPointData() throws Exception {

        myRoomWaypointEntry = new RoomWayPointEntry();
        myRoomWaypointEntry.setLevelnumber("LevelNumber");
        myRoomWaypointEntry.setFloorplanid("FloorPlanID");
        myRoomWaypointEntry.setFloordescription("Description");
        myRoomWaypointEntry.setName("Name");
        myRoomWaypointEntry.setDescription("Description");
        myRoomWaypointEntry.setLatitude("Latitude");
        myRoomWaypointEntry.setLongitude("Longitude");
        myRoomWaypointEntry.setText("Text");

        for (int i = 0; i < 20;i++){
            roomwaypointdata.add(i,myRoomWaypointEntry);
        }

        mParseIndoorAtlas.setRoomwaypointdata(roomwaypointdata);
        assertEquals(mParseIndoorAtlas.getRoomWayPointData(),roomwaypointdata);

    }

    @Test
    public void getConfigurationData() throws Exception {

    }

    @Test
    public void getEventData() throws Exception {

    }

}

package com.example.salfino.naviblind_110217;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by Saviour on 15/07/2017.
 */

public class ParseIndoorAtlas {
    private static final String TAG = "ParseIndoorAtlas";
    private ArrayList<RoomEntry> roomdata;
    private ArrayList<RouteEntry> routedata;
    private ArrayList<TextEntry> textdata;
    private ArrayList<WayPointEntry> waypointdata;
    private ArrayList<RoomWayPointEntry> roomwaypointdata;
    private ArrayList<ConfigEntry> configurationdata;
    private ArrayList<EventEntry> eventdata;

    public ParseIndoorAtlas() {
        this.roomdata = new ArrayList<>();//Initializing Array list on constructor
        this.routedata = new ArrayList<>();//Initializing Array list on constructor
        this.textdata = new ArrayList<>();//Initializing Array list on constructor
        this.waypointdata = new ArrayList<>();//Initializing Array list on constructor
        this.roomwaypointdata = new ArrayList<>();//Initializing Array list on constructor
        this.configurationdata = new ArrayList<>();
        this.eventdata = new ArrayList<>();
    }

    public ArrayList<RoomEntry> getRoomData() {
        return roomdata;
    }

    public ArrayList<RouteEntry> getRouteData() {
        return routedata;
    }

    public ArrayList<TextEntry> getTextData() {
        return textdata;
    }

    public ArrayList<WayPointEntry> getWayPointData() {
        return waypointdata;
    }

    public ArrayList<RoomWayPointEntry> getRoomWayPointData() {
        return roomwaypointdata;
    }

    public ArrayList<ConfigEntry> getConfigurationData(){
        return configurationdata;
    }

    public ArrayList<EventEntry> getEventData(){
        return eventdata;
    }

    public boolean parseRoom(String xmlData) {
        boolean status = true;
        RoomEntry currentRecord = null;
        boolean inEntry = false;
        String textValue = "";

        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput (new StringReader(xmlData));
            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){
                String tagName = xpp.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        Log.d(TAG, "parse: Starting tag for " + tagName);
                        if ("room".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            currentRecord = new RoomEntry();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        Log.d(TAG, "parse: Ending tag for " + tagName);
                        if(inEntry){
                            if("room".equalsIgnoreCase(tagName)){
                                roomdata.add(currentRecord);
                                inEntry = false;//Ending tag for entry
                            } else if ("levelNo".equalsIgnoreCase(tagName)) {//Guaranteed not to be Null
                                currentRecord.setLevelnumber(textValue);
                            } else if ("floorPlanId".equalsIgnoreCase(tagName)){//Guaranteed not to be Null
                                    currentRecord.setFloorplanid(textValue);
                            } else if ("floordescription".equalsIgnoreCase(tagName)) {//Guaranteed not to be Null
                                currentRecord.setFloordescription(textValue);
                            }else if ("name".equalsIgnoreCase(tagName)){//Guaranteed not to be Null
                                    currentRecord.setName(textValue);
                            }else if ("roomdescription".equalsIgnoreCase(tagName)){//Guaranteed not to be Null
                                    currentRecord.setRoomdescription(textValue);
                            } else if ("text".equalsIgnoreCase(tagName)){
                                currentRecord.setText(textValue);
                            } else if ("latitude".equalsIgnoreCase(tagName)){
                                currentRecord.setLatitude(textValue);
                            } else if ("longitude".equalsIgnoreCase(tagName)){
                                currentRecord.setLongitude(textValue);
                            }
                        }
                        break;
                    default: //nothing else to do
                }
                eventType = xpp.next();
            }

            for (RoomEntry mydata1: roomdata){
                Log.d(TAG, "*************************");
                Log.d(TAG, mydata1.toString());
            }

        }catch (Exception e) {
            status = false;
            e.printStackTrace();
        }

        return status;
    }

    public boolean parseEvent(String xmlData) {
        boolean status = true;
        EventEntry currentRecord = null;
        boolean inEntry = false;
        String textValue = "";

        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput (new StringReader(xmlData));
            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){
                String tagName = xpp.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        Log.d(TAG, "parse: Starting tag for " + tagName);
                        if ("event".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            currentRecord = new EventEntry();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        Log.d(TAG, "parse: Ending tag for " + tagName);
                        if(inEntry){
                            if("event".equalsIgnoreCase(tagName)){
                                eventdata.add(currentRecord);
                                inEntry = false;//Ending tag for entry
                            } else if ("levelNo".equalsIgnoreCase(tagName)) {//Guaranteed not to be Null
                                currentRecord.setLevelnumber(textValue);
                            } else if ("floorPlanId".equalsIgnoreCase(tagName)){//Guaranteed not to be Null
                                currentRecord.setFloorplanid(textValue);
                            } else if ("floordescription".equalsIgnoreCase(tagName)) {//Guaranteed not to be Null
                                currentRecord.setFloordescription(textValue);
                            }else if ("name".equalsIgnoreCase(tagName)){//Guaranteed not to be Null
                                currentRecord.setName(textValue);
                            }else if ("eventdescription".equalsIgnoreCase(tagName)){//Guaranteed not to be Null
                                currentRecord.setEventdescription(textValue);
                            } else if ("text".equalsIgnoreCase(tagName)){
                                currentRecord.setText(textValue);
                            } else if ("latitude".equalsIgnoreCase(tagName)){
                                currentRecord.setLatitude(textValue);
                            } else if ("longitude".equalsIgnoreCase(tagName)){
                                currentRecord.setLongitude(textValue);
                            }
                        }
                        break;
                    default: //nothing else to do
                }
                eventType = xpp.next();
            }

            for (EventEntry mydata7: eventdata){
                Log.d(TAG, "????????????????????????");
                Log.d(TAG, mydata7.toString());
            }

        }catch (Exception e) {
            status = false;
            e.printStackTrace();
        }

        return status;
    }

    public boolean parseWayPoint(String xmlData) {
        boolean status = true;
        WayPointEntry currentRecord = null;
        boolean inEntry = false;
        String textValue = "";

        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput (new StringReader(xmlData));
            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){
                String tagName = xpp.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        Log.d(TAG, "parse: Starting tag for " + tagName);
                        if ("waypoint".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            currentRecord = new WayPointEntry();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        Log.d(TAG, "parse: Ending tag for " + tagName);
                        if(inEntry){
                            if("waypoint".equalsIgnoreCase(tagName)){
                                waypointdata.add(currentRecord);
                                inEntry = false;//Ending tag for entry
                            } else if ("levelNo".equalsIgnoreCase(tagName)) {//Guaranteed not to be Null
                                currentRecord.setLevelnumber(textValue);
                            } else if ("floorPlanId".equalsIgnoreCase(tagName)){//Guaranteed not to be Null
                                currentRecord.setFloorplanid(textValue);
                            } else if ("floordescription".equalsIgnoreCase(tagName)) {//Guaranteed not to be Null
                                currentRecord.setFloordescription(textValue);
                            }else if ("name".equalsIgnoreCase(tagName)){//Guaranteed not to be Null
                                currentRecord.setName(textValue);
                            }else if ("waypointdescription".equalsIgnoreCase(tagName)){//Guaranteed not to be Null
                                currentRecord.setWaypointdescription(textValue);
                            } else if ("text".equalsIgnoreCase(tagName)){
                                currentRecord.setText(textValue);
                            } else if ("latitude".equalsIgnoreCase(tagName)){
                                currentRecord.setLatitude(textValue);
                            } else if ("longitude".equalsIgnoreCase(tagName)){
                                currentRecord.setLongitude(textValue);
                            }
                        }
                        break;
                    default: //nothing else to do
                }
                eventType = xpp.next();
            }

            for (WayPointEntry mydata4: waypointdata){
                Log.d(TAG, "/////////////////////////////");
                Log.d(TAG, mydata4.toString());
            }

        }catch (Exception e) {
            status = false;
            e.printStackTrace();
        }

        return status;
    }

    public boolean parseRoomWayPoint(String xmlData) {
        boolean status = true;
        RoomWayPointEntry currentRecord = null;
        boolean inEntry = false;
        String textValue = "";

        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput (new StringReader(xmlData));
            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){
                String tagName = xpp.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        Log.d(TAG, "parse: Starting tag for " + tagName);
                        if ("waypoint".equalsIgnoreCase(tagName)|"room".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            currentRecord = new RoomWayPointEntry();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        Log.d(TAG, "parse: Ending tag for " + tagName);
                        if(inEntry){
                            if("waypoint".equalsIgnoreCase(tagName)|"room".equalsIgnoreCase(tagName)){
                                roomwaypointdata.add(currentRecord);
                                inEntry = false;//Ending tag for entry
                            } else if ("levelNo".equalsIgnoreCase(tagName)) {//Guaranteed not to be Null
                                currentRecord.setLevelnumber(textValue);
                            } else if ("floorPlanId".equalsIgnoreCase(tagName)){//Guaranteed not to be Null
                                currentRecord.setFloorplanid(textValue);
                            } else if ("floordescription".equalsIgnoreCase(tagName)) {//Guaranteed not to be Null
                                currentRecord.setFloordescription(textValue);
                            }else if ("name".equalsIgnoreCase(tagName)){//Guaranteed not to be Null
                                currentRecord.setName(textValue);
                            }else if ("waypointdescription".equalsIgnoreCase(tagName)|"roomdescription".equalsIgnoreCase(tagName)){//Guaranteed not to be Null
                                currentRecord.setDescription(textValue);
                            } else if ("text".equalsIgnoreCase(tagName)){
                                currentRecord.setText(textValue);
                            } else if ("latitude".equalsIgnoreCase(tagName)){
                                currentRecord.setLatitude(textValue);
                            } else if ("longitude".equalsIgnoreCase(tagName)){
                                currentRecord.setLongitude(textValue);
                            }
                        }
                        break;
                    default: //nothing else to do
                }
                eventType = xpp.next();
            }

            for (RoomWayPointEntry mydata5: roomwaypointdata){
                Log.d(TAG, "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                Log.d(TAG, mydata5.toString());
            }

        }catch (Exception e) {
            status = false;
            e.printStackTrace();
        }

        return status;
    }

    public boolean parseRoute(String xmlData) {
        boolean status = true;
        RouteEntry currentRecord = null;
        boolean inEntry = false;
        String textValue = "";

        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput (new StringReader(xmlData));
            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){
                String tagName = xpp.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        Log.d(TAG, "parse: Starting tag for " + tagName);
                        if ("route".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            currentRecord = new RouteEntry();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        Log.d(TAG, "parse: Ending tag for " + tagName);
                        if(inEntry){
                            if("route".equalsIgnoreCase(tagName)){
                                routedata.add(currentRecord);
                                inEntry = false;//Ending tag for entry
                            } else if ("levelNo".equalsIgnoreCase(tagName)) {//Guaranteed not to be Null
                                currentRecord.setLevelNo(textValue);
                            } else if ("floorPlanId".equalsIgnoreCase(tagName)){//Guaranteed not to be Null
                                currentRecord.setFloorPlanId(textValue);
                            } else if ("description".equalsIgnoreCase(tagName)){//Guaranteed not to be Null
                                currentRecord.setRoutedescription(textValue);
                            } else if ("startname".equalsIgnoreCase(tagName)){
                                currentRecord.setStartpointname(textValue);
                            } else if ("starttext".equalsIgnoreCase(tagName)) {
                                currentRecord.setStartpointtext(textValue);
                            } else if ("firstname".equalsIgnoreCase(tagName)){
                                    currentRecord.setFirstwaypointname(textValue);
                            } else if ("firsttext".equalsIgnoreCase(tagName)){
                                    currentRecord.setFirstwaypointtext(textValue);
                            } else if ("secondname".equalsIgnoreCase(tagName)) {
                                currentRecord.setSecondwaypointname(textValue);
                            } else if ("secondtext".equalsIgnoreCase(tagName)) {
                                currentRecord.setSecondwaypointtext(textValue);
                            } else if ("endname".equalsIgnoreCase(tagName)) {
                                    currentRecord.setEndpointname(textValue);
                            } else if ("endtext".equalsIgnoreCase(tagName)) {
                                currentRecord.setEndpointtext(textValue);
                            } else if ("firstconfirmation".equalsIgnoreCase(tagName)) {
                                currentRecord.setFirstconfirmation(textValue);
                            } else if ("secondconfirmation".equalsIgnoreCase(tagName)){
                                    currentRecord.setSecondconfirmation(textValue);
                            }
                        }
                        break;
                    default: //nothing else to do
                }
                eventType = xpp.next();
            }

            for (RouteEntry mydata2: routedata){
                Log.d(TAG, "++++++++++++++++++++");
                Log.d(TAG, mydata2.toString());
            }

        }catch (Exception e) {
            status = false;
            e.printStackTrace();
        }

        return status;
    }

    public boolean parseText(String xmlData) {
        boolean status = true;
        TextEntry currentRecord = null;
        boolean inEntry = false;
        String textValue = "";

        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput (new StringReader(xmlData));
            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){
                String tagName = xpp.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        Log.d(TAG, "parse: Starting tag for " + tagName);
                        if ("maintext".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            currentRecord = new TextEntry();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        Log.d(TAG, "parse: Ending tag for " + tagName);
                        if(inEntry){
                            if("maintext".equalsIgnoreCase(tagName)){
                                textdata.add(currentRecord);
                                inEntry = false;//Ending tag for entry
                            } else if ("introtext".equalsIgnoreCase(tagName)){//Guaranteed not to be Null
                                currentRecord.setIntrotext(textValue);
                            } else if ("locationtext".equalsIgnoreCase(tagName)){//Guaranteed not to be Null
                                currentRecord.setLocationtext(textValue);
                            } else if ("menutext".equalsIgnoreCase(tagName)){
                                currentRecord.setMenutext(textValue);
                            } else if ("roomtext".equalsIgnoreCase(tagName)){
                                currentRecord.setRoomtext(textValue);
                            } else if ("waypointtext".equalsIgnoreCase(tagName)) {
                                currentRecord.setWaypointtext(textValue);
                            } else if ("confirmationtext".equalsIgnoreCase(tagName)) {
                                currentRecord.setConfirmationtext(textValue);
                            } else if ("repeattext".equalsIgnoreCase(tagName)) {
                                currentRecord.setRepeattext(textValue);
                            } else if ("servicetext".equalsIgnoreCase(tagName)) {
                                currentRecord.setServicetext(textValue);
                            } else if ("eventsintrotext".equalsIgnoreCase(tagName)) {
                                currentRecord.setEventsintrotext(textValue);
                            }else if ("noeventstext".equalsIgnoreCase(tagName)) {
                                currentRecord.setNoeventstext(textValue);
                            } else if ("routetext".equalsIgnoreCase(tagName)) {
                                currentRecord.setRoutetext(textValue);
                            } else if ("noroutetext".equalsIgnoreCase(tagName)) {
                                currentRecord.setNoroutetext(textValue);
                            }else if ("resumetext".equalsIgnoreCase(tagName)){
                                    currentRecord.setResumetext(textValue);
                            }
                        }
                        break;
                    default: //nothing else to do
                }
                eventType = xpp.next();
            }

            for (TextEntry mydata3: textdata){
                Log.d(TAG, "=====================");
                Log.d(TAG, mydata3.toString());
            }

        }catch (Exception e) {
            status = false;
            e.printStackTrace();
        }

        return status;
    }

    public boolean parseConfiguration(String xmlData) {
        boolean status = true;
        ConfigEntry currentRecord = null;
        boolean inEntry = false;
        String textValue = "";

        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput (new StringReader(xmlData));
            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){
                String tagName = xpp.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        Log.d(TAG, "parse: Starting tag for " + tagName);
                        if ("parameter".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            currentRecord = new ConfigEntry();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        Log.d(TAG, "parse: Ending tag for " + tagName);
                        if(inEntry){
                            if("parameter".equalsIgnoreCase(tagName)){
                                configurationdata.add(currentRecord);
                                inEntry = false;//Ending tag for entry
                            } else if ("defaultinterval".equalsIgnoreCase(tagName)) {//Guaranteed not to be Null
                                currentRecord.setDefaultinterval(textValue);
                            } else if ("defaultdisplacement".equalsIgnoreCase(tagName)){//Guaranteed not to be Null
                                currentRecord.setDefaultdisplacement(textValue);
                            } else if ("defaultaccuracy".equalsIgnoreCase(tagName)) {//Guaranteed not to be Null
                                currentRecord.setDefaultaccuracy(textValue);
                            } else if ("defaultcurrentdistance".equalsIgnoreCase(tagName)) {//Guaranteed not to be Null
                                currentRecord.setDefaultcurrentdistance(textValue);
                            } else if ("defaultspeechrate".equalsIgnoreCase(tagName)) {//Guaranteed not to be Null
                                currentRecord.setDefaultspeechrate(textValue);
                            } else if ("defaultpitch".equalsIgnoreCase(tagName)) {//Guaranteed not to be Null
                                currentRecord.setDefaultpitch(textValue);
                            } else if ("firstfixlatitude".equalsIgnoreCase(tagName)) {//Guaranteed not to be Null
                                currentRecord.setFirstfixlatitude(textValue);
                            } else if ("firstfixlongitude".equalsIgnoreCase(tagName)) {//Guaranteed not to be Null
                                currentRecord.setFirstfixlongitude(textValue);
                            } else if ("firstfixfloor".equalsIgnoreCase(tagName)) {//Guaranteed not to be Null
                                currentRecord.setFirstfixfloor(textValue);
                            } else if ("firstfixaccuracy".equalsIgnoreCase(tagName)) {//Guaranteed not to be Null
                                    currentRecord.setFirstfixaccuracy(textValue);
                        }
                        }
                        break;
                    default: //nothing else to do
                }
                eventType = xpp.next();
            }

            for (ConfigEntry mydata6: configurationdata){
                Log.d(TAG, "..............................");
                Log.d(TAG, mydata6.toString());
            }

        }catch (Exception e) {
            status = false;
            e.printStackTrace();
        }

        return status;
    }
}

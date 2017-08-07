package com.example.salfino.naviblind_alpha;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Saviour on 26/07/2017.
 */
@RunWith(AndroidJUnit4.class)
public class ParseIndoorAtlasIntrumentedTest {
    private RoomEntry myRoomEntry;
    private ArrayList<RoomEntry> roomdata = new ArrayList<>();
    private ParseIndoorAtlas mParseIndoorAtlas = new ParseIndoorAtlas();

    @Test
    public void parseRoom() throws Exception {

        myRoomEntry = new RoomEntry();
        myRoomEntry.setLevelnumber("A");
        myRoomEntry.setFloorplanid("B");
        myRoomEntry.setFloordescription("C");
        myRoomEntry.setName("D");
        myRoomEntry.setRoomdescription("E");
        myRoomEntry.setLatitude("F");
        myRoomEntry.setLongitude("G");
        myRoomEntry.setText("H");

        for (int i = 0; i < 3;i++){
            roomdata.add(i,myRoomEntry);
        }

        StringBuilder xmlResult = new StringBuilder();
        String file = "res/raw/ia_config_test.xml";
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);

        int charsRead;
        char[] inputBuffer = new char[500];//Read 500 characters at a time
        while(true){
            charsRead = reader.read(inputBuffer);
            if(charsRead < 0){//End of stream of data
                break;
            }
            if(charsRead > 0){//Keep count of number of characters read from stream
                xmlResult.append(String.copyValueOf(inputBuffer,0,charsRead));//Append until there is no more data to read
            }
        }

        reader.close();//All IO object will be closed
        String myXML =  xmlResult.toString();
        //assertFalse(mParseIndoorAtlas.parseRoom(myXML));
        //assertTrue(mParseIndoorAtlas.parseRoom(myXML));
        //assertEquals(mParseIndoorAtlas.parseRoomTest(myXML),roomdata);
        Assert.assertEquals(mParseIndoorAtlas.parseRoomTest(myXML),roomdata);
    }

    @Test
    public void parseEvent() throws Exception {

    }

    @Test
    public void parseWayPoint() throws Exception {

    }

    @Test
    public void parseRoomWayPoint() throws Exception {

    }

    @Test
    public void parseRoute() throws Exception {

    }

    @Test
    public void parseText() throws Exception {

    }

    @Test
    public void parseConfiguration() throws Exception {

    }

}
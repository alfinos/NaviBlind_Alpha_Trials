package com.example.salfino.naviblind_110217;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.indooratlas.android.sdk.IAGeofenceEvent;
import com.indooratlas.android.sdk.IAGeofenceListener;
import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;
import com.indooratlas.android.sdk.IARegion;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private int[] audioCommands = {//Array of sounds
            R.raw.alert,//0
            R.raw.tick,//1
            R.raw.metal_metronome,//2
    };
    //Waypoint geo-coordinates in decimal degrees (DD)
    public double currentDistance = 10000d;//Initialize current distance to a ridiculously large number
    //private static final int REQUEST_CODE = 1234;
    private final int MY_CODE_PERMISSIONS = 1;
    private static final int PERMISSION_REQUEST_COARSE_BL = 2;
    //private long DEFAULT_INTERVAL = 200L;//milliseconds
    //private float DEFAULT_DISPLACEMENT = 0.8f;//meters
    public long DEFAULT_INTERVAL;
    public float DEFAULT_DISPLACEMENT;
    public double DEFAULT_ACCURACY;
    public double DEFAULT_CURRENT_DISTANCE;
    public float DEFAULT_SPEECH_RATE;
    public float DEFAULT_PITCH;
    public IALocationManager mIALocationManager;
    public MediaPlayer mPlayer;
    public SpeechRecognizer mSR;
    public BluetoothAdapter mBTAdapter;
    private TextView mLogging;
    private TextView mTextView;
    private ScrollView mScrollView;
    private static final String TAG = "IndoorAtlas";
    private long mRequestStartTime;
    private boolean calibrationOK = false;
    private boolean statusOK = false;
    private boolean permissionOK = false;

    private boolean startOfRoute = true;
    private boolean duringRoute = false;
    private boolean startTextFlag = false;
    private boolean firstTextFlag = false;
    private boolean secondTextFlag = false;

    public boolean endFlag = false;

    BluetoothLeScanner scanner;
    ScanSettings scanSettings;
    public boolean mScanning;
    String dName = "";
    String macAddress = "";
    public double rssiGR = -17;
    public double rssiSR = -17;
    public double rssiSG = -17;
    public double metersGR = 10000;//Initialize current distance to a ridiculously large number
    public double metersSR = 10000;//Initialize current distance to a ridiculously large number
    public double metersSG = 10000;//Initialize current distance to a ridiculously large number
    private GestureDetectorCompat gestureObject;
    TextToSpeech t1;
    Bundle params = new Bundle();
    public ArrayList<RouteEntry> routeData;
    public ArrayList<WayPointEntry> waypointData;
    public ArrayList<TextEntry> textData;
    public ArrayList<ConfigEntry> configData;
    public ArrayList<RoomEntry> roomData;
    public ArrayList<EventEntry> eventData;
    public ArrayList<RoomWayPointEntry> roomwaypointData;
    public String xmlFile;
    public String introutterance = "";
    public String confirmationutterance = "";
    public String locationutterance = "";
    public String menuutterance = "";
    public String roomutterance = "";
    public String waypointutterance = "";
    public String repatutterance = "";
    public String serviceutterance = "";
    public String routeutterance = "";
    public String mytempLocation = "";
    public int chosenRouteIndex = 0;


    private void logText(String msg) {
        //double duration = mRequestStartTime != 0
        //        ? (SystemClock.elapsedRealtime() - mRequestStartTime) / 1e3
        //        : 0d;
        mLogging.setText(String.format(Locale.UK, "\n %s", msg));
        mLogging.setTextSize(25);
        mLogging.setTextColor(0xFFFF4046);
        mScrollView.smoothScrollBy(0, mLogging.getBottom());
    }

    //Method to convert a byte array to a HEX. string.
    private String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b & 0xff));

        return sb.toString();
    }

    public void initialiseBLE(){

        final BluetoothManager bluetoothManager =  (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBTAdapter = bluetoothManager.getAdapter();//Get the Bluetooth Adapter first
        //Create the scan settings
        ScanSettings.Builder scanSettingsBuilder = new ScanSettings.Builder();
        //Set scan latency mode. Lower latency, faster device detection/more battery and resources consumption
        scanSettingsBuilder.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);
        //Wrap settings together and save on a settings var (declared globally).
        scanSettings = scanSettingsBuilder.build();
        //Get the BLE scanner from the BT adapter (var declared globally)
        scanner = mBTAdapter.getBluetoothLeScanner();
    }

    public String initializeTTS(){

        String initString = "";

        TextEntry myTextObject = textData.get(0);
        introutterance = myTextObject.getIntrotext();
        confirmationutterance = myTextObject.getConfirmationtext();
        locationutterance = myTextObject.getLocationtext();
        menuutterance = myTextObject.getMenutext();
        roomutterance = myTextObject.getRoomtext();
        waypointutterance = myTextObject.getWaypointtext();
        repatutterance = myTextObject.getRepeattext();
        serviceutterance = myTextObject.getServicetext();
        routeutterance = myTextObject.getRoutetext();

        //checkSpecialEvents();

        String id = "85b435a5-971f-47c3-8d33-23831680cca0";
        for (int i = 0; i < roomData.size();i++){
            RoomEntry myObject = roomData.get(i);
            String currentplanid = myObject.getFloorplanid();
            if (currentplanid.equals(id)){
                String floorutterance = myObject.getFloordescription();
                initString = introutterance + floorutterance + "... " + repatutterance;
                break;
            }
        }
        return initString;
    }

    public String checkSpecialEvents(){
        String eventString = "";

        return  eventString;
    }

    public void menu(String currentLocation){
        String allLocations = "";

        for (int i = 0; i < roomData.size();i++){
            RoomEntry myObject = roomData.get(i);
            String currentName = myObject.getName();
            String currentDescription = myObject.getRoomdescription();
            String fullDescription = currentName + " which is " + currentDescription;
            allLocations = fullDescription + "... " + allLocations;
        }
        texttospeech(menuutterance + "... " +roomutterance +"... " + allLocations,"Menu",currentLocation);
    }

    public void chooseRoute (String startname,String endname ){
        //Toast.makeText(MainActivity.this, "!START NAME::"+startname, Toast.LENGTH_SHORT).show();
        //Toast.makeText(MainActivity.this, "!END NAME::"+endname, Toast.LENGTH_SHORT).show();
        //int endnameconvert = Integer.parseInt(endname);
        //Toast.makeText(MainActivity.this, "INDEX AT START OF LOOP::"+chosenRouteIndex, Toast.LENGTH_SHORT).show();
        for (int i = 0; i < routeData.size();i++){
            RouteEntry myObject = routeData.get(i);
            String currentStartName = myObject.getStartpointname().trim();
            String currentEndName = myObject.getEndpointname().trim();
            //int currentEndNameConvert = Integer.parseInt(currentEndName);
            if (currentStartName.equalsIgnoreCase(startname.trim())&& currentEndName.equalsIgnoreCase(endname.trim())){
                chosenRouteIndex = i;
            }else{
                Toast.makeText(MainActivity.this, "Route not found!!!!!!!!!", Toast.LENGTH_SHORT).show();
            }
        }
        //Toast.makeText(MainActivity.this, "INDEX OUTSIDE LOOP::"+chosenRouteIndex, Toast.LENGTH_SHORT).show();
        RouteEntry myObject = routeData.get(chosenRouteIndex);
        String currentDescription = myObject.getRoutedescription();

        String inputString = routeutterance + currentDescription;
        texttospeech(inputString,"OnChoosingRoute",startname);
    }

    public void launchRoute(String currentLocation){
        RouteEntry myObject = routeData.get(chosenRouteIndex);
        String startText = myObject.getStartpointtext();
        texttospeech(startText,"OnStartText",currentLocation);
    }

    public void launchFirstText(String currentLocation){
        RouteEntry myObject = routeData.get(chosenRouteIndex);
        String firstText = myObject.getFirstwaypointtext();
        texttospeech(firstText,"OnFirstText",currentLocation);
    }

    public void launchSecondText(String currentLocation){
        RouteEntry myObject = routeData.get(chosenRouteIndex);
        String secondText = myObject.getSecondwaypointtext();
        texttospeech(secondText,"OnSecondText",currentLocation);
    }

    public void launchEndText(String currentLocation){
        RouteEntry myObject = routeData.get(chosenRouteIndex);
        String endText = myObject.getEndpointtext();
        texttospeech(endText,"OnEndRoute",currentLocation);
    }

    private void startLeScan(boolean enable) {
        if (enable) {
            mScanning = true;//Start BLE Scan
            scanner.startScan(null, scanSettings, mScanCallback);
        }else{
            //Stop BLE scan
            mScanning = false;
            scanner.stopScan(mScanCallback);
        }
    }

    private double getDistance(double rssi, String location) {//RSSI (dBm) = -10n log10(d) + A and n = 2 for free space and A is average RSSI at 1m
        double A = -60.0;
        if (location.equals("GC")){
            A = -60.0;// average RSSI for beacon installed in George Roussos office
        } else if (location.equals("SR")) {
            A =  -50.0; // average RSSI for beacon isntalled in Staff Room, next to lifts
        } else if (location.equals("SG")) {
            A = -62.0; // average RSSI for beacon isntalled in Systems Group Area
        }
        return Math.pow(10.0,((rssi-(A))/-25.0));//-60dBm is average RSSI at 1m distance i.e. A
    }

    //Finding BLE Devices
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            //String advertisingString = byteArrayToHex(result.getScanRecord().getBytes());
            dName = result.getDevice().getName();
            if (dName != null){
                dName = (result.getDevice().getName()).trim();
            }
            macAddress = result.getDevice().getAddress();
            if (macAddress != null){
                macAddress = (result.getDevice().getAddress()).trim();
            }

            if (macAddress != null && macAddress.equals("F4:46:EA:8F:C2:2D")) {
                rssiGR = result.getRssi();
                metersGR = getDistance(rssiGR,"GC");

            } else if (macAddress != null && macAddress.equals("C3:4E:E7:D1:2E:3A")) {
                rssiSR = result.getRssi();
                metersSR = getDistance(rssiSR, "SR");

            } else if (macAddress != null && macAddress.equals("C7:96:98:06:4C:64")) {
                rssiSG = result.getRssi();
                metersSG = getDistance(rssiSG,"SG");
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };

    private void initialiseBluetooth(){

        //Check if device does support BT by hardware
        if (!getBaseContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            //Toast shows a message on the screen for a LENGTH_SHORT period
            Toast.makeText(this, "BLUETOOTH NOT SUPPORTED!", Toast.LENGTH_SHORT).show();
            finish();
        }
        //Check if device does support BT Low Energy by hardware. Else close the app(finish())!
        if (!getBaseContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            //Toast shows a message on the screen for a LENGTH_SHORT period
            Toast.makeText(this, "BLE NOT SUPPORTED!", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            //If BLE is supported, get the BT adapter. Preparing for use!
            mBTAdapter = BluetoothAdapter.getDefaultAdapter();
            //If getting the adapter returns error, close the app with error message!
            if (mBTAdapter == null) {
                Toast.makeText(this, "ERROR GETTING BLUETOOTH ADAPTER!", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                //Check if BT is enabled! This method requires BT permissions in the manifest.
                if (!mBTAdapter.isEnabled()) {
                    //If it is not enabled, ask user to enable it with default BT enable dialog! BT enable response will be received in the onActivityResult method.
                    Intent enableBTintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBTintent, PERMISSION_REQUEST_COARSE_BL);
                }
            }
        }
    }

   private void mediaPlayer (final File file, final String utteranceID, final String currentLocationName){

       try {

           Uri myuri = Uri.parse(file.getPath());
           mPlayer = MediaPlayer.create(this,myuri);
           AudioAttributes myAttributes = new AudioAttributes.Builder()
                   .setUsage(AudioAttributes.USAGE_MEDIA)
                   .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                   .build();
           mPlayer.setAudioAttributes(myAttributes);
           mPlayer.setLooping(false);
           mPlayer.start();

           mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
               @Override
               public void onCompletion(MediaPlayer mp) {
                   mp.stop();
                   mp.release();
                   mPlayer = null;
                   file.delete();

                   switch (utteranceID) {
                       case "OnInitialization":
                           mIALocationManager.registerRegionListener(mRegionListener);
                           mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mIALocationListener);
                           break;
                       case "OnLocationChanged":
                           mIALocationManager.registerRegionListener(mRegionListener);
                           mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mIALocationListener);
                           break;
                       case "OnLocationChangedStart":
                           startOfRoute = false;
                           menu(currentLocationName);
                           break;
                       case "Menu":
                           mytempLocation = currentLocationName;
                           startVoiceRecognitionActivity();
                           break;
                       case "Repeat":
                           startOfRoute = true;//?? CHECK!!
                           mIALocationManager.registerRegionListener(mRegionListener);
                           mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mIALocationListener);
                           break;
                       case "OnChoosingRoute":
                           launchRoute(currentLocationName);
                           break;
                       case "OnStartText":
                           startOfRoute = false;
                           duringRoute = true;
                           startTextFlag = true;
                           firstTextFlag = false;
                           secondTextFlag = false;
                           mIALocationManager.registerRegionListener(mRegionListener);
                           mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mIALocationListener);
                           break;
                       case "OnFirstText":
                           startOfRoute = false;
                           duringRoute = true;
                           startTextFlag = false;
                           firstTextFlag = true;
                           secondTextFlag = false;
                           mIALocationManager.registerRegionListener(mRegionListener);
                           mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mIALocationListener);
                           break;
                       case "OnSecondText":
                           startOfRoute = false;
                           duringRoute = true;
                           startTextFlag = false;
                           firstTextFlag = false;
                           secondTextFlag = true;
                           mIALocationManager.registerRegionListener(mRegionListener);
                           mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mIALocationListener);
                           break;
                       case "OnEndRoute":
                           startOfRoute = false;
                           duringRoute = false;
                           firstTextFlag = false;
                           secondTextFlag = false;
                           mIALocationManager.removeLocationUpdates(mIALocationListener);
                           mIALocationManager.unregisterRegionListener(mRegionListener);
                           endFlag = true;
                           break;
                   }
               }
           });

           mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
               @Override
               public boolean onError(MediaPlayer mp, int what, int extra) {
                   Toast.makeText(MainActivity.this, "ERROR!!!!!!", Toast.LENGTH_SHORT).show();
                   switch (what) {
                       case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                           switch (extra) {
                               case MediaPlayer.MEDIA_ERROR_IO:
                                   break;
                               case MediaPlayer.MEDIA_ERROR_MALFORMED:
                                   break;
                               case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                                   break;
                               case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                                   break;
                           }
                           logText("ERROR: " + "What Code: " + what + "Extra Code: " +extra);
                           break;
                       case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                           switch (extra) {
                               case MediaPlayer.MEDIA_ERROR_IO:
                                   break;
                               case MediaPlayer.MEDIA_ERROR_MALFORMED:
                                   break;
                               case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                                   break;
                               case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                                   break;
                           }
                           logText("ERROR: " + "What Code: " + what + "Extra Code: " +extra);
                           break;
                   }
                   return false;
               }
           });
       } catch (Exception e) {
           Toast.makeText(MainActivity.this, "Check Wi-Fi connection or audio file missing!!", Toast.LENGTH_LONG).show();
       }
   }
    private void playSound (final int audioCommandIndex) {

        try {
            mPlayer = MediaPlayer.create(this,audioCommands[audioCommandIndex]);
            AudioAttributes myAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            mPlayer.setAudioAttributes(myAttributes);
            mPlayer.setLooping(false);
            mPlayer.start();

            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                    mp.release();
                    mPlayer = null;
                }
            });

            mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Toast.makeText(MainActivity.this, "ERROR!!!!!!", Toast.LENGTH_SHORT).show();
                    switch (what) {
                        case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                            switch (extra) {
                                case MediaPlayer.MEDIA_ERROR_IO:
                                    break;
                                case MediaPlayer.MEDIA_ERROR_MALFORMED:
                                    break;
                                case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                                    break;
                                case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                                    break;
                            }
                            logText("ERROR: " + "What Code: " + what + "Extra Code: " +extra);
                            break;
                        case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                            switch (extra) {
                                case MediaPlayer.MEDIA_ERROR_IO:
                                    break;
                                case MediaPlayer.MEDIA_ERROR_MALFORMED:
                                    break;
                                case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                                    break;
                                case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                                    break;
                            }
                            logText("ERROR: " + "What Code: " + what + "Extra Code: " +extra);
                            break;
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Check Wi-Fi connection or audio file missing!!", Toast.LENGTH_LONG).show();
        }
    }
    private void texttospeech(final String text, final String utteranceId,final String currentLocationName){

        mLogging.setText("");
        mLogging.setText(text);
        mLogging.setTextSize(35);
        mLogging.setTextColor(0xFFFFFFFF);
        mScrollView.smoothScrollBy(0, mLogging.getBottom());
        final File file = new File(this.getFilesDir(),"tempfile");

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    t1.setLanguage(Locale.UK);
                    t1.setSpeechRate(DEFAULT_SPEECH_RATE);
                    t1.setPitch(DEFAULT_PITCH);
                    t1.synthesizeToFile(text,params,file,utteranceId);
                    //t1.speak(text,TextToSpeech.QUEUE_FLUSH,params,utteranceId);
                }
            }
        });

        t1.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {

            }

            @Override
            public void onDone(final String utteranceId) {
                if (utteranceId.equals("OnInitialization")){
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mediaPlayer(file,utteranceId,currentLocationName);
                            //mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mIALocationListener);
                        }
                    });

                } else if (utteranceId.equals("OnLocationChanged")){

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mediaPlayer(file,utteranceId,currentLocationName);
                            //mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mIALocationListener);
                        }
                    });

                } else if (utteranceId.equals("OnLocationChangedStart")){

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mediaPlayer(file,utteranceId,currentLocationName);
                           // startOfRoute = false;
                           // menu(currentLocationName);
                        }
                    });
                }else if (utteranceId.equals("Menu")){

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mediaPlayer(file,utteranceId,currentLocationName);
                            //mytempLocation = currentLocationName;
                            //startVoiceRecognitionActivity();
                        }
                    });
                } else if (utteranceId.equals("Repeat")){

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mediaPlayer(file,utteranceId,currentLocationName);
                            //startOfRoute = true;//?? CHECK!!
                            //mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mIALocationListener);
                        }
                    });
                }else if (utteranceId.equals("OnChoosingRoute")){

                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mediaPlayer(file,utteranceId,currentLocationName);
                            //launchRoute(currentLocationName);
                        }
                    });
                }else if (utteranceId.equals("OnStartText")){

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mediaPlayer(file,utteranceId,currentLocationName);
//                            startOfRoute = false;
//                            duringRoute = true;
//                            startTextFlag = true;
//                            firstTextFlag = false;
//                            secondTextFlag = false;
//                            mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mIALocationListener);
                        }
                    });
                }else if (utteranceId.equals("OnFirstText")){

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mediaPlayer(file,utteranceId,currentLocationName);
//                            startOfRoute = false;
//                            duringRoute = true;
//                            startTextFlag = false;
//                            firstTextFlag = true;
//                            secondTextFlag = false;
//                            mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mIALocationListener);
                        }
                    });
                }else if (utteranceId.equals("OnSecondText")){

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mediaPlayer(file,utteranceId,currentLocationName);
//                            startOfRoute = false;
//                            duringRoute = true;
//                            startTextFlag = false;
//                            firstTextFlag = false;
//                            secondTextFlag = true;
//                            mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mIALocationListener);
                        }
                    });
                }else if (utteranceId.equals("OnEndRoute")){

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mediaPlayer(file,utteranceId,currentLocationName);
//                            startOfRoute = false;
//                            duringRoute = false;
//                            firstTextFlag = false;
//                            secondTextFlag = false;
//                            mIALocationManager.removeLocationUpdates(mIALocationListener);
//                            endFlag = true;
                        }
                    });
                }

            }

            @Override
            public void onError(String utteranceId) {

            }
        });
    }

    private void ttsSilence(final String utteranceId){

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    t1.playSilentUtterance(500,1,utteranceId);
                }
            }
        });

        t1.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {

            }

            @Override
            public void onDone(String utteranceId) {
                if (utteranceId.equals("1")){
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startVoiceRecognitionActivity();//Launch speech recogniser
                        }
                    });

                }

            }

            @Override
            public void onError(String utteranceId) {

            }
        });
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private class DebugGesture extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if(!endFlag){
                if(mPlayer != null && mPlayer.isPlaying()){
                    mPlayer.seekTo(0);
                }
            }else{

                new CountDownTimer(2000, 1000){// 2 seconds count down timer
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        startOfRoute = true;
                        endFlag = false;
                        mIALocationManager.registerRegionListener(mRegionListener);
                        String initString = initializeTTS();
                        texttospeech(initString,"OnInitialization","");

                    }
                }.start();

            }
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if(e2.getX() > e1.getX()){
                if(mPlayer != null && mPlayer.isPlaying())//Action when swiping to the RIGHT
                {
                   mPlayer.pause();
                }

            } else if (e2.getX() < e1.getX()){//Action when swiping to the LEFT

                if(mPlayer != null)
                {
                   mPlayer.start();
                }
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] neededPermissions = {
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.RECORD_AUDIO
        };
        ActivityCompat.requestPermissions( this, neededPermissions, MY_CODE_PERMISSIONS);
        setContentView(R.layout.activity_main);

        mLogging = (TextView) findViewById(R.id.mytextView);
        mTextView = (TextView) findViewById(R.id.coordinates);
        mTextView.setTextSize(15);
        mTextView.setTextColor(0xFFFF4046);
        mScrollView = (ScrollView) findViewById(R.id.myscrollView);

        Log.d(TAG, "onCreate: starting Asynctask");
        DownloadXML downloadData = new DownloadXML();
        downloadData.execute("http://naviblind.000webhostapp.com/configuration.xml");
        Log.d(TAG, "onCreate: done");

        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"");
        //map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"123");

        gestureObject = new GestureDetectorCompat(this, new DebugGesture());

        //Create a new instance of IALocationManager using its create() method
        mIALocationManager = IALocationManager.create(this);
       /* IALocation location = new IALocation.Builder().withLatitude(START_POSITION_LAT)
                                                      .withLongitude(START_POSITION_LON)
                                                      .withAccuracy(75f)
                                                      .withFloorLevel(2).build();*/
        //IALocation location = new IALocation.Builder().withAccuracy(75f).withFloorLevel(2).build();
        //mIALocationManager.setLocation(location);//Explicitly set the the initial fix as specified above*/

        //Create edges with unique clockwise (lat, lon) points
//        List edges = Arrays.asList(new double[][]{{51.52231720,-0.13089649},{51.52230740,-0.13089247},
//                {51.52231219,-0.13088007},{51.52230218,-0.13087805}});
//        //Create the geofence for 10 seconds in floor number 2
//        IAGeofence geofence = new IAGeofence.Builder()
//                .withEdges(edges)
//                .withFloor(2)
//                .withId("Stairs E GeoFence")
//                .withTransitionType(IAGeofence.GEOFENCE_TRANSITION_ENTER | IAGeofence.GEOFENCE_TRANSITION_EXIT).build();
//
//        IAGeofenceRequest geofenceRequest = new IAGeofenceRequest.Builder()
//                .withGeofence(geofence)
//                .withInitialTrigger(IAGeofenceRequest.INITIAL_TRIGGER_ENTER).build();
//
//        mIALocationManager.addGeofences(geofenceRequest,mIAGeofenceListener);

        //Create a new instance of SpeechRecognizer using its createSpeechRecognizer() method
        mSR = SpeechRecognizer.createSpeechRecognizer(this);
        //Set the Speech Listener as the new speechListener defined in inner class below
        mSR.setRecognitionListener(new speechListener());

        initialiseBluetooth();
        initialiseBLE();
        //startLeScan(true);
        playSound(0);//Play intro sound

        new CountDownTimer(5000, 1000){// 5 seconds count down timer
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                ConfigEntry myConfigObject = configData.get(0);
                DEFAULT_DISPLACEMENT = Float.parseFloat(myConfigObject.getDefaultdisplacement());
                DEFAULT_INTERVAL = Long.parseLong(myConfigObject.getDefaultinterval());
                DEFAULT_CURRENT_DISTANCE = Double.parseDouble(myConfigObject.getDefaultcurrentdistance());
                DEFAULT_ACCURACY = Double.parseDouble(myConfigObject.getDefaultaccuracy());
                DEFAULT_SPEECH_RATE = Float.parseFloat(myConfigObject.getDefaultspeechrate());
                DEFAULT_PITCH = Float.parseFloat(myConfigObject.getDefaultpitch());

                IALocationRequest request = IALocationRequest.create();
                request.setPriority(IALocationRequest.PRIORITY_HIGH_ACCURACY);//High-accuracy updates requested
                request.setFastestInterval(DEFAULT_INTERVAL);//Explicitly set the fastest interval for location updates in milliseconds
                request.setSmallestDisplacement(DEFAULT_DISPLACEMENT);//Set the minimum displacement between location updates in meters
                mIALocationManager.registerRegionListener(mRegionListener);
                String initString = initializeTTS();
                texttospeech(initString,"OnInitialization","");
            }
        }.start();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_CODE_PERMISSIONS: {// If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // permission was granted

                    Toast.makeText(MainActivity.this, "Permission granted for coarse location and Wi-Fi status", Toast.LENGTH_SHORT).show();
                    permissionOK = true;
                } else {// permission denied

                    Toast.makeText(MainActivity.this, "Permission denied for coarse location and Wi-Fi status", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public IAGeofenceListener mIAGeofenceListener = new IAGeofenceListener() {
        @Override
        public void onGeofencesTriggered(IAGeofenceEvent iaGeofenceEvent) {
            //Do something with the triggered geofences in geoFenceEvent
            Toast.makeText(MainActivity.this, "In GEOFENCE!!!...", Toast.LENGTH_LONG).show();
            //mTextView.setText("Entered Geofence");
            //mTextView.setTextSize(18);
            //mTextView.setTextColor(0xFFFFFFFF);

        }
    };

    public IALocationListener mIALocationListener = new IALocationListener() {
      //Implement an IALocationListener interface and override its onLocationChanged() callback method
      @Override
      public void onLocationChanged(IALocation iaLocation) {

          String currentLocationName = "";
          mTextView.setText(String.format(Locale.UK, "Latitude: %.8f,\nLongitude: %.8f,\nAccuracy: %.8f,\nCertainty: %.8f,\nLevel: %d",
                  iaLocation.getLatitude(), iaLocation.getLongitude(),iaLocation.getAccuracy(),iaLocation.getFloorCertainty(),
                  iaLocation.getFloorLevel()));
          mTextView.setTextSize(15);

          if (iaLocation.getAccuracy() <= DEFAULT_ACCURACY) { //iaLocation.getFloorLevel() == 2 && iaLocation.getAccuracy()<=5
              String locationString = "No Location";
              for (int i = 0; i < roomwaypointData.size();i++){
                  RoomWayPointEntry myObject = roomwaypointData.get(i);
                  double comparelatitude = Double.parseDouble(myObject.getLatitude());
                  double comparelongitude = Double.parseDouble(myObject.getLongitude());
                  Haversine haversineObject = new Haversine(); //Create the Haversine object
                  currentDistance = 1000*(haversineObject.distance(iaLocation.getLatitude(),iaLocation.getLongitude(),comparelatitude,comparelongitude));

                  if (currentDistance <= DEFAULT_CURRENT_DISTANCE){
                      String textutternace = myObject.getText();
                      String descriptionutternace = myObject.getDescription();
                      currentLocationName = myObject.getName();
                      locationString = textutternace + "... " + descriptionutternace;
                      break;
                  }
              }
              if (!locationString.equals("No Location")){
              mIALocationManager.removeLocationUpdates(mIALocationListener);
                  mIALocationManager.unregisterRegionListener(mRegionListener);
              }

              if ((startOfRoute)&(!duringRoute)&(!locationString.equals("No Location"))){
                  texttospeech(locationString,"OnLocationChangedStart",currentLocationName);
              }else if ((!startOfRoute)&(!duringRoute)&(!locationString.equals("No Location"))) {
                  texttospeech(locationString, "OnLocationChanged", currentLocationName);
              }else if ((!startOfRoute)&(duringRoute)&(!firstTextFlag)&(!secondTextFlag)&(!locationString.equals("No Location"))) {
                  launchFirstText(currentLocationName);
                  //Toast.makeText(MainActivity.this, "Location Changing...in route!!", Toast.LENGTH_SHORT).show();
              }else if ((!startOfRoute)&(duringRoute)&(firstTextFlag)&(!secondTextFlag)&(!locationString.equals("No Location"))) {
                  launchSecondText(currentLocationName);
              }else if ((!startOfRoute)&(duringRoute)&(!firstTextFlag)&(secondTextFlag)&(!locationString.equals("No Location"))){
                  launchEndText(currentLocationName);

              }else {
                  playSound(1);//Play tick sound
              }
          } else {
              playSound(1);//Play tick sound
              //texttospeech(serviceutterance,"OnNoService");
          }
      }

      @Override
      public void onStatusChanged(String provider, int status, Bundle bundle) {
          mPlayer = null;
          switch (status) {
              case IALocationManager.STATUS_CALIBRATION_CHANGED:
                  String quality = "unknown";
                  switch (bundle.getInt("quality")) {
                      case IALocationManager.CALIBRATION_POOR:
                          quality = "Poor";
                          calibrationOK = false;
                          break;
                      case IALocationManager.CALIBRATION_GOOD:
                          quality = "Good";
                          calibrationOK = true;
                          break;
                      case IALocationManager.CALIBRATION_EXCELLENT:
                          quality = "Excellent";
                          calibrationOK = true;
                          break;
                  }
                  //logText("Calibration Quality: " + quality + " Status Code: " + status);
                  logText("Calibration: " + quality);
                  break;
              case IALocationManager.STATUS_AVAILABLE:
                  //logText("onStatusChanged: Available" + " Status Code: " + status);
                  logText("Status: Available");
                  statusOK = true;
                  break;
              case IALocationManager.STATUS_LIMITED:
                  logText("Status: Limited");
                  statusOK = true;
                  playSound(1);//Play tick sound
                  break;
              case IALocationManager.STATUS_OUT_OF_SERVICE:
                  logText("Status: Out of service");
                  statusOK = false;
                  playSound(1);//Play tick sound
                  break;
              case IALocationManager.STATUS_TEMPORARILY_UNAVAILABLE:
                  logText("Status: Temporarily unavailable");
                  playSound(1);//Play tick sound
                  statusOK = false;
          }
      }
  };

    private IARegion.Listener mRegionListener = new IARegion.Listener() {
        @Override
        public void onEnterRegion(IARegion iaRegion) {
            if (iaRegion.getType() == IARegion.TYPE_FLOOR_PLAN) {
                String id = iaRegion.getId();
                Log.d(TAG, "floorPlan changed to " + id);
                for (int i = 0; i < roomData.size();i++){
                    RoomEntry myObject = roomData.get(i);
                    String currentplanid = myObject.getFloorplanid();
                    if (currentplanid.equals(id)){
                        String floorutterance = myObject.getFloordescription();
                        mTextView.setText("FLOOR MATCH:: " + floorutterance);
                        mTextView.setTextSize(15);
                        break;
                    }
                }

                IALocation location = new IALocation.Builder()
                        .withFloorLevel(2).build();
                mIALocationManager.setLocation(location);//Explicitly set floor level to 2
                //Toast.makeText(MainActivity.this, "REGION CHANGE: " + id, Toast.LENGTH_SHORT).show();
                //IALocation location = new IALocation.Builder()
                //        .withFloorLevel(2).build();
                //mIALocationManager.setLocation(location);//Explicitly set floor level to 2
            }
        }

        @Override
        public void onExitRegion(IARegion iaRegion) {
            // leaving a previously entered region
            if (iaRegion.getType() == IARegion.TYPE_FLOOR_PLAN) {
                String id = iaRegion.getId();
                Log.d(TAG, "floorPlan changed to " + id);
                for (int i = 0; i < roomData.size();i++){
                    RoomEntry myObject = roomData.get(i);
                    String currentplanid = myObject.getFloorplanid();
                    if (currentplanid.equals(id)){
                        String floorutterance = myObject.getFloordescription();
                        mTextView.setText("FLOOR MATCH:: " + floorutterance);
                        mTextView.setTextSize(15);
                        break;
                    }
                }

                IALocation location = new IALocation.Builder()
                        .withFloorLevel(2).build();
                mIALocationManager.setLocation(location);//Explicitly set floor level to 2
            }
        }
    };
    @Override
    protected void onResume() {
        if(mPlayer != null)
        {
            mPlayer.start();
        }

        super.onResume();
        //Toast.makeText(MainActivity.this, "DEBUG::onResume() callback...", Toast.LENGTH_LONG).show();
        mRequestStartTime = SystemClock.elapsedRealtime();
        startLeScan(true);
    }

    @Override
    protected void onPause() {
        if(mPlayer != null && mPlayer.isPlaying())
        {
            mPlayer.pause();
        }

        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }

        super.onPause();
        //Toast.makeText(MainActivity.this, "DEBUG::onPause() callback...", Toast.LENGTH_LONG).show();
        mIALocationManager.removeLocationUpdates(mIALocationListener);
        mIALocationManager.removeGeofenceUpdates(mIAGeofenceListener);
        mIALocationManager.unregisterRegionListener(mRegionListener);
        startLeScan(false);
    }

    @Override
    protected void onDestroy() {
        mIALocationManager.destroy();
        startLeScan(false);
        super.onDestroy();
        if(mPlayer != null) {
            mPlayer.release();//releasing and nullifying MediaPLayer
        }

        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.debug_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.menu_item_floor_plan:
                Intent i = new Intent(MainActivity.this, FloorPlanActivity.class);
                startActivity(i);
                if(mPlayer != null && mPlayer.isPlaying())
                {
                    mPlayer.pause();
                }
                return true;

            case R.id.menu_item_BLE:
                Intent k = new Intent(MainActivity.this, BluetoothScanner.class);
                startActivity(k);
                if(mPlayer != null && mPlayer.isPlaying())
                {
                    mPlayer.pause();
                }
                return true;

            case R.id.menu_test_activity:
                Intent m = new Intent(MainActivity.this, TestActivity.class);
                startActivity(m);
                if(mPlayer != null && mPlayer.isPlaying())
                {
                    mPlayer.pause();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class speechListener implements RecognitionListener {

        @Override
        public void onReadyForSpeech(Bundle params) {
            Log.d(TAG, "onReadyForSpeech");
        }

        @Override
        public void onBeginningOfSpeech() {
            Log.d(TAG, "onBeginningOfSpeech");
        }

        @Override
        public void onRmsChanged(float rmsdB) {
            Log.d(TAG, "onRmsChanged");
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            Log.d(TAG, "onBufferReceived");
        }

        @Override
        public void onEndOfSpeech() {
            Log.d(TAG, "onEndofSpeech");
        }

        @Override
        public void onError(int error) {
            Log.d(TAG,  "error " +  error);
            mLogging.setText("");
            mLogging.setText("Error Number " + error + " occurred.");
            mLogging.setTextSize(20);
            mLogging.setTextColor(0xFFFF4046);
            mScrollView.smoothScrollBy(0, mLogging.getBottom());
            //long startTime = System.currentTimeMillis();
            texttospeech(repatutterance,"Repeat","");
            //long estimatedTime = System.currentTimeMillis() - startTime;
        }

        @Override
        public void onResults(Bundle results) {
            Log.d(TAG, "onResults " + results);

            ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            String input = data.get(0).toString();
            String myInput = input.trim();
            mLogging.setText("");
            mLogging.setText(String.format(Locale.UK, "\n %s", "You said : " + myInput));
            mLogging.setTextSize(35);
            mLogging.setTextColor(0xFFFFFFFF);
            mScrollView.smoothScrollBy(0, mLogging.getBottom());
            currentDistance = 100d;
            boolean match = false;
            Toast.makeText(MainActivity.this, "DEBUG::" + myInput, Toast.LENGTH_SHORT).show();

            for (int i = 0; i < roomData.size();i++){
                RoomEntry myObject = roomData.get(i);
                String currentName = myObject.getName();
                currentName = currentName.trim();
                if (myInput.equalsIgnoreCase(currentName)){
                    match = true;
                }
            }
            if (myInput.equals("yes go on")){
                duringRoute = true;
                mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mIALocationListener);
                //Need to request Geofence updates but requestGeofenceUpdates not available!!!!
                //startLeScan(true);
            }else if (match){
                duringRoute = false;
                Toast.makeText(MainActivity.this, "START::"+mytempLocation, Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "END::"+myInput, Toast.LENGTH_SHORT).show();
                chooseRoute(mytempLocation,myInput);//Pass myInput which is end location and mytempLocation which is current location and hence start as parameters
            }

            else{
                texttospeech(repatutterance,"Repeat","");
                Toast.makeText(MainActivity.this, "REPEAT!!!", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            Log.d(TAG, "onPartialResults");

            texttospeech(repatutterance,"Repeat","");
            //introduction();
            //for (int i = 0; i < data.size(); i++)
            //{
             //   Log.d(TAG, "result " + data.get(i));
             //   mystr += data.get(i);
            //}
            //mTextView.setText("results: " + String.valueOf(data.size()));
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            Log.d(TAG, "onEvent " + eventType);
            texttospeech(repatutterance,"Repeat","");
        }
    }

    /**
     * Fire an intent to start the voice recognition activity.
     */
    private void    startVoiceRecognitionActivity()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please speak now...");
        //intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"com.example.salfino.voice_test");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,2000);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS,8000);
        mSR.startListening(intent);

        //startActivityForResult(intent, REQUEST_CODE);//Start an activity and get a result back
        //When user is done, onActivityResult() method is called
    }

//    @Override
//    //INPUT: Request code passed to startActivityForResult(), resultCode is either RESULT_OK or RESULT_CANCELED
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
//        {
//            // Populate the wordsList with the String values the recognition engine thought it heard
//            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//            //mwordsList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, matches));
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    private class DownloadXML extends AsyncTask<String,Void,String> {
        private static final String TAG = "DownloadData";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: parameter is " + s);
            xmlFile = s;
            ParseIndoorAtlas parseIndoorAtlas = new ParseIndoorAtlas();

            parseIndoorAtlas.parseText(xmlFile);
            textData = parseIndoorAtlas.getTextData();

            parseIndoorAtlas.parseConfiguration(xmlFile);
            configData = parseIndoorAtlas.getConfigurationData();

            parseIndoorAtlas.parseRoomWayPoint(xmlFile);
            roomwaypointData = parseIndoorAtlas.getRoomWayPointData();//Retreive both room and waypoint data

            parseIndoorAtlas.parseRoom(xmlFile); //downloaded XML file
            roomData = parseIndoorAtlas.getRoomData();//Get room data only

            parseIndoorAtlas.parseRoute(xmlFile);
            routeData = parseIndoorAtlas.getRouteData();//Get route data

            parseIndoorAtlas.parseWayPoint(xmlFile);
            waypointData = parseIndoorAtlas.getWayPointData();//Get waypoint data only

            parseIndoorAtlas.parseEvent(xmlFile);
            eventData = parseIndoorAtlas.getEventData();//Get events data if any available
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, "doInBackground: starts with " + params[0]);
            int count = params.length;
            String indoorAtlasFeed = "";
            for (int i = 0; i < count; i++) {
                indoorAtlasFeed = downloadXML(params[i]);
            }
            if (indoorAtlasFeed == null) {
                Log.e(TAG, "doInBackground: Error downloading XML data");
            }
            return indoorAtlasFeed;
        }

        private String downloadXML (String urlPath){
            StringBuilder xmlResult = new StringBuilder();

            try{
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "downloadXML: The response code was " + response);
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);

                int charsRead;
                char[] inputBuffer = new char[500];
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

                return xmlResult.toString();

            } catch (MalformedURLException e) {
                Log.e(TAG, "downloadXML: Invalid URL " + e.getMessage());
                Toast.makeText(MainActivity.this,"downloadXML: Invalid URL. Please fix configuration file and relaunch Naviblind" , Toast.LENGTH_SHORT).show();

            } catch (IOException e){
                Log.e(TAG, "downloadXML: IO Exception reading data: " + e.getMessage());
                Toast.makeText(MainActivity.this,"downloadXML: IO Exception reading data. Please fix configuration file and relaunch Naviblind" , Toast.LENGTH_SHORT).show();

            } catch (SecurityException e){
                Log.e(TAG, "downloadXML: Security Exception. Needs Permission! " + e.getMessage());
                Toast.makeText(MainActivity.this,"downloadXML: Security Exception. Needs Permission. Please fix configuration file and relaunch Naviblind" , Toast.LENGTH_SHORT).show();
                //e.printStackTrace();
            }

            return null;
        }

    }

}

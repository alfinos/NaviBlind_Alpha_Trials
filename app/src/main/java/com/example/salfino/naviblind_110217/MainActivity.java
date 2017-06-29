package com.example.salfino.naviblind_110217;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private int[] textCommands = {
            R.string.welcome,
            R.string.main_door,
            R.string.after_main_door,
            R.string.four_steps,
            R.string.narrow_corridor,
            R.string.two_steps,
            R.string.at_two_steps,
            R.string.end_point,
            R.string.response_yes,
            R.string.response_no,
            R.string.yes_or_no,
            R.string.repeat,
            R.string.please_repeat
    };

    private int[] audioCommands = {
            R.raw.after_main_door,
            R.raw.at_two_steps,
            R.raw.end_point,
            R.raw.four_steps,
            R.raw.main_door,
            R.raw.narrow_corridor,
            R.raw.please_repeat,
            R.raw.repeat,
            R.raw.response_no,
            R.raw.response_yes,
            R.raw.two_steps,
            R.raw.welcomemale,
            R.raw.yes_or_no
    };
    //Waypoint geo-coordinates in decimal degrees (DD)
    private static final double GR_OFFICE_LAT = 51.52222145;
    private static final double GR_OFFICE_LON = -0.13049584;
    private static final double START_POSITION_LAT = 51.52209086;
    private static final double START_POSITION_LON = -0.13077546;
    private static final double FOUR_STEPS_LAT = 51.52221143;
    private static final double FOUR_STEPS_LON = -0.13077848;
    private static final double TWO_STEPS_LAT = 51.52228758;
    private static final double TWO_STEPS_LON = -0.13059912;
    private static final double MAIN_DOOR_LAT = 51.52213759;
    private static final double MAIN_DOOR_LON = -0.13069935;
    private static final int REQUEST_CODE = 1234;
    private final int MY_CODE_PERMISSIONS = 1;
    //static final String FASTEST_INTERVAL = "fastestInterval";
    //static final String SHORTEST_DISPLACEMENT = "shortestDisplacement";
    private long DEFAULT_INTERVAL = 500L;
    private float DEFAULT_DISPLACEMENT = 1.5f;
    public IALocationManager mIALocationManager;
    public SpeechRecognizer mSR;
    //private Button mLocationButton;
    //private Button mStopButton;
    private Button mYes;
    private Button mNo;
    //private Button mlaunchFloorPlan;
    private TextView mLogging;
    private TextView mTextView;
    private ScrollView mScrollView;
    private static final String TAG = "IndoorAtlas";
    private long mRequestStartTime;
    private int myFlag = 2;

    private void logText(String msg) {
        double duration = mRequestStartTime != 0
                ? (SystemClock.elapsedRealtime() - mRequestStartTime) / 1e3
                : 0d;
        //mLogging.append(String.format(Locale.UK, "\n[%06.2f]: %s", duration, msg));
        mLogging.setText(String.format(Locale.UK, "\n[%06.2f]:\n %s", duration, msg));
        mLogging.setTextSize(40);
        mLogging.setTextColor(0xFFFF8290);
        mScrollView.smoothScrollBy(0, mLogging.getBottom());
    }

    private void displayText (int textCommandIndex, String voiceCommandURL) {

        mLogging.setText("");
        mLogging.setText(textCommands[textCommandIndex]);
        mLogging.setTextSize(30);
        mLogging.setTextColor(0xFFFF4046);
        mScrollView.smoothScrollBy(0, mLogging.getBottom());
        //MediaPlayer mPlayer = MediaPlayer.create(this,R.raw.welcome);
        try {
            MediaPlayer mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(voiceCommandURL);
            mPlayer.prepare();
            mPlayer.start();

        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Check Wi-Fi connection or audio file missing!!", Toast.LENGTH_LONG).show();
            //MediaPlayer mPlayer = MediaPlayer.create(this,R.raw.welcome);
            //mPlayer.start();
        }
    }

    private void displayTextTwo (int textCommandIndex, int audioCommandIndex) {

        mLogging.setText("");
        mLogging.setText(textCommands[textCommandIndex]);
        mLogging.setTextSize(30);
        mLogging.setTextColor(0xFFFF4046);
        mScrollView.smoothScrollBy(0, mLogging.getBottom());
        //MediaPlayer mPlayer = MediaPlayer.create(this,R.raw.welcome);
        try {
            MediaPlayer mPlayer = MediaPlayer.create(this,audioCommands[audioCommandIndex]);
            mPlayer.start();

        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Check Wi-Fi connection or audio file missing!!", Toast.LENGTH_LONG).show();
            //MediaPlayer mPlayer = MediaPlayer.create(this,R.raw.welcome);
            //mPlayer.start();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(MainActivity.this, "DEBUG::onCreate() callback...", Toast.LENGTH_LONG).show();

        /*if (savedInstanceState != null) {
            mFastestInterval = savedInstanceState.getLong(FASTEST_INTERVAL);
            mShortestDisplacement = savedInstanceState.getFloat(SHORTEST_DISPLACEMENT);
        }*/
        String[] neededPermissions = {
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.RECORD_AUDIO
        };
        ActivityCompat.requestPermissions( this, neededPermissions, MY_CODE_PERMISSIONS);
        setContentView(R.layout.activity_main);
       // mLocationButton = (Button) findViewById(R.id.locationButton);
        //mStopButton = (Button) findViewById(R.id.stopButton);
        //mlaunchFloorPlan = (Button) findViewById(R.id.launchPlan);
        mYes = (Button) findViewById(R.id.locationButton);//IMP:Rename id to a more meaningful name
        mNo = (Button) findViewById(R.id.stopButton);//IMP:Rename id to a more meaningful name
        mLogging = (TextView) findViewById(R.id.mytextView);
        mTextView = (TextView) findViewById(R.id.coordinates);
        mTextView.setTextSize(15);
        mTextView.setTextColor(0xFFFF4046);
        mScrollView = (ScrollView) findViewById(R.id.myscrollView);

        //Create a new instance of IALocationManager using its create() method
        mIALocationManager = IALocationManager.create(this);
        IALocation location = new IALocation.Builder().withLatitude(START_POSITION_LAT)
                                                      .withLongitude(START_POSITION_LON)
                                                      .withAccuracy(75f)
                                                      .withFloorLevel(2).build();
        mIALocationManager.setLocation(location);//Explicitly set the

        IALocationRequest request = IALocationRequest.create();
        request.setFastestInterval(DEFAULT_INTERVAL);//Explicitly set the fastest interval for location updates in milliseconds
        request.setSmallestDisplacement(DEFAULT_DISPLACEMENT);//Set the minimum displacement between location updates in meters

        //Create a new instance of SpeechRecognizer using its createSpeechRecognizer() method
        mSR = SpeechRecognizer.createSpeechRecognizer(this);
        //Set the Speech Listener as the new speechListener defined in inner class below
        mSR.setRecognitionListener(new speechListener());

        mYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFlag = 1;
            }
        });

        mNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFlag = 0;
            }
        });

        //displayText(0,"https://naviblind.000webhostapp.com/welcomemale.mp3");

        /*mLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "getLocation Button pressed!", Toast.LENGTH_LONG).show();
                mRequestStartTime = SystemClock.elapsedRealtime();
                mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mIALocationListener);
            }
        });

        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "stopLocation Button pressed!", Toast.LENGTH_LONG).show();
                mLogging.setText("");
                mIALocationManager.removeLocationUpdates(mIALocationListener);
                mIALocationManager.destroy();
            }
        });*/

       /* mlaunchFloorPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "launch Floor Plan Button pressed!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(MainActivity.this, FloorPlanActivity.class);
                //Intent i = new Intent(MainActivity.this, TestActivity.class);
                startActivity(i);
            }
        });*/

        /*new CountDownTimer(2000, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                displayText(0,"https://iwtcourseworksa.000webhostapp.com/welcomemale.mp3");
            }
        }.start();

        new CountDownTimer(20000, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                displayText(1,"https://iwtcourseworksa.000webhostapp.com/main_door.mp3");
            }
        }.start();*/

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_CODE_PERMISSIONS: {// If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // permission was granted

                    Toast.makeText(MainActivity.this, "Permission granted for coarse location and Wi-Fi status", Toast.LENGTH_SHORT).show();
                } else {// permission denied

                    Toast.makeText(MainActivity.this, "Permission denied for coarse location and Wi-Fi status", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }

    }

    public IALocationListener mIALocationListener = new IALocationListener() {
      //Implement an IALocationListener interface and override its onLocationChanged() callback method
      @Override
      public void onLocationChanged(IALocation iaLocation) {
          //Toast.makeText(MainActivity.this, "Location Changing...", Toast.LENGTH_SHORT).show();
          mTextView.setText(String.format(Locale.UK, "Latitude: %.8f,\nLongitude: %.8f,\nAccuracy: %.8f,\nCertainty: %.8f,\nLevel: %d",
                  iaLocation.getLatitude(), iaLocation.getLongitude(),iaLocation.getAccuracy(),iaLocation.getFloorCertainty(),
                  iaLocation.getFloorLevel()));
          //mTextView.setText(String.valueOf(iaLocation.getLatitude() + ", " + iaLocation.getLongitude()));
          //Location updates being delivered here
          mTextView.setTextSize(15);
          //Below shows possible way of implementing voice and text triggers
          Haversine havObject = new Haversine(); //Create the Haversine object
          double currentDistance_SP = 1000*(havObject.distance(iaLocation.getLatitude(),iaLocation.getLongitude(),START_POSITION_LAT,START_POSITION_LON));
          double currentDistance_4S = 1000*(havObject.distance(iaLocation.getLatitude(),iaLocation.getLongitude(),FOUR_STEPS_LAT,FOUR_STEPS_LON));
          double currentDistance_2S = 1000*(havObject.distance(iaLocation.getLatitude(),iaLocation.getLongitude(),TWO_STEPS_LAT,TWO_STEPS_LON));
          double currentDistance_GR = 1000*(havObject.distance(iaLocation.getLatitude(),iaLocation.getLongitude(),GR_OFFICE_LAT,GR_OFFICE_LON));
          double currentDistance_MD = 1000*(havObject.distance(iaLocation.getLatitude(),iaLocation.getLongitude(),MAIN_DOOR_LAT,MAIN_DOOR_LON));

          //mLogging.setText("");
          mLogging.setText(String.valueOf(" SP: " + currentDistance_SP + ",\n MD: " + currentDistance_MD + ",\n 4S: " + currentDistance_4S + ",\n 2S: " + currentDistance_2S + ",\n GR: " + currentDistance_GR));
          mLogging.setTextSize(20);
          mLogging.setTextColor(0xFFFF4046);
          mScrollView.smoothScrollBy(0, mLogging.getBottom());

          if (currentDistance_SP <= 4) {
             // displayText(0,"https://naviblind.000webhostapp.com/welcomemale.mp3");
              //mIALocationManager.removeLocationUpdates(mIALocationListener);
              displayTextTwo(0,11);
              new CountDownTimer(10000, 1000){//10 second count down timer
                  @Override
                  public void onTick(long millisUntilFinished) {
                      Toast.makeText(MainActivity.this, "Timer On...", Toast.LENGTH_SHORT).show();
                  }

                  @Override
                  public void onFinish() {
                      //mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mIALocationListener);
                  }
              }.start();

          } else if (currentDistance_MD <=4){
              //displayText(2,"https://naviblind.000webhostapp.com/after_main_door.mp3");
              displayTextTwo(2,0);
          } else if (currentDistance_4S <=4){
              //displayText(4,"https://naviblind.000webhostapp.com/narrow_corridor.mp3");
              displayTextTwo(4,5);
          } else if (currentDistance_2S <=4) {
              //displayText(6,"https://naviblind.000webhostapp.com/at_two_steps.mp3");
              displayTextTwo(6,1);
          } else if (currentDistance_GR <=4) {
              //displayText(7,"https://naviblind.000webhostapp.com/end_point.mp3");
              displayTextTwo(7,2);
          }
      }

      @Override
      public void onStatusChanged(String provider, int status, Bundle bundle) {
          switch (status) {
              case IALocationManager.STATUS_CALIBRATION_CHANGED:
                  String quality = "unknown";
                  switch (bundle.getInt("quality")) {
                      case IALocationManager.CALIBRATION_POOR:
                          quality = "Poor";
                          break;
                      case IALocationManager.CALIBRATION_GOOD:
                          quality = "Good";
                          break;
                      case IALocationManager.CALIBRATION_EXCELLENT:
                          quality = "Excellent";
                          break;
                  }
                  logText("Calibration Quality: " + quality + " Status Code: " + status);
                  break;
              case IALocationManager.STATUS_AVAILABLE:
                  logText("onStatusChanged: Available" + " Status Code: " + status);
                  break;
              case IALocationManager.STATUS_LIMITED:
                  logText("onStatusChanged: Limited");
                  break;
              case IALocationManager.STATUS_OUT_OF_SERVICE:
                  logText("onStatusChanged: Out of service");
                  break;
              case IALocationManager.STATUS_TEMPORARILY_UNAVAILABLE:
                  logText("onStatusChanged: Temporarily unavailable");
          }

      }
  };

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(MainActivity.this, "DEBUG::onResume() callback...", Toast.LENGTH_LONG).show();
        mRequestStartTime = SystemClock.elapsedRealtime();
        mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mIALocationListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(MainActivity.this, "DEBUG::onPause() callback...", Toast.LENGTH_LONG).show();
        mIALocationManager.removeLocationUpdates(mIALocationListener);
    }

    @Override
    protected void onDestroy() {
        Toast.makeText(MainActivity.this, "DEBUG::onDestroy() callback...", Toast.LENGTH_LONG).show();
        mIALocationManager.destroy();
        super.onDestroy();
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
                Toast.makeText(MainActivity.this, "launch Floor Plan debug.", Toast.LENGTH_LONG).show();
                Intent i = new Intent(MainActivity.this, FloorPlanActivity.class);
                //Intent i = new Intent(MainActivity.this, TestActivity.class);
                startActivity(i);
                return true;
            /*case R.id.menu_item_debug:
                Toast.makeText(MainActivity.this, "launch Debug Session...", Toast.LENGTH_LONG).show();
                Intent k = new Intent(MainActivity.this, DebugActivity.class);
                startActivity(k);
                return true;*/
            case R.id.menu_item_test_speech:
                Toast.makeText(MainActivity.this, "launch Speech Recognition debug.", Toast.LENGTH_LONG).show();
                startVoiceRecognitionActivity();//Just temporary action
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*@Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong(FASTEST_INTERVAL, mFastestInterval);
        savedInstanceState.putFloat(SHORTEST_DISPLACEMENT, mShortestDisplacement);
        super.onSaveInstanceState(savedInstanceState);
    }*/

    class speechListener implements RecognitionListener {

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
            Toast.makeText(MainActivity.this, "DEBUG::Recognition Listener onError()", Toast.LENGTH_LONG).show();
            mLogging.setText("");
            mLogging.setText("Error Number " + error + " occurred.");
            mLogging.setTextSize(30);
            mLogging.setTextColor(0xFFFF4046);
            mScrollView.smoothScrollBy(0, mLogging.getBottom());

        }

        @Override
        public void onResults(Bundle results) {
            Log.d(TAG, "onResults " + results);

            Toast.makeText(MainActivity.this, "DEBUG::Recognition Listener onResult()", Toast.LENGTH_LONG).show();
            //String mystr = new String();

            ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            mLogging.setText("");
            mLogging.setText("You said : " + data.get(0));
            mLogging.setTextSize(30);
            mLogging.setTextColor(0xFFFF4046);
            mScrollView.smoothScrollBy(0, mLogging.getBottom());
            //for (int i = 0; i < data.size(); i++)
            //{
            //   Log.d(TAG, "result " + data.get(i));
            //  mystr += data.get(i);
            //}
            // mTextView.setText("results: " + String.valueOf(data.size()));

        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            Log.d(TAG, "onPartialResults");

            Toast.makeText(MainActivity.this, "DEBUG::Recognition Listener onPartialResults()", Toast.LENGTH_LONG).show();
            String mystr = new String();
            Log.d(TAG, "onResults " + partialResults);

            ArrayList data = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            mLogging.setText("");
            mLogging.setText("You said : " + data.get(0));
            mLogging.setTextSize(30);
            mLogging.setTextColor(0xFFFF4046);
            mScrollView.smoothScrollBy(0, mLogging.getBottom());
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

        }
    }

    /**
     * Fire an intent to start the voice recognition activity.
     */
    private void startVoiceRecognitionActivity()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please speak now...");
        //intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"com.example.salfino.voice_test");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
        Toast.makeText(MainActivity.this, "DEBUG::Launch startListening()", Toast.LENGTH_LONG).show();
        mSR.startListening(intent);

        //startActivityForResult(intent, REQUEST_CODE);//Start an activity and get a result back
        //When user is done, onActivityResult() method is called
    }

    @Override
    //INPUT: Request code passed to startActivityForResult(), resultCode is either RESULT_OK or RESULT_CANCELED
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            // Populate the wordsList with the String values the recognition engine thought it heard
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //mwordsList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, matches));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}

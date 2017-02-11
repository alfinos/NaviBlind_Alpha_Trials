package com.example.salfino.naviblind_110217;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final int CODE_PERMISSIONS = 1;
    //static final String FASTEST_INTERVAL = "fastestInterval";
    //static final String SHORTEST_DISPLACEMENT = "shortestDisplacement";
    public IALocationManager mIALocationManager;
    private Button mLocationButton;
    private Button mStopButton;
    private TextView mLogging;
    private ScrollView mScrollView;
    private static final String TAG = "IndoorAtlas";
    private long mRequestStartTime;
   // private long mFastestInterval = -1L;
   // private float mShortestDisplacement = -1f;

    /*public void getLocation (View v){
        Toast.makeText(MainActivity.this, "getLocation Button pressed!", Toast.LENGTH_SHORT).show();
        mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mIALocationListener);
    }

    public void stopLocation (View v){
        Toast.makeText(MainActivity.this, "stopLocation Button pressed!", Toast.LENGTH_SHORT).show();
        mIALocationManager.removeLocationUpdates(mIALocationListener);
    }*/

    private void logText(String msg) {
        double duration = mRequestStartTime != 0
                ? (SystemClock.elapsedRealtime() - mRequestStartTime) / 1e3
                : 0d;
        //mLogging.append(String.format(Locale.UK, "\n[%06.2f]: %s", duration, msg));
        mLogging.setText(String.format(Locale.UK, "\n[%06.2f]: %s", duration, msg));
        mLogging.setTextSize(40);
        mLogging.setTextColor(0xFFFF4046);
        mScrollView.smoothScrollBy(0, mLogging.getBottom());
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*if (savedInstanceState != null) {
            mFastestInterval = savedInstanceState.getLong(FASTEST_INTERVAL);
            mShortestDisplacement = savedInstanceState.getFloat(SHORTEST_DISPLACEMENT);
        }*/
        String[] neededPermissions = {
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        ActivityCompat.requestPermissions( this, neededPermissions, CODE_PERMISSIONS );
        setContentView(R.layout.activity_main);
        mLocationButton = (Button) findViewById(R.id.locationButton);
        mStopButton = (Button) findViewById(R.id.stopButton);
        mLogging = (TextView) findViewById(R.id.mytextView);
        mScrollView = (ScrollView) findViewById(R.id.myscrollView);

        mIALocationManager = IALocationManager.create(this);

        mLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "getLocation Button pressed!", Toast.LENGTH_LONG).show();
                mIALocationManager.removeLocationUpdates(mIALocationListener);
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
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {// If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // permission was granted

                    Toast.makeText(MainActivity.this, "Permission granted for coarse location and Wi-Fi status", Toast.LENGTH_SHORT).show();
                } else {// permission denied

                    Toast.makeText(MainActivity.this, "Permission denied for coarse location and Wo-Fi status", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }

    }
  /*  @Override
    public void onLocationChanged(IALocation iaLocation) {
        Log.d(TAG, "Latitude: " + iaLocation.getLatitude());
        Log.d(TAG, "Longitude: " +iaLocation.getLongitude());
        Toast.makeText(MainActivity.this, "Latitude: " + iaLocation.getLatitude() + "Longitude: " + iaLocation.getLongitude(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }*/

    public IALocationListener mIALocationListener = new IALocationListener() {
      @Override
      public void onLocationChanged(IALocation iaLocation) {
          logText(String.format(Locale.UK, "Latitude: %f, Longitude: %f",
                  iaLocation.getLatitude(), iaLocation.getLongitude()));
          Toast.makeText(MainActivity.this, "Latitude: " + iaLocation.getLatitude() + "Longitude: " + iaLocation.getLongitude(), Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onStatusChanged(String s, int i, Bundle bundle) {
          switch (i) {
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
                  logText("Calibration Quality: " + quality);
                  break;
              case IALocationManager.STATUS_AVAILABLE:
                  logText("onStatusChanged: Available");
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
        //mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mIALocationListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
       // mIALocationManager.removeLocationUpdates(mIALocationListener);
    }

    @Override
    protected void onDestroy() {
        mIALocationManager.destroy();
        super.onDestroy();
    }

    /*@Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong(FASTEST_INTERVAL, mFastestInterval);
        savedInstanceState.putFloat(SHORTEST_DISPLACEMENT, mShortestDisplacement);
        super.onSaveInstanceState(savedInstanceState);
    }*/

}

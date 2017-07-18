package com.example.salfino.naviblind_110217;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;
import com.indooratlas.android.sdk.IARegion;
import com.indooratlas.android.sdk.resources.IAFloorPlan;
import com.indooratlas.android.sdk.resources.IALatLng;
import com.indooratlas.android.sdk.resources.IAResourceManager;
import com.indooratlas.android.sdk.resources.IAResult;
import com.indooratlas.android.sdk.resources.IAResultCallback;
import com.indooratlas.android.sdk.resources.IATask;

import java.io.File;

public class FloorPlanActivity extends AppCompatActivity {

    private static final String TAG = "IndoorAtlas";
    //Waypoint geo-coordinates in decimal degrees (DD)
    private static final double GR_OFFICE_LAT = 51.52222145;
    private static final double GR_OFFICE_LON = -0.13049584;
    private static final double START_POSITION_LAT = 51.52231720;
    private static final double START_POSITION_LON = -0.13089649;
    private static final double FOUR_STEPS_LAT = 51.52221143;
    private static final double FOUR_STEPS_LON = -0.13077848;
    private static final double TWO_STEPS_LAT = 51.52228758;
    private static final double TWO_STEPS_LON = -0.13059912;
    private static final double MAIN_DOOR_LAT = 51.52213759;
    private static final double MAIN_DOOR_LON = -0.13069935;
    private long DEFAULT_INTERVAL = 100L;//milliseconds
    private float DEFAULT_DISPLACEMENT = 0.2f;//meters

    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1;
    private static final float dotRadius = 1.0f; // blue dot radius in meters

    private IALocationManager mIALocationManagerSecond;
    private IAResourceManager mFloorPlanManager;
    private IATask<IAFloorPlan> mPendingAsyncResult;
    private IAFloorPlan mFloorPlan;
    private DotImage mImageView;
    private long mDownloadId;
    private DownloadManager mDownloadManager;

    private IALocationListener mIALocationListenerSecond = new IALocationListener() {
        @Override
        public void onLocationChanged(IALocation iaLocation) {
            Log.d(TAG, "location is: " + iaLocation.getLatitude() + "," + iaLocation.getLongitude());
            if (mImageView != null && mImageView.isReady()) {
                IALatLng latLng = new IALatLng(iaLocation.getLatitude(), iaLocation.getLongitude());
                PointF point = mFloorPlan.coordinateToPoint(latLng);
                mImageView.setDotCenter(point);
                mImageView.postInvalidate();
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }
    };

    private IARegion.Listener mRegionListener = new IARegion.Listener() {
        @Override
        public void onEnterRegion(IARegion iaRegion) {
            if (iaRegion.getType() == IARegion.TYPE_FLOOR_PLAN) {
                String id = iaRegion.getId();
                Log.d(TAG, "floorPlan changed to " + id);
                IALocation location = new IALocation.Builder()
                        .withFloorLevel(2).build();
                mIALocationManagerSecond.setLocation(location);//Explicitly set floor level to 2
                Toast.makeText(FloorPlanActivity.this, id, Toast.LENGTH_SHORT).show();
                fetchFloorPlan(id);
            }
        }

        @Override
        public void onExitRegion(IARegion iaRegion) {
            // leaving a previously entered region
            if (iaRegion.getType() == IARegion.TYPE_FLOOR_PLAN) {
                String id = iaRegion.getId();
                Log.d(TAG, "floorPlan changed to " + id);
                IALocation location = new IALocation.Builder()
                        .withFloorLevel(2).build();
                mIALocationManagerSecond.setLocation(location);//Explicitly set floor level to 2
                Toast.makeText(FloorPlanActivity.this, id, Toast.LENGTH_SHORT).show();
                fetchFloorPlan(id);
            }

        }
    };

    private void showFloorPlanImage(String filePath) {
        Log.w(TAG, "showFloorPlanImage: " + filePath);
        mImageView.setRadius(mFloorPlan.getMetersToPixels() * dotRadius);
        mImageView.setImage(ImageSource.uri(filePath));
    }

    private void cancelPendingNetworkCalls() {
        if (mPendingAsyncResult != null && !mPendingAsyncResult.isCancelled()) {
            mPendingAsyncResult.cancel();
        }
    }

    /**
     * Fetches floor plan data from IndoorAtlas server. Source Indoor Atlas SDK
     */
    private void fetchFloorPlan(String id) {
        cancelPendingNetworkCalls();
        final IATask<IAFloorPlan> asyncResult = mFloorPlanManager.fetchFloorPlanWithId(id);
        mPendingAsyncResult = asyncResult;
        if (mPendingAsyncResult != null) {
            mPendingAsyncResult.setCallback(new IAResultCallback<IAFloorPlan>() {
                @Override
                public void onResult(IAResult<IAFloorPlan> result) {
                    Log.d(TAG, "fetch floor plan result:" + result);
                    if (result.isSuccess() && result.getResult() != null) {
                        mFloorPlan = result.getResult();
                        String fileName = mFloorPlan.getId() + ".img";
                        String filePath = Environment.getExternalStorageDirectory() + "/"
                                + Environment.DIRECTORY_DOWNLOADS + "/" + fileName;
                        File file = new File(filePath);
                        if (!file.exists()) {
                            DownloadManager.Request request =
                                    new DownloadManager.Request(Uri.parse(mFloorPlan.getUrl()));
                            request.setDescription("IndoorAtlas floor plan");
                            request.setTitle("Floor plan");
                            // requires android 3.2 or later to compile
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                request.allowScanningByMediaScanner();
                                request.setNotificationVisibility(DownloadManager.
                                        Request.VISIBILITY_HIDDEN);
                            }
                            request.setDestinationInExternalPublicDir(Environment.
                                    DIRECTORY_DOWNLOADS, fileName);

                            mDownloadId = mDownloadManager.enqueue(request);
                        } else {
                            showFloorPlanImage(filePath);
                        }
                    } else {
                        // do something with error
                        if (!asyncResult.isCancelled()) {
                            Toast.makeText(FloorPlanActivity.this,
                                    (result.getError() != null
                                            ? "error loading floor plan: " + result.getError()
                                            : "access to floor plan denied"), Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                }
            }, Looper.getMainLooper()); // deliver callbacks in main thread
        }
    }

    /**
     * Methods for fetching floor plan data and bitmap image.
     * Method {@link #fetchFloorPlan(String id)} fetches floor plan data including URL to bitmap
     */

     /*  Broadcast receiver for floor plan image download */
    private BroadcastReceiver onComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0L);
            if (id != mDownloadId) {
                Log.w(TAG, "Ignore unrelated download");
                return;
            }
            Log.w(TAG, "Image download completed");
            Bundle extras = intent.getExtras();
            DownloadManager.Query q = new DownloadManager.Query();
            q.setFilterById(extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID));
            Cursor c = mDownloadManager.query(q);

            if (c.moveToFirst()) {
                int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    // process download
                    String filePath = c.getString(c.getColumnIndex(
                            DownloadManager.COLUMN_LOCAL_FILENAME));
                    showFloorPlanImage(filePath);
                }
            }
            c.close();
        }
    };

    private void ensurePermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_plan);

        findViewById(android.R.id.content).setKeepScreenOn(true);

        mImageView = (DotImage) findViewById(R.id.imageView);

        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        mIALocationManagerSecond = IALocationManager.create(this);
        mFloorPlanManager = IAResourceManager.create(this);

        IALocation location = new IALocation.Builder().withLatitude(START_POSITION_LAT)
                .withLongitude(START_POSITION_LON)
                .withAccuracy(75f)
                .withFloorLevel(2).build();
        mIALocationManagerSecond.setLocation(location);//Explicitly set the the initial fix as specified above

        IALocationRequest request = IALocationRequest.create();
        request.setPriority(IALocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setFastestInterval(DEFAULT_INTERVAL);//Explicitly set the fastest interval for location updates in milliseconds
        request.setSmallestDisplacement(DEFAULT_DISPLACEMENT);//Set the minimum displacement between location updates in meters


        /* optional setup of floor plan id
           if setLocation is not called, then location manager tries to find
           location automatically */
        /*final String floorPlanId = "";
        if (!TextUtils.isEmpty(floorPlanId)) {
            final IALocation location = IALocation.from(IARegion.floorPlan(floorPlanId));
            mIALocationManagerSecond.setLocation(location);
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        ensurePermissions();
        // starts receiving location updates
        mIALocationManagerSecond.requestLocationUpdates(IALocationRequest.create(), mIALocationListenerSecond);
        mIALocationManagerSecond.registerRegionListener(mRegionListener);
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    @Override
    protected void onPause() {
        super.onPause();
        mIALocationManagerSecond.removeLocationUpdates(mIALocationListenerSecond);
        mIALocationManagerSecond.unregisterRegionListener(mRegionListener);
        unregisterReceiver(onComplete);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIALocationManagerSecond.destroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Refer to Indoor Atlas sample code in case this is required later
    }
}



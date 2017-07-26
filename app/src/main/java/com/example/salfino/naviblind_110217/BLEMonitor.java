package com.example.salfino.naviblind_110217;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class BLEMonitor extends AppCompatActivity {

    public TextView mTest;
    public TextView mTest2;
    public TextView mTest3;
    public final static int REQUEST_ENABLE_BT = 1;
    public BluetoothAdapter mBTAdapter;
    BluetoothLeScanner scanner;
    ScanSettings scanSettings;
    public boolean mScanning;
    String dName = "";
    String macAddress = "";
    Integer rssi = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mTest = (TextView) findViewById(R.id.textView);
        mTest.setTextSize(20);
        mTest.setTextColor(0xFFFF4046);

        mTest2 = (TextView) findViewById(R.id.textViewtwo);
        mTest2.setTextSize(20);
        mTest2.setTextColor(0xFFFF4044);

        mTest3 = (TextView) findViewById(R.id.textViewthree);
        mTest3.setTextSize(20);
        mTest3.setTextColor(0xFFFF4044);

        initialiseBLE();
        startLeScan(true);
        Toast.makeText(BLEMonitor.this, "SCAN STARTED...", Toast.LENGTH_LONG).show();
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

        if (mBTAdapter == null || !mBTAdapter.isEnabled()) {//Enable Bluetooth
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);//Locally defined integer greater than zero
        }
        //Create the scan settings
        ScanSettings.Builder scanSettingsBuilder = new ScanSettings.Builder();
        //Set scan latency mode. Lower latency, faster device detection/more battery and resources consumption
        scanSettingsBuilder.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);
        //Wrap settings together and save on a settings var (declared globally).
        scanSettings = scanSettingsBuilder.build();
        //Get the BLE scanner from the BT adapter (var declared globally)
        scanner = mBTAdapter.getBluetoothLeScanner();
    }
    private void startLeScan(boolean enable) {
        if (enable) {
            //********************
            //START THE BLE SCAN
            //********************
            //Scanning parameters FILTER / SETTINGS / RESULT CALLBACK. Filter are used to define a particular
            //device to scan for. The Callback is defined above as a method.
            mScanning = true;
            scanner.startScan(null, scanSettings,mScanCallback);
            mTest.setText("Scanning for BLE Devices");
            mTest2.setText("Scanning for BLE Devices");
            mTest3.setText("Scanning for BLE Devices");
        }else{
            //Stop scan
            mScanning = false;
            scanner.stopScan(mScanCallback);
            mTest.setText("Stopped Scanning...");
            mTest2.setText("Stopped Scanning...");
            mTest3.setText("Stopped Scanning...");
        }
    }

    private double getDistance(double rssi, String location) {//RSSI (dBm) = -10n log10(d) + A and n = 2 for free space and A is average RSSI at 1m
        double A = -60.0;
        if (location.equals("GC")){
            A = -60.0;// average RSSI for beacon installed in George Roussos office
        } else if (location.equals("SR")) {
            A =  -50.0; // average RSSI for beacon isntalled in Staff Room, next to lifts
        } else if (location.equals("SG")) {
            A = -55.0; // average RSSI for beacon isntalled in Systems Group Area
        }
        return Math.pow(10.0,((rssi-(A))/-25.0));//-60dBm is average RSSI at 1m distance i.e. A
    }

    //Finding BLE Devices
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            String advertisingString = byteArrayToHex(result.getScanRecord().getBytes());
            rssi = result.getRssi();
            dName = result.getDevice().getName();
            if (dName != null) {
                dName = (result.getDevice().getName()).trim();
            }
            macAddress = result.getDevice().getAddress();
            if (macAddress != null) {
                macAddress = (result.getDevice().getAddress()).trim();
            }

            if (macAddress != null && macAddress.equals("F4:46:EA:8F:C2:2D")) {
                mTest.setText(String.format(Locale.UK, "Position: %s,\nRSSI: %d, \nMeters: %f",
                        "266 Beacon", result.getRssi(), getDistance(result.getRssi(), "GC")));
            } else if (macAddress != null && macAddress.equals("C3:4E:E7:D1:2E:3A")) {
                mTest2.setText(String.format(Locale.UK, "Position: %s,\nRSSI: %d, \nMeters: %f",
                        "SR Beacon", result.getRssi(), getDistance(result.getRssi(), "SR")));
            } else if (macAddress != null && macAddress.equals("C7:96:98:06:4C:64")) {
                mTest3.setText(String.format(Locale.UK, "Position: %s,\nRSSI: %d, \nMeters: %f",
                        "SG Beacon", result.getRssi(), getDistance(result.getRssi(), "SG")));

            } else {
                mTest.setText("Can't find End Point Beacon!!!");
                mTest2.setText("Can't find Lifts Area Beacon!!!");
                mTest3.setText("Can't find Systems Group Beacon!!!");
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

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        startLeScan(false);
        Toast.makeText(BLEMonitor.this, "SCAN STOPPED...", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

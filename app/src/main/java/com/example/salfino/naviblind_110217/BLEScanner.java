package com.example.salfino.naviblind_110217;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.util.List;

public class BLEScanner extends Service {
    int mStartMode;
    public BluetoothAdapter mBTAdapter;
    BluetoothGatt mBluetoothGatt;
    BluetoothLeScanner scanner;
    ScanSettings scanSettings;
    public boolean mScanning;
    public Handler mHandler;
    public static final long SCAN_PERIOD = 10000;
    String dName = "";
    String macAddress = "";
    Integer rssi = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return mStartMode;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initialiseBLE();
        startLeScan(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        startLeScan(false);
        Toast.makeText(BLEScanner.this, "SCAN STOPPED...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
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
    private void startLeScan(boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
           /* mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    scanner.stopScan(mScanCallback);
                    mTest.setText("Stopped Scanning...");
                }
            }, SCAN_PERIOD);*/
            //********************
            //START THE BLE SCAN
            //********************
            //Scanning parameters FILTER / SETTINGS / RESULT CALLBACK. Filter are used to define a particular
            //device to scan for. The Callback is defined above as a method.
            mScanning = true;
            scanner.startScan(null, scanSettings, mScanCallback);
            Toast.makeText(BLEScanner.this, "SCANNING FOR BLE DEVICES...", Toast.LENGTH_SHORT).show();
        }else{
            //Stop scan
            mScanning = false;
            scanner.stopScan(mScanCallback);
            Toast.makeText(BLEScanner.this, "SCANNING STOPPED...", Toast.LENGTH_SHORT).show();
        }
    }

    //Finding BLE Devices
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            String advertisingString = byteArrayToHex(result.getScanRecord().getBytes());
            rssi = result.getRssi();
            dName = result.getDevice().getName();
            if (dName != null){
                dName = (result.getDevice().getName()).trim();
            }
            macAddress = result.getDevice().getAddress();
            if (macAddress != null){
                macAddress = (result.getDevice().getAddress()).trim();
            }

            /*mTest.setText(String.format(Locale.UK, "RSSI: %d, \nMAC Address: %s, \nDevice Name: %s",
                    result.getRssi(), result.getDevice().getAddress(), result.getDevice().getName()));*/

            if (macAddress != null && macAddress.equals("F4:46:EA:8F:C2:2D")){
                Toast.makeText(BLEScanner.this, "RSSI::" + result.getRssi() , Toast.LENGTH_LONG).show();
                //mTest.setText(String.format(Locale.UK, "RSSI: %d, \nMAC Address: %s, \nDevice Name: %s",
                //        result.getRssi(), result.getDevice().getAddress(), result.getDevice().getName()));
            }else{Toast.makeText(BLEScanner.this, "Can't find beacon...", Toast.LENGTH_SHORT).show();;}

            /*if (dName != null && macAddress != null) {
                if (dName.equals("iBKS105") && macAddress.equals("F4:46:EA:8F:C2:2D")) {
                    mTest.setText(String.format(Locale.UK, "RSSI: %d, \nAdvertisment: %s, \nMAC Address: %s, \nDevice Name: %s",
                        result.getRssi(), advertisingString, result.getDevice().getAddress(), result.getDevice().getName()));
                } else {
                     mTest.setText("Scanning for GR Waypoint Device...");
            }
            } else {
                mTest.setText("Reading NULL...");
            }*/

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

    //Connecting to a GATT Server on the BLE device
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
        }
    };
}

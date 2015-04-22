package com.tsd.citybug;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity implements IBroadcastReceiverClient {
    private LocationManager mLocationManager;
    private Location mLocation;
    private Button[] mButtons = new Button[6];
    private TextView mTextViewStatus, mTextViewLatLng;
    private CassandraHelper mCassandraHelper;
    private WifiBroadcastReceiver mWifiBroadcastReceiver = new WifiBroadcastReceiver(this, this);
    private LogManager mLogManager = new LogManager(this, "entries-log");
    private String mMacAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        final LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                mLocation = location;
            }
            public void onProviderDisabled(String arg0) {
                // TODO Auto-generated method stub
            }
            public void onProviderEnabled(String arg0) {
                // TODO Auto-generated method stub
            }
            public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
                // TODO Auto-generated method stub
            }
        };
        try {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
        } catch (Exception e) {
            mTextViewLatLng.setText(e.getMessage());
        }
        mButtons[0] = (Button) findViewById(R.id.Button1);
        mButtons[1] = (Button) findViewById(R.id.Button2);
        mButtons[2] = (Button) findViewById(R.id.Button3);
        mButtons[3] = (Button) findViewById(R.id.Button4);
        mButtons[4] = (Button) findViewById(R.id.Button5);
        mButtons[5] = (Button) findViewById(R.id.Button6);
        mTextViewLatLng = (TextView) findViewById(R.id.TextView_LatLng);
        mTextViewStatus = (TextView) findViewById(R.id.TextView_Status);

        for (int i = 0; i < mButtons.length; i++) {
            final int event = i + 1;
            mButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    triggerEvent(event);
                }
            });
        }

        mMacAddress = getMacAddress();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getMacAddress() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        return wInfo.getMacAddress();
    }

    private void triggerEvent(int event) {
        mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (mLocation != null) {
            double lat = mLocation.getLatitude();
            double lng = mLocation.getLongitude();
            double height = 0;
            String mac = mMacAddress;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sdf.format(new Date());
            saveOrSendData(mac, lat, lng, height, time, event);
        } else {
            notify("Unable to get latitude & longitude.");
        }
    }
    
    private void saveOrSendData(String mac, double lat, double lng, double height, String time, int event) {
        if (mCassandraHelper.connect()) {
            if (mCassandraHelper.send(mac, lat, lng, height, time, event)) {
                notify("Data sent successfully.");
                return;
            } else {
                notify("Unable to send data to server. The entry will now be stored in the log.");
            }
        }
        mLogManager.save(mac, lat, lng, height, time, event);
    }

    private void notify(String message){
        mTextViewStatus.setText(message);
    }

    private void sendLogToServer() {
        String mac, time;
        double lat, lng, height;
        int event;
        Entry e = null;

        for (int i = 0; i < mLogManager.getEntries().size(); i++) {
            e = mLogManager.getEntries().get(i);
            mac = e.getMacAddress();
            lat = e.getLatitude();
            lng = e.getLongitude();
            height = e.getHeight();
            time = e.getTime();
            event = e.getEvent();
            if (mCassandraHelper.send(mac, lat, lng, height, time, event)) {
                mLogManager.getEntries().remove(i);
                i--;
            } else {
                mLogManager.save();
            }
        }
    }

    @Override
    public void wifiEnabled(){
        notify("Wifi enabled.");
    }

    @Override
    public void wifiDisabled(){
        notify("Wifi disabled");
    }

    @Override
    public void connectingToNetwork(){
        notify("Connecting to network...");
    }

    @Override
    public void networkUnavailable(){
        notify("Network unavailable.");
    }

    @Override
    public void networkAvailable(){
        notify("Connecting to server...");
        if (mCassandraHelper.connect()) {
            notify("Connected to server. Sending data...");
            sendLogToServer();
        }
        else
            notify("Unable to connect to server.");
    }

}

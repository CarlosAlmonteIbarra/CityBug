package com.tsd.citybug;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {
    private LocationManager mLocationManager;
    private Location mLocation;
    private Button[] mButtons = new Button[6];
    private TextView mTextViewStatus, mTextViewLatLng;

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
            final int event = i;
            mButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    triggerEvent(event);
                }
            });
        }
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

    private void triggerEvent(int event) {
        mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (mLocation != null) {
            double lat = mLocation.getLatitude();
            double lng = mLocation.getLongitude();
            String value = String.valueOf(lat) + ", " + String.valueOf(lng);
            mTextViewLatLng.setText(value);
        } else {
            mTextViewLatLng.setText("NONE");
        }

        switch (event) {
            case 1:
            {
                break;
            }
            case 2:
            {
                break;
            }
            case 3:
            {
                break;
            }
            case 4:
            {
                break;
            }
            case 5:
            {
                break;
            }
            case 6:
            {
                break;
            }
        }
    }
    
}

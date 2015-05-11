package com.tsd.citybug;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;

public class WifiBroadcastReceiver extends BroadcastReceiver {
	private IBroadcastReceiverClient mClient;
    private Context mContext;
    private final int TIMEOUT = 10; // seconds

    public WifiBroadcastReceiver(IBroadcastReceiverClient client, Context ctx){
        mClient = client;
        mContext = ctx;
    }

    @Override
    public void onReceive(Context context, Intent intent){
        final String action = intent.getAction();

        if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
            if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)){
                mClient.wifiEnabled();
                mClient.connectingToNetwork();
                int timeout = TIMEOUT * 1000, result = 0;
                while (((result = isInternetOn(mContext)) != 2) && timeout > 0){
                    timeout--;
                }
                if (result == 0)
                    mClient.networkUnavailable();
                else if (result == 2)
                    mClient.networkAvailable();
                    
            } else {
                mClient.wifiDisabled();
            }
        }
    }

    public int isInternetOn(Context ctx) {
        ConnectivityManager Connect_Manager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        State connected = State.CONNECTED;
        State connecting = State.CONNECTING;
        State disconnected = State.DISCONNECTED;

        State info0 = Connect_Manager.getNetworkInfo(0).getState();
        State info1 = Connect_Manager.getNetworkInfo(1).getState();

        if (info0 == connected || info1 == connected)
            return 2;
        if (info0 == connecting || info1 == connected)
                return 1;
        else if (info0 == disconnected || info1 == disconnected)
            return 0;
        
        return 0;
    }
}

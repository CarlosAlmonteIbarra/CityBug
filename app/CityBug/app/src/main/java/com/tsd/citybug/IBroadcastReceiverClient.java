package com.tsd.citybug;

public interface IBroadcastReceiverClient {
	void wifiEnabled();
    void wifiDisabled();
    void connectingToNetwork();
    void networkUnavailable();
    void networkAvailable();
}

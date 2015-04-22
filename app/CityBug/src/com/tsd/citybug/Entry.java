package com.tsd.citybug;

public class Entry {
	private String mMacAddress;
	private double mLatitude, mLongitude, mHeight;
	private String mTime;
	private int mEvent;
	
	public String getMacAddress() { return mMacAddress; }
	public double getLatitude() { return mLatitude; }
	public double getLongitude() { return mLongitude; }
	public double getHeight() { return mHeight; }
	public String getTime() { return mTime; }
	public int getEvent() { return mEvent; }

	public Entry(String mac, double lat, double lng, double height, String time, int event) {
		mMacAddress = mac;
		mLatitude = lat;
		mLongitude = lng;
		mHeight = height;
		mTime = time;
		mEvent = event;
	}
	
}

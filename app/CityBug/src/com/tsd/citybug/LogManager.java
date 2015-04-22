package com.tsd.citybug;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class LogManager {
	private FileOutputStream mWriter;
    private FileInputStream mReader;
    private String mFilename;
    private Context mContext;
    private List<Entry> mEntries;

    public List<Entry> getEntries() { return mEntries; }

    public LogManager(Context context, final String filename) {
    	mContext = context;
    	mFilename = filename;
        load();
    }

    public void save() {
        try {
            mWriter = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            String data = "";
            Entry e = null;
            for (int i = 0; i < mEntries.size(); i++) {
                e = mEntries.get(i);
                data = e.getMacAddress() + "|";
                data += String.valueOf(e.getLatitude()) + "|";
                data += String.valueOf(e.getLongitude()) + "|";
                data += String.valueOf(e.getHeight()) + "|";
                data += e.getTime() + "|";
                data += String.valueOf(e.getEvent()) + "\n";
                mWriter.write(data.getBytes());
            }
        } catch (Exception e) { }
        finally {
            try {
                mWriter.close();
            } catch (Exception e) { }
        }
    }

    public void save(String mac, double lat, double lng, double height, String time, int event) {
    	Entry entry = new Entry(mac, lat, lng, height, time, event);
        mEntries.add(entry);
        save();
    }

    public void clear() {
        mEntries = new ArrayList<Entry>();
        try {
            mWriter = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            mWriter.write("".getBytes());
        } catch (Exception e) { }
        finally {
            try {
                mWriter.close();
            } catch (Exception e) { }
        }
    }

    public void load() {
    	mEntries = new ArrayList<Entry>();
    	int sByte;
    	String row = "";

    	try {
    		mReader = mContext.openFileInput(mFilename);
    		while ((sByte = mReader.read()) != -1) {
    			if ((char)sByte == '\n') {
    				addRow(row);
    				row = "";
    			} else {
    				row += (char)sByte;
    			}
    		}
    		mReader.close();
    	} catch (Exception e) { }
    }

    private void addRow(String row) {
    	String[] values = row.split("\\|");
    	String mac = values[0];
    	double lat = Double.parseDouble(values[1]);
    	double lng = Double.parseDouble(values[2]);
    	double height = Double.parseDouble(values[3]);
    	String time = values[4];
    	int event = Integer.parseInt(values[5]);
        Entry entry = new Entry(mac, lat, lng, height, time, event);
    	mEntries.add(entry);
    }

}

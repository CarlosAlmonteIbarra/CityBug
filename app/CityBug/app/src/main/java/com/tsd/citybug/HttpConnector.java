package com.tsd.citybug;

import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

public class HttpConnector {

    public boolean send(Entry entry) {
        String json = getJSON(entry);
        Boolean result = false;

        try {
            result = new HttpAsyncTask().execute(json).get(5000, TimeUnit.MILLISECONDS);
        } catch (Exception e) { }

        return result;
    }

    private String getJSON(Entry entry) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.accumulate("mac", entry.getMacAddress());
            jsonObject.accumulate("latitude", entry.getLatitude());
            jsonObject.accumulate("longitude", entry.getLongitude());
            jsonObject.accumulate("height", entry.getHeight());
            jsonObject.accumulate("datetime", entry.getTime());
            jsonObject.accumulate("type", entry.getEvent());
        } catch (Exception e) { }

        return jsonObject.toString();
    }

    private class HttpAsyncTask extends AsyncTask<String,Void,Boolean> {
        private final String IP_ADDRESS = "192.168.1.66";
        private final int PORT = 5000;

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                URI uri = new URI("http", null, IP_ADDRESS, PORT, null, null, null);
                HttpPost httpPost = new HttpPost(uri);
                StringEntity stringEntity = new StringEntity(params[0]);

                httpPost.setEntity(stringEntity);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");

                HttpResponse httpResponse = httpClient.execute(httpPost);
                InputStream inputStream = httpResponse.getEntity().getContent();
                if (inputStream != null)
                    return true;
            } catch (Exception e) { }
            return false;
        }

    }

}

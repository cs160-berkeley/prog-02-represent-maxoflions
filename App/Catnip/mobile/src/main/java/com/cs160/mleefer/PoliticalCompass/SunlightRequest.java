package com.cs160.mleefer.PoliticalCompass;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Max on 3/10/2016.
 */
public class SunlightRequest extends AsyncTask<String, Void, JSONObject> { // AsyncTask skeleton from http://stackoverflow.com/questions/6343166/how-to-fix-android-os-networkonmainthreadexception

    private Exception exception;

    protected JSONObject doInBackground(String... urls) {
        try {
            URL url= new URL(urls[0]);
            Log.d("D", "opening sunlight stream");
            InputStream is = url.openStream();
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8")); //response reading from http://stackoverflow.com/questions/6511880/how-to-parse-a-json-input-stream
            StringBuilder responseStrBuilder = new StringBuilder(512);

            String inputStr;

            Log.d("D", "sunlight loop");
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }
            JSONObject j= new JSONObject(responseStrBuilder.toString());
            Log.d("Debug", "sunlight positive return");
            return j;

        } catch (Exception e) {
            this.exception = e;
            Log.d("DEBUG", "sunlight exception" + e.toString());
            return null;
        }
    }

    @Override
    protected void onPostExecute(JSONObject j) {

    }
}
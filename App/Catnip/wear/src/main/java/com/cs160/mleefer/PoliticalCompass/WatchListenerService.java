package com.cs160.mleefer.PoliticalCompass;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WatchListenerService extends WearableListenerService {
    // In PhoneToWatchService, we passed in a path, either "/FRED" or "/LEXY"
    // These paths serve to differentiate different phone-to-watch messages
    private static final String SELECT_CANDIDATE = "/Select";
    private static final String TAG = "WATCHLISTENER";
    private static final String START_ACTIVITY_PATH = "/start-activity";
    private static final String DATA_ITEM_RECEIVED_PATH = "/data-item-received";

    private String county;
    private String state;
    private double op;
    private ArrayList<CandidateInfo> cands;

    private class PhoneToWatchPackage implements Serializable {
        public String county;
        public String state;
        public double op;
        public ArrayList<CandidateInfo> cands;

        public PhoneToWatchPackage(String _county, String _state, double _op, ArrayList<CandidateInfo> _cands) {
            county = _county;
            state = _state;
            op = _op;
            cands = _cands;
        }

        public boolean dataReady() {
            return county != null && state != null && op != -1 && cands != null;
        }

        public void clear() {
            county = null;
            state = null;
            op = -1;
            cands = null;
        }
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d(TAG, "onDataChanged");
        final List<DataEvent> events = FreezableUtils
                .freezeIterable(dataEvents);

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

        ConnectionResult connectionResult =
                googleApiClient.blockingConnect(30, TimeUnit.SECONDS);

        if (!connectionResult.isSuccess()) {
            Log.e(TAG, "Failed to connect to GoogleApiClient.");
            return;
        }

        for (DataEvent event : events) {
            Uri uri = event.getDataItem().getUri();

            String path = uri.getPath();
            DataMap payload = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();

            switch (path) {
                case "/reps":
                    //deserialize & assign
                    Log.d(TAG, "Setting reps");

                    ByteArrayInputStream bis = new ByteArrayInputStream(payload.getByteArray("REPS")); //deserialization from http://stackoverflow.com/questions/2836646/java-serializable-object-to-byte-array
                    ObjectInput in = null;
                    try {
                        in = new ObjectInputStream(bis);
                        cands = (ArrayList<CandidateInfo>) in.readObject();

                    } catch (IOException e) {
                        Log.d("WatchListener", "Could not deserialize");
                    } catch (ClassNotFoundException e) {

                    } finally {
                        try {
                            bis.close();
                        } catch (IOException ex) {
                            // ignore close exception
                        }
                        try {
                            if (in != null) {
                                in.close();
                            }
                        } catch (IOException ex) {
                            // ignore close exception
                        }
                    }

                    //set all to null
                    state = null;
                    county = null;
                    op = -1;
                    break;
                case "/state":
                    Log.d(TAG, "Setting state");
                    state = payload.getString("STATE");
                    break;
                case "/county":
                    Log.d(TAG, "Setting county");
                    county = payload.getString("COUNTY");
                    break;
                case "/obama_percent":
                    Log.d(TAG, "Setting percent");
                    op = payload.getDouble("COUNTY", 100.0);
                    break;
            }
            if (dataReady()) {
                String json = null; //JSON read from http://stackoverflow.com/questions/13814503/reading-a-json-file-in-android/13814551#13814551
                try {

                    InputStream is = getAssets().open("election_county_2012.json");

                    int size = is.available();

                    byte[] buffer = new byte[size];

                    is.read(buffer);

                    is.close();

                    json = new String(buffer, "UTF-8");


                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                Intent intent = new Intent(this, MainActivity.class );
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //you need to add this flag since you're starting a new activity from a service

                intent.putExtra("CANDIDATES", cands);
                intent.putExtra("STATE", state);
                intent.putExtra("COUNTY", county);
                intent.putExtra("OBAMAP", op);
                Log.d("T", "about to start watch MainActivity with CAT_NAME: Fred");
                startActivity(intent);
            }
        }
    }

    private boolean dataReady() {
        return county != null && state != null && op != -1 && cands != null;
    }


    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in WatchListenerService, got: " + messageEvent.getPath());
        //use the 'path' field in sendmessage to differentiate use cases
        //(here, fred vs lexy)
        Log.d("DEBUG", "Received");

        if( messageEvent.getPath().equalsIgnoreCase( SELECT_CANDIDATE ) ) {
            byte[] value = messageEvent.getData();
            String tmp = new String(value);
            Log.d(TAG, "got string: " + tmp);
            String[] s = tmp.split("!");
            ArrayList<CandidateInfo> cands = new ArrayList<>();
            for (String cand : s[0].split("=")) {
                cands.add(new CandidateInfo(cand));
            }
//            ByteArrayInputStream bis = new ByteArrayInputStream(value);
//            ObjectInput in = null;
//            PhoneToWatchPackage val = null;
//            try {
//                in = new ObjectInputStream(bis);
//                val = (PhoneToWatchPackage) in.readObject();
//            } catch (Exception e) {
//
//            } finally {
//                try {
//                    bis.close();
//                } catch (IOException ex) {
//                    // ignore close exception
//                }
//                try {
//                    if (in != null) {
//                        in.close();
//                    }
//                } catch (IOException ex) {
//                    // ignore close exception
//                }
//            }

            Intent intent = new Intent(this, MainActivity.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //you need to add this flag since you're starting a new activity from a service

            intent.putExtra("CANDIDATES", cands);
            intent.putExtra("STATE", s[1]);
            intent.putExtra("COUNTY", s[2]);
            intent.putExtra("OBAMAP", Double.parseDouble(s[3]));
            Log.d("T", "about to start watch MainActivity with CAT_NAME: Fred");
            startActivity(intent);
        } else {
            super.onMessageReceived(messageEvent);
        }

        Log.d("DEBUG", "finished receiving");

    }
}
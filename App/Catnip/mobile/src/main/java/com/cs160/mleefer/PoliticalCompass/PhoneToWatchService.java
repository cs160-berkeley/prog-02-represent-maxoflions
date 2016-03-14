package com.cs160.mleefer.PoliticalCompass;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mleefer on 2/19/16.
 */
public class PhoneToWatchService extends Service {

    private GoogleApiClient mApiClient;

    private static final String TAG = "DEBUG";

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
            if(!dataReady()) {
                Log.d(TAG, "AN ARGUMENT TO CONSTRUCTOR WAS NULL");
            }
        }

        private void writeObject(java.io.ObjectOutputStream stream)
                throws IOException {

            stream.writeBytes(county);
            stream.writeBytes(state);
            Log.d(TAG, "About to write cands%");
            stream.writeObject(cands);
            Log.d(TAG, "Wrote cands%");
            stream.writeDouble(op);
        }

        private void readObject(java.io.ObjectInputStream stream)
                throws IOException, ClassNotFoundException {
            county = stream.readUTF();
            state = stream.readUTF();
            Log.d(TAG, "readObject: ");
            cands = (ArrayList<CandidateInfo>) stream.readObject();
            op = stream.readDouble();
        }

        public boolean dataReady() {
            if(county == null || state == null || op == -1 || cands == null) {
                if (state == null) {
                    Log.d(TAG, "state is null");
                }
                if (county == null) {
                    Log.d(TAG, "subadmin is null");
                }
                if (cands == null) {
                    Log.d(TAG, "cands is null");
                }
                if (op == -1) {
                    Log.d(TAG, "op is null");
                }

                return false;
            } else {
                return true;
            }
        }

        public void clear() {
            county = null;
            state = null;
            op = -1;
            cands = null;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //initialize the googleAPIClient for message passing
        Log.d("DEBUG", "service Started");
        mApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                    }
                })
                .build();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mApiClient.disconnect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Which cat do we want to feed? Grab this info from INTENT
        // which was passed over when we called startService
        Log.d("DEBUG", "sending");
        if(intent == null) {
            return -1;
        }
        ArrayList<CandidateInfo> cands = intent.getParcelableArrayListExtra("FOUND_CANDIDATES");
        String county = intent.getStringExtra("COUNTY");
        String state = intent.getStringExtra("STATE");
        Double op = intent.getDoubleExtra("OBAMAP", 100);

        String val = "";
        for(CandidateInfo c: cands) {
            val += c.writeObject();
            val += "=";
        }
        val += "!" + county + "!";
        val += state + "!";
        final String value = val+ op.toString();

//        PhoneToWatchPackage val = new PhoneToWatchPackage(county, state, op, cands);

//        String msg = "";
//        for (String cand:cands) {
//            msg+=cand;
//            msg+="%";
//        }

        //send candidates
//        Log.d("DEBUG", "before deserialization");
//        ByteArrayOutputStream bos = new ByteArrayOutputStream(); // serialization to array from http://stackoverflow.com/questions/2836646/java-serializable-object-to-byte-array
//        ObjectOutput out = null;
//        final byte[] candBytes;
//        try {
//            Log.d("DEBUG", "1");
//            out = new ObjectOutputStream(bos);
//            Log.d("DEBUG", "2");
//            out.writeObject(val);
//            Log.d("DEBUG", "3");
//            candBytes = bos.toByteArray();
//
//            Log.d("DEBUG", "before thread");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    //first, connect to the apiclient
                    mApiClient.connect();
                    //now that you're connected, send a message with the cat name
                    Log.d("DEBUG", "sending now");
                    sendMessage("/Select", value.getBytes());
                }
            }).start();

            Log.d("DEBUG", "sent");


//        } catch(IOException e) {
//            Log.d("Phonetowatch", "onStartCommand: ");
//        } finally {
//            try {
//                if (out != null) {
//                    out.close();
//                }
//            } catch (IOException ex) {
//                // ignore close exception
//            }
//            try {
//                bos.close();
//            } catch (IOException ex) {
//                // ignore close exception
//            }
//        }
        return START_STICKY;
//        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/reps");
//        putDataMapReq.getDataMap().putByteArray("REPS", candBytes);
//        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
//        Wearable.DataApi.putDataItem(mApiClient, putDataReq);
//
//        //send location bits
//        putDataMapReq = PutDataMapRequest.create("/county");
//        putDataMapReq.getDataMap().putString("COUNTY", county);
//        putDataReq = putDataMapReq.asPutDataRequest();
//        Wearable.DataApi.putDataItem(mApiClient, putDataReq);
//
//        putDataMapReq = PutDataMapRequest.create("/state");
//        putDataMapReq.getDataMap().putString("STATE", state);
//        putDataReq = putDataMapReq.asPutDataRequest();
//        Wearable.DataApi.putDataItem(mApiClient, putDataReq);
//
//        putDataMapReq = PutDataMapRequest.create("/obama-percent");
//        putDataMapReq.getDataMap().putDouble("OBAMAP", op);
//        putDataReq = putDataMapReq.asPutDataRequest();
//        Wearable.DataApi.putDataItem(mApiClient, putDataReq);

//        // Send the message with the cat name
    }

    @Override //remember, all services need to implement an IBiner
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendMessage( final String path, final byte[] text ) {
        //one way to send message: start a new thread and call .await()
        //see watchtophoneservice for another way to send a message
        new Thread( new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mApiClient ).await();
                for(Node node : nodes.getNodes()) {
                    //we find 'nodes', which are nearby bluetooth devices (aka emulators)
                    //send a message for each of these nodes (just one, for an emulator)
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mApiClient, node.getId(), path, text ).await();
                    //4 arguments: api client, the node ID, the path (for the listener to parse),
                    //and the message itself (you need to convert it to bytes.)
                }
            }
        }).start();
    }

}

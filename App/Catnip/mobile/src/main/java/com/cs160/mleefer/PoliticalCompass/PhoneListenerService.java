package com.cs160.mleefer.PoliticalCompass;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by mleefer and noon on 2/19/16 at very late in the night. (early in the morning?)
 */
public class PhoneListenerService extends WearableListenerService {

//   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
private static final String DETAILED_VIEW = "/Select";
private static final String NEW_LOCATION = "/Random";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("DEBUG", "in PhoneListenerService, got: " + messageEvent.getPath());
        if( messageEvent.getPath().equalsIgnoreCase(DETAILED_VIEW) ) {
            Log.d("DEBUG", "Received Select");

            // Value contains the String we sent over in WatchToPhoneService, "good job"
            Intent intent = new Intent(this, DetailedViewActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            for (CandidateInfo cand : CandidateInfo.all_candidates) {
                if (value.equals(cand.getName())) {
                    intent.putExtra("REP", (Parcelable)cand);
                }
            }
            startActivity(intent);
        } else if(messageEvent.getPath().equalsIgnoreCase(NEW_LOCATION)) {

            Random r = new Random();

//            TODO repeatedly check if in US and retry
            ArrayList<CandidateInfo> cands = new ArrayList<>();
            double lon = 0;
            double lat = 0;
            while (cands.size() == 0){
                lon = -66.8 - (r.nextDouble() * 58.2);
                lat = 25 + (r.nextDouble() * 22.5);
                cands = LocationSelect.getRepsByLatLong(lat, lon, getApplicationContext());
            }
            Geocoder geocoder;  // LongLat to address code from http://stackoverflow.com/questions/9409195/how-to-get-complete-address-from-latitude-and-longitude
            List<Address> addresses = null;
            geocoder = new Geocoder(this, Locale.getDefault());

            boolean done = false;
            while (!done) {
                try {
                    addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    done = true;
                } catch (IOException e) {
                    done = false;
                }
            }
            String state = LocationSelect.state_abbrev_map.get(addresses.get(0).getAdminArea());
            String county = addresses.get(0).getSubAdminArea();
            if (county == null || state == null) {
                String TAG = "LOCATIONFAIL";
                Log.d(TAG, "failed to get some data from address");
                if (state == null) {
                    Log.d(TAG, "state is null");
                } else {
                    if (state.length() > 2) {

                    }
                    Log.d(TAG, "state: "+state);
                }
                if (county == null) {
                    Log.d(TAG, "subadmin is null");
                    county = addresses.get(0).getLocality();
                    if (county == null) {
                        Log.d(TAG, "locality is null");
                    } else {
                        Log.d(TAG, "county: " + county);
                    }
                }
            }



            Intent intent = new Intent(this, PhoneToWatchService.class);
            intent.putExtra("FOUND_CANDIDATES", cands);
            intent.putExtra("STATE", state);
            Log.d("NEW LOCATION WITH", county + state);
            intent.putExtra("OBAMAP", LocationSelect.getOPbyCountyState(county, state));
            intent.putExtra("COUNTY", county);
            startService(intent);

            intent = new Intent(this, RepsViewActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("CANDIDATES", cands);
            startActivity(intent);

        } else {
            super.onMessageReceived( messageEvent );
        }

    }
}

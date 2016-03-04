package com.cs160.mleefer.PoliticalCompass;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

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
                    intent.putExtra("REP", cand);
                }
            }
            startActivity(intent);
        } else if(messageEvent.getPath().equalsIgnoreCase(NEW_LOCATION)){

            Intent intent = new Intent(this, RepsViewActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            intent.putExtra("LOCATION", "66666");
            startActivity(intent);

        } else {
            super.onMessageReceived( messageEvent );
        }

    }
}

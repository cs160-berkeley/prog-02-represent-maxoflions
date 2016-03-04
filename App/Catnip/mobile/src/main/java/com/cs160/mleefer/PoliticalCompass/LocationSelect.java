package com.cs160.mleefer.PoliticalCompass;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class LocationSelect extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_select);

        EditText postal = (EditText) findViewById(R.id.postal_address);
        Button postalSubmit = (Button) findViewById(R.id.postal_button);
        FrameLayout locationButton = (FrameLayout) findViewById(R.id.gps_button);

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO get location from gps
                goToRepsView("66666");
            }
        });
    }

    public void goToRepsView(String location) {
        //        String reps[] = {"Jon Stewart", "John Doe", "Deez Nuts"};
        Intent intent = new Intent(this, RepsViewActivity.class);
        intent.putExtra("LOCATION", location);
        startActivity(intent);
    }

    public void goToRepsView(View v) {
        // TODO get candidates from location
//        String reps[] = {"Jon Stewart", "John Doe", "Deez Nuts"};
//        Intent intent = new Intent(this, RepsViewActivity.class);
//        intent.putExtra("LOCATION", location);
//        startActivity(intent);
        goToRepsView("94709");
    }
}

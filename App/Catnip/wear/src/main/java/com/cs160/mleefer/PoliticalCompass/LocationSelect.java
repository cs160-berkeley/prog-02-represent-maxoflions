package com.cs160.mleefer.PoliticalCompass;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;

public class LocationSelect extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_select);

        RelativeLayout r = (RelativeLayout) findViewById(R.id.bg);
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
    }
    private void start() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}

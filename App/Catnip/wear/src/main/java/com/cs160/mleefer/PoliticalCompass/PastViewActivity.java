package com.cs160.mleefer.PoliticalCompass;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class PastViewActivity extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_view);

        //TODO get actual statistics from location
        float Obama = 68;
        float Romney = 32;

        FrameLayout red = (FrameLayout) findViewById(R.id.red);
        FrameLayout blue = (FrameLayout) findViewById(R.id.blue);
        TextView redtext = (TextView) findViewById(R.id.name);
        TextView bluetext = (TextView) findViewById(R.id.bluetext);
        red.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, Romney));
        blue.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, Obama));
        redtext.setText(Double.toString(Romney) + "%");
        bluetext.setText(Double.toString(Obama) + "%");
    }
}

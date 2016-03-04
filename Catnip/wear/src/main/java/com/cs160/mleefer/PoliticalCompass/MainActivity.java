package com.cs160.mleefer.PoliticalCompass;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.wearable.view.GridViewPager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView mTextView;
    private Button mFeedBtn;
    private GestureDetectorCompat mDetector;
    private CandidateInfo[] candidates;
    private int curr_cand = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = new Intent(this, LocationSelect.class);
//        startActivity(intent);

//        mFeedBtn = (Button) findViewById(R.id.feed_btn);
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String[] locNcands = {};

        if (extras != null) {
            String value = extras.getString("CANDIDATES");
            Log.d("DEBUG", value);
            locNcands = value.split("@");
            String[] cands = locNcands[0].split("%");
            candidates = new CandidateInfo[cands.length];
            for (int i=0;i<cands.length;i++) {
                Log.d("DEBUG", cands[i]);
                String[] fields = cands[i].split("&", 3);
                candidates[i] = new CandidateInfo(fields[0], fields[1], fields[2]);
            }
        } else
        if (candidates == null) {
            Log.d("DEBUG", "CANDIDATES WAS NULL");
            Intent i = new Intent(this, LocationSelect.class);
            startActivity(i);
            return;
        }


        final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyGridPagerAdapter(this, getFragmentManager(), candidates, locNcands[1]));
//

//        updateRep(candidates[0]);

        /* do this in onCreate */
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
//        mAccelCurrent = SensorManager.GRAVITY_EARTH;
//        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

//    void changeRep(CandidateInfo cand) {
//        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//        MainFragment fragment = MainFragment.newInstance(cand.getParty(), cand.getPosition(), cand.getName(), cand.getColor());
//        fragmentTransaction.add(R.id.fragment_container, fragment);
//        fragmentTransaction.commit();
//
//    }

    void updateRep(CandidateInfo cand) {
        TextView text = (TextView) findViewById(R.id.name);
        text.setText(cand.getName());
        text = (TextView) findViewById(R.id.position);
        text.setText(cand.getPosition());
        text = (TextView) findViewById(R.id.party);
        text.setText(cand.getParty());
        RelativeLayout bg = (RelativeLayout) findViewById(R.id.bg);
        bg.setBackgroundColor(cand.getColor());
    }

    public void goToRandom() {
        //TODO use an API to get a random location
        Intent intent = new Intent(this, WatchToPhoneService.class);
        intent.putExtra("TYPE", "RANDOM");
        intent.putExtra("LOCATION", "94709");
        startService(intent);

    }

    public void goToPastView() {
        Intent intent = new Intent(this, PastViewActivity.class);
        intent.putExtra("REPS", candidates);
        startActivity(intent);
    }

    /* put this into your activity class. Shake code from http://stackoverflow.com/questions/2317428/android-i-want-to-shake-it */
    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private static float mAccelCurrent; // current acceleration including gravity
    private static float mAccelLast; // last acceleration including gravity

    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter
            if (delta > 10) {
                Log.d("DEBUG", "received shake event, "+ mAccel+" - "+mAccelLast);
                goToRandom();
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

//    @Override
//    protected void onResume() {
//        super.onResume();
//        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
//    }
//
//    @Override
//    protected void onPause() {
//        mSensorManager.unregisterListener(mSensorListener);
//        super.onPause();
//    }
}

package com.cs160.mleefer.PoliticalCompass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DetailedViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);

        CandidateInfo cand = getIntent().getParcelableExtra("REP");
        
        TextView text = (TextView) findViewById(R.id.name);
        text.setText(cand.getName());
        text = (TextView) findViewById(R.id.twitter);
        text.setText(cand.getTwitter());
        text = (TextView) findViewById(R.id.position);
        text.setText(cand.getPosition());
        text = (TextView) findViewById(R.id.party);
        text.setText(cand.getParty().substring(0, 1));
        text = (TextView) findViewById(R.id.email);
        text.setText(cand.getEmail());
        Linkify.addLinks(text, Linkify.EMAIL_ADDRESSES);
        text = (TextView) findViewById(R.id.website);
        text.setText(cand.getWebsite());
        Linkify.addLinks(text, Linkify.WEB_URLS);

        ListView lv = (ListView) findViewById(R.id.committees);
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cand.getCommittees());
        lv.setAdapter(itemsAdapter);

        lv = (ListView) findViewById(R.id.recent_bills);
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cand.getRecentBills());
        lv.setAdapter(itemsAdapter);


        Log.d("T", "COLOR: " + Integer.toString(cand.getColor()));
        Log.d("T", "COLOR: " + cand.getParty());
        RelativeLayout bg = (RelativeLayout) findViewById(R.id.bg);
        bg.setBackgroundColor(cand.getColor());
        ListView lst = (ListView) findViewById(R.id.committees);
        lst.setBackgroundColor(cand.getSubColor());
        lst = (ListView) findViewById(R.id.recent_bills);
        lst.setBackgroundColor(cand.getSubColor());
        ImageView pic = (ImageView) findViewById(R.id.photo_inner);
        cand.setImage(pic, getApplicationContext());
    }
}

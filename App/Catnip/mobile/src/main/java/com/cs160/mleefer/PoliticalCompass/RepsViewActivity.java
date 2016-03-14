package com.cs160.mleefer.PoliticalCompass;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RepsViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reps_view);

        ArrayList<CandidateInfo> reps = getIntent().getParcelableArrayListExtra("CANDIDATES");


//        final List<CandidateInfo> candidatesData = new ArrayList<>();
//        //TODO get reps from location instead
//        String[] fake_candidates = {"Jon Stewart", "John Doe", "Deez Nuts"};
//        for(String rep_name :fake_candidates)
//        {
//            candidatesData.add(new CandidateInfo(rep_name));
//        }
//        candidatesData.get(2).setParty("Republican");
//
        CandidateInfo allCands[] = new CandidateInfo[reps.size()];
        CandidateInfo.all_candidates = reps.toArray(allCands);

        ListView list = (ListView) findViewById(R.id.listView);
        MyAdapter mAdapter=new MyAdapter(getApplicationContext(), reps);

        mAdapter.notifyDataSetInvalidated();
        list.setAdapter(mAdapter);
    }


    public void goToDetailedView(CandidateInfo candy) {
        Intent intent = new Intent(this, DetailedViewActivity.class);
        intent.putExtra("REP", (Parcelable) candy);
        startActivity(intent);
    }

    public class MyAdapter extends ArrayAdapter<CandidateInfo> {

        private final Context context;
        private final List<CandidateInfo> values;


        public MyAdapter(Context context, List<CandidateInfo> values) {
            super(context, -1, values);
            this.context = context;
            this.values = values;
        }


        @Override
        public int getCount() {
            return values.size();
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        public void addItem(final CandidateInfo item) {
            values.add(item);
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//            LayoutInflater inflater = (LayoutInflater)  context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View rowView = inflater.inflate(R.layout.representative_card, parent, false);

            View rowView = LayoutInflater.from(context).inflate(R.layout.representative_card, null);
//TODO figure out why links don't work
            TextView text = (TextView) rowView.findViewById(R.id.name);
            text.setText(values.get(position).getName());
            text = (TextView) rowView.findViewById(R.id.twitter);
            text.setText(values.get(position).getTwitter());
            text = (TextView) rowView.findViewById(R.id.position);
            text.setText(values.get(position).getPosition());
            text = (TextView) rowView.findViewById(R.id.party);
            text.setText(values.get(position).getParty().substring(0, 1));
            text = (TextView) rowView.findViewById(R.id.email);
            text.setText(values.get(position).getEmail());
            Linkify.addLinks(text, Linkify.EMAIL_ADDRESSES);
            text = (TextView) rowView.findViewById(R.id.website);
            text.setText(values.get(position).getWebsite());
            Linkify.addLinks(text, Linkify.WEB_URLS);

            ImageView pic = (ImageView) rowView.findViewById(R.id.photo_inner);
            values.get(position).setImage(pic, getApplicationContext());

            ImageView details_button = (ImageView) rowView.findViewById(R.id.details_arrow);


            details_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String n = ((TextView) ((View) v.getParent().getParent())
                            .findViewById(R.id.name)).getText().toString();
                    for (CandidateInfo cand : CandidateInfo.all_candidates) {
                        if(cand.getName().equals(n)) {
                            goToDetailedView(cand);
                        }
                    }
                    Log.d("BAD", "BAD JUJU");
                }
            });

            RelativeLayout color = (RelativeLayout) rowView.findViewById(R.id.bg);
            color.setBackgroundColor(values.get(position).getColor());


            FrameLayout details_image = (FrameLayout) rowView.findViewById(R.id.photo);

            details_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CandidateInfo c = new CandidateInfo(
                            ((TextView) ((View) v.getParent()).findViewById(R.id.name)).getText().toString());
                    goToDetailedView(c);
                }
            });

            return rowView;
        }
    }

}
package com.cs160.mleefer.PoliticalCompass;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Max on 3/2/2016.
 */
public class CandidateInfo implements Parcelable, Serializable {
    private String twitter = "null";
    private String twitter_id = "null";
    private String name;
    private String party;
    private String email;
    private String website;
    private String position;
    private String photo_path = "null";
    private String bio = "null";
    private ArrayList<String> committees = new ArrayList<>();
    private ArrayList<String> recent_bills = new ArrayList<>();

    public static CandidateInfo[] all_candidates;

    CandidateInfo(JSONObject j, Context context) {
        try {
//            theunitedstates.io/congress/225x225

            bio = j.getString("bioguide_id");

            String apikey = "57cb6d8d4bc94e36bf0a9c4644752dbe"; //url generation from http://stackoverflow.com/questions/30129602/i-am-trying-to-connect-to-the-sunlight-foundation-api-in-an-android-app-but-the
            String baseURL = "https://congress.api.sunlightfoundation.com";
            String requestAddition = "/committees?apikey="+apikey + "&member_ids=" + bio;
            String url = baseURL + requestAddition;
            AsyncTask getCommittees = new SunlightRequest().execute(url);

            requestAddition = "/bills?apikey="+apikey + "&sponsor_id=" + bio;
            url = baseURL + requestAddition;
            AsyncTask getBills = new SunlightRequest().execute(url);

//            requestAddition = "/bills?apikey="+apikey + "&member_ids=" + bio;
//            url = baseURL + requestAddition;
//            AsyncTask getBills = new SunlightRequest().execute(url);





            RequestCreator later = Picasso.with(context).load("https://theunitedstates.io/images/congress/225x275/" + bio + ".jpg"); // .into(pictureView);

            photo_path = "https://theunitedstates.io/images/congress/225x275/" + bio + ".jpg";

            name = j.getString("first_name") + " " + j.getString("last_name");
            if (j.isNull("twitter_id")) {
                photo_path = "@drawable/default_icon";
                twitter = name + " doesn't appear to have a twitter.";
            } else {
                twitter_id = j.getString("twitter_id");
                //TODO get last tweet and photo
            }
            party = j.getString("party");
            switch (party) {
                case "D":
                    party = "Democrat";
                    break;
                case "R":
                    party = "Republican";
                    break;
                default:
                    party = "Independent";
                    break;
            }
            email = j.getString("oc_email");
            website = j.getString("website");
            position = j.getString("title");

            ArrayList<String> committeesList = new ArrayList<String>();
            JSONObject c = null;
            while(c == null) {
                try {
                    c = (JSONObject) getCommittees.get(5, TimeUnit.SECONDS);
                } catch(Exception e) {
                    Log.d("ERR", "CandInfo init exception " + e.toString());
                }
            }
            JSONArray coms = c.getJSONArray("results");
            for(int i = 0; i<coms.length(); i++) {
                JSONObject com = coms.getJSONObject(i);
                String com_name = com.getString("name");
                if (com_name == null) {
                    continue;
                }
                committeesList.add(com_name);
            }
            committees = committeesList;



            JSONObject bill;
            ArrayList<String> billsList = new ArrayList<String>();
            while(true) {
                try {
                    bill = (JSONObject) getBills.get();
                    break;
                } catch(Exception e) {}
            }
            JSONArray bs = bill.getJSONArray("results");
            for(int i = 0; i<bs.length(); i++) {
                JSONObject b = bs.getJSONObject(i);
                String bill_name = b.getString("short_title");
                if (bill_name == null || bill_name.equals("null") ) {
                    continue;
                }

                bill_name+= " (" + b.getString("introduced_on") + ")";
                billsList.add(bill_name);
            }
            recent_bills = billsList;

        } catch(JSONException e) {
            Log.e("Malformed JSON", "JSONtoCandidateInfoList: ", e);
        }
    }

    public String getWebsite() {
        return website;
    }

    public void setImage(ImageView v, Context context) {
        Picasso.with(context).load("https://theunitedstates.io/images/congress/225x275/" + bio + ".jpg").into(v);
    }



    public String getTwitter() {
        return twitter;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public String getParty() {
        return party;
    }

    public int getColor() {
        if (party.equals("Democrat")) {
            return Color.parseColor("#4489fe");
        } else
        if (party.equals("Republican")) {
            return Color.parseColor("#C43F3F");
        } else {
            return Color.parseColor("#7F7F7F");
        }
    }
    public int getSubColor() {
        if (party.equals("Democrat")) {
            return Color.parseColor("#7394f8");
        } else
        if (party.equals("Republican")) {
            return Color.parseColor("#C86666");
        } else {
            return Color.parseColor("#8F8F8F");
        }
    }

    public void setParty(String p) {
        party = p;
    }

    public String getEmail() {
        return email;
    }

    public String getPosition() {
        return position;
    }

    public String getPhoto_path() {
        return photo_path;
    }

    public ArrayList<String> getCommittees() {
        return committees;
    }

    public ArrayList<String> getRecentBills() {
        return recent_bills;
    }

    public String toWatchString() {
        return name+"&"+position+"&"+party;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private void writeObject(java.io.ObjectOutputStream dest)
            throws IOException {
        Log.d("CIWRITE", "Writing cand");
        dest.writeBytes(twitter);
        dest.writeBytes(name);
        dest.writeBytes(party);
        dest.writeBytes(email);
        dest.writeBytes(website);
        dest.writeBytes(position);
        dest.writeBytes(photo_path);
        dest.writeBytes(bio);
        dest.writeObject(committees);
        dest.writeObject(recent_bills);
        Log.d("CIWRITE", "Done writing cand");
    }

    private void readObject(java.io.ObjectInputStream dest)
            throws IOException, ClassNotFoundException {
        twitter = dest.readUTF();
        name = dest.readUTF();
        party = dest.readUTF();
        email = dest.readUTF();
        website = dest.readUTF();
        position = dest.readUTF();
        photo_path = dest.readUTF();
        bio = dest.readUTF();
        committees = (ArrayList<String>) dest.readObject();
        recent_bills = (ArrayList<String>) dest.readObject();
    }

    public String writeObject() {
        String s = twitter + "%" +
                name + "%"+
                party + "%"+
                email + "%"+
                website + "%"+
                position + "%"+
                photo_path + "%"+
                bio + "%"+
                TextUtils.join("$", committees) + "%"+
                TextUtils.join("$", recent_bills);
        Log.d("DB", "writing CI to "+s);
        return s;
    }

    public CandidateInfo(String s) {
        String[] vars = s.split("%");
        twitter = vars[0];
        name = vars[1];
        party = vars[2];
        email = vars[3];
        website = vars[4];
        position = vars[5];
        photo_path = vars[6];
        bio = vars[7];
        committees = readArray(vars[8]);
        recent_bills = readArray(vars[9]);
    }

    private ArrayList<String> readArray(String in) {
        ArrayList<String> a = new ArrayList<>();
        for (String s: in.split("$")) {
            a.add(s);
        }
        return a;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(twitter);
        dest.writeString(name);
        dest.writeString(party);
        dest.writeString(email);
        dest.writeString(website);
        dest.writeString(position);
        dest.writeString(photo_path);
        dest.writeString(bio);
        dest.writeSerializable(committees);
        dest.writeSerializable(recent_bills);
    }

    public static final Parcelable.Creator<CandidateInfo> CREATOR = new Parcelable.Creator<CandidateInfo>() {
        public CandidateInfo createFromParcel(Parcel in) {
            return new CandidateInfo(in);
        }

        public CandidateInfo[] newArray(int size) {
            return new CandidateInfo[size];
        }
    };

    public CandidateInfo(Parcel in) {
        twitter = in.readString();
        name = in.readString();
        party = in.readString();
        email = in.readString();
        website = in.readString();
        position = in.readString();
        photo_path = in.readString();
        bio = in.readString();
        committees = (ArrayList<String>) in.readSerializable();
        recent_bills = (ArrayList<String>) in.readSerializable();
    }
}
package com.cs160.mleefer.PoliticalCompass;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

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

    public String getWebsite() {
        return website;
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
            return Color.parseColor("#807F7F");
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
        Log.d("CIWRITE", "Writing cand");
        return twitter + "%" +
                name + "%"+
                party + "%"+
                email + "%"+
                website + "%"+
                position + "%"+
                photo_path + "%"+
                bio + "%"+
                TextUtils.join("$", committees) + "%"+
                TextUtils.join("$", recent_bills);
    }

    public CandidateInfo(String s) {
        Log.d("DB", "Making CI from "+s);
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
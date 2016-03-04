package com.cs160.mleefer.PoliticalCompass;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Max on 3/2/2016.
 */
public class CandidateInfo implements Parcelable {
    private String twitter;
    private String name;
    private String party;
    private String email;
    private String website;
    private String position;
    private String photo_path;
    private String[] committees = {"Pen Fifteen Club", "Awesome Possums"};
    private String[] recent_bills = {"Bill 1", "Bill 2", "Bill 3", "Bill 4", "Bill 5", "Bill 6", "bill 7"};

    public static CandidateInfo[] all_candidates;

    CandidateInfo(String _name) {
        twitter = "And their words to the root and the rock would echo down, down and the magic would hear and answer, faint as a falling butterfly.";
        name = _name;
        party = "Democrat";
        email = "JJ-DaBomb@gmail.com";
        website = "http://www.thedailyshow.com";
        position = "Senator";
        photo_path = "@drawable/jon_stewart";

    }

    //TODO make the getters retrieve info from the APIs
    public String getWebsite() {
        return website;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getName() {
        return name;
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

    public String[] getCommittees() {
        return committees;
    }

    public String[] getRecentBills() {
        return recent_bills;
    }

    public String toWatchString() {
        return name+"&"+position+"&"+party;
    }

    @Override
    public int describeContents() {
        return 0;
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
        dest.writeStringArray(committees);
        dest.writeStringArray(recent_bills);
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
        committees = in.createStringArray();
        recent_bills = in.createStringArray();
    }
}
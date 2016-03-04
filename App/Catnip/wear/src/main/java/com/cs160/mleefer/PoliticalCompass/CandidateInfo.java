package com.cs160.mleefer.PoliticalCompass;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Max on 3/2/2016.
 */
public class CandidateInfo implements Parcelable {
    private String name;
    private String party;
    private String position;

    CandidateInfo(String _name, String _position, String _party) {
        name = _name;
        party = _party;
        position = _position;
    }

    public String getName() {
        return name;
    }

    public String getParty() {
        return party;
    }

    public int getColor() {
        if (party == "Democrat") {
            return Color.parseColor("#4489fe");
        } else
        if (party == "Republican") {
            return Color.parseColor("#C43F3F");
        } else {
            return Color.parseColor("#807F7F");
        }
    }

    public void setParty(String p) {
        party = p;
    }

    public String getPosition() {
        return position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(party);
        dest.writeString(position);
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
        name = in.readString();
        party = in.readString();
        position = in.readString();
    }
}
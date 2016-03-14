package com.cs160.mleefer.PoliticalCompass;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.telecom.ConnectionRequest;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class LocationSelect extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    //theunited.io/images/congress/225x275/[bioguide].jpg

    private GoogleApiClient mGoogleApiClient = null;
    private Location mLastLocation = null;
    private static JSONArray counties = null;
    private static final String googleAPIKey = "AIzaSyCqTBj9_Kp_djnJp_4Wro8IdMHy0CiZ4g0";
    private static final String sunlightAPIKey = "57cb6d8d4bc94e36bf0a9c4644752dbe";
    public static HashMap<String, String> state_abbrev_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_select);

        if(state_abbrev_map == null) {
            state_abbrev_map = new HashMap<String, String>();
            state_abbrev_map.put("Alabama", "AL");
            state_abbrev_map.put("Alaska", "AK");
            state_abbrev_map.put("Alberta", "AB");
            state_abbrev_map.put("American Samoa", "AS");
            state_abbrev_map.put("Arizona", "AZ");
            state_abbrev_map.put("Arkansas", "AR");
            state_abbrev_map.put("Armed Forces (AE)", "AE");
            state_abbrev_map.put("Armed Forces Americas", "AA");
            state_abbrev_map.put("Armed Forces Pacific", "AP");
            state_abbrev_map.put("British Columbia", "BC");
            state_abbrev_map.put("California", "CA");
            state_abbrev_map.put("Colorado", "CO");
            state_abbrev_map.put("Connecticut", "CT");
            state_abbrev_map.put("Delaware", "DE");
            state_abbrev_map.put("District Of Columbia", "DC");
            state_abbrev_map.put("Florida", "FL");
            state_abbrev_map.put("Georgia", "GA");
            state_abbrev_map.put("Guam", "GU");
            state_abbrev_map.put("Hawaii", "HI");
            state_abbrev_map.put("Idaho", "ID");
            state_abbrev_map.put("Illinois", "IL");
            state_abbrev_map.put("Indiana", "IN");
            state_abbrev_map.put("Iowa", "IA");
            state_abbrev_map.put("Kansas", "KS");
            state_abbrev_map.put("Kentucky", "KY");
            state_abbrev_map.put("Louisiana", "LA");
            state_abbrev_map.put("Maine", "ME");
            state_abbrev_map.put("Manitoba", "MB");
            state_abbrev_map.put("Maryland", "MD");
            state_abbrev_map.put("Massachusetts", "MA");
            state_abbrev_map.put("Michigan", "MI");
            state_abbrev_map.put("Minnesota", "MN");
            state_abbrev_map.put("Mississippi", "MS");
            state_abbrev_map.put("Missouri", "MO");
            state_abbrev_map.put("Montana", "MT");
            state_abbrev_map.put("Nebraska", "NE");
            state_abbrev_map.put("Nevada", "NV");
            state_abbrev_map.put("New Brunswick", "NB");
            state_abbrev_map.put("New Hampshire", "NH");
            state_abbrev_map.put("New Jersey", "NJ");
            state_abbrev_map.put("New Mexico", "NM");
            state_abbrev_map.put("New York", "NY");
            state_abbrev_map.put("Newfoundland", "NF");
            state_abbrev_map.put("North Carolina", "NC");
            state_abbrev_map.put("North Dakota", "ND");
            state_abbrev_map.put("Northwest Territories", "NT");
            state_abbrev_map.put("Nova Scotia", "NS");
            state_abbrev_map.put("Nunavut", "NU");
            state_abbrev_map.put("Ohio", "OH");
            state_abbrev_map.put("Oklahoma", "OK");
            state_abbrev_map.put("Ontario", "ON");
            state_abbrev_map.put("Oregon", "OR");
            state_abbrev_map.put("Pennsylvania", "PA");
            state_abbrev_map.put("Prince Edward Island", "PE");
            state_abbrev_map.put("Puerto Rico", "PR");
            state_abbrev_map.put("Quebec", "QC");
            state_abbrev_map.put("Rhode Island", "RI");
            state_abbrev_map.put("Saskatchewan", "SK");
            state_abbrev_map.put("South Carolina", "SC");
            state_abbrev_map.put("South Dakota", "SD");
            state_abbrev_map.put("Tennessee", "TN");
            state_abbrev_map.put("Texas", "TX");
            state_abbrev_map.put("Utah", "UT");
            state_abbrev_map.put("Vermont", "VT");
            state_abbrev_map.put("Virgin Islands", "VI");
            state_abbrev_map.put("Virginia", "VA");
            state_abbrev_map.put("Washington", "WA");
            state_abbrev_map.put("West Virginia", "WV");
            state_abbrev_map.put("Wisconsin", "WI");
            state_abbrev_map.put("Wyoming", "WY");
            state_abbrev_map.put("Yukon Territory", "YT");
        }

        if (!Geocoder.isPresent()) {
            Log.d("THIS IS BAD", "No geocoder");
            return;
        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        EditText postal = (EditText) findViewById(R.id.postal_address);
        Button postalSubmit = (Button) findViewById(R.id.postal_button);
        FrameLayout locationButton = (FrameLayout) findViewById(R.id.gps_button);

        if (counties == null) {
            String json = null; //JSON read from http://stackoverflow.com/questions/13814503/reading-a-json-file-in-android/13814551#13814551
            try {

                InputStream is = getAssets().open("election_county_2012.json");

                int size = is.available();

                byte[] buffer = new byte[size];

                is.read(buffer);

                is.close();

                json = new String(buffer, "UTF-8");

                counties = new JSONArray(json);

            } catch (IOException | JSONException ex) {
                ex.printStackTrace();
            }
        }



        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO get location from gps

                Log.d("T", "Waiting on location");
                while (mLastLocation == null) {
                    Thread.yield();
                }
                Log.d("T", "GOT LOCATION");

                Geocoder geocoder;  // LongLat to address code from http://stackoverflow.com/questions/9409195/how-to-get-complete-address-from-latitude-and-longitude
                List<Address> addresses = null;
                geocoder = new Geocoder(LocationSelect.this, Locale.getDefault());

                ArrayList<CandidateInfo> cands = getRepsByLatLong(mLastLocation.getLatitude(), mLastLocation.getLongitude(), getApplicationContext());

                boolean done = false;
                while (!done) {
                    try {
                        addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        done = true;
                    } catch (IOException e) {
                        done = false;
                    }
                }
                String state = state_abbrev_map.get(addresses.get(0).getAdminArea());
                String county = addresses.get(0).getSubAdminArea();
                String postalcode = addresses.get(0).getPostalCode();
                
                if (postalcode == null || county == null || state == null) {
                    String TAG = "LOCATIONFAIL";
                    Log.d(TAG, "failed to get some data from address");
                    if (postalcode == null) {
                        Log.d(TAG, "postalcode is null");
                    } else {
                        Log.d(TAG, "postalcode: "+postalcode);
                    }
                    if (state == null) {
                        Log.d(TAG, "state is null");
                    } else {
                        if (state.length() > 2) {
                            
                        }
                        Log.d(TAG, "state: "+state);
                    }
                    if (county == null) {
                        Log.d(TAG, "subadmin is null");
                        county = addresses.get(0).getLocality();
                        if (county == null) {
                            Log.d(TAG, "locality is null");
                        } else {
                            Log.d(TAG, "county: " + county);
                        }
                    }
                }


                goToRepsView(cands, state, county);

            }
        });
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        while (mLastLocation == null) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            Log.d("DEBUG", "location call returned");
            Thread.yield();
        }
    }

    public void onConnectionSuspended(int i) {
    }

    public void onConnectionFailed(ConnectionResult c) {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    public void goToRepsView(ArrayList<CandidateInfo> cands, String state, String county) {
        //        String reps[] = {"Jon Stewart", "John Doe", "Deez Nuts"};

        Intent intent = new Intent(this, PhoneToWatchService.class);
        intent.putExtra("FOUND_CANDIDATES", cands);
        intent.putExtra("STATE", state);
        intent.putExtra("OBAMAP", getOPbyCountyState(county, state));
        intent.putExtra("COUNTY", county);
        startService(intent);

        intent = new Intent(this, RepsViewActivity.class);
        intent.putExtra("CANDIDATES", cands);
        startActivity(intent);
    }

    public void goToRepsView(View v) {
        String zip = ((EditText) findViewById(R.id.postal_address)).getText().toString();
        ArrayList<CandidateInfo> cands = getRepsByZip(zip);

        Geocoder geocoder;  // LongLat to address code from http://stackoverflow.com/questions/9409195/how-to-get-complete-address-from-latitude-and-longitude
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        boolean done = false;
        while (!done) {
            try {
                addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                done = true;
            } catch (IOException e) {
                done = false;
                Log.d("T", "Get address error");
            }
        }
        String state = addresses.get(0).getAdminArea();
        String county = addresses.get(0).getSubAdminArea();

        goToRepsView(cands, state, county);
    }

    public static double getOPbyCountyState(String county, String state) {
        Log.d("GETOPWITH", county + state);
        try {
            for (int i = 0; i < counties.length(); i++) {
                JSONObject j = counties.getJSONObject(i);
                if (state.equals(j.getString("state-postal")) && county.equals(j.getString("county-name"))) {
                    return 100.0 * j.getDouble("obama-vote") / (j.getDouble("obama-vote") + j.getDouble("romney-vote"));
                }
            }
            Log.d("DEBUG", "Didn't find county");
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
        }
        return -1;
    }


    public ArrayList<CandidateInfo> getRepsByZip(String zip) {

        //url generation from http://stackoverflow.com/questions/30129602/i-am-trying-to-connect-to-the-sunlight-foundation-api-in-an-android-app-but-the
        String baseURL = "https://congress.api.sunlightfoundation.com";
        String zipCodeAddition = "/legislators/locate?apikey="+sunlightAPIKey + "&zip=" + zip;
        String url = baseURL + zipCodeAddition;


        JSONObject j;
        AsyncTask call = new SunlightRequest().execute(url);
        while(true) {
            try {
                j = (JSONObject) call.get(1, TimeUnit.SECONDS);
                break;
            } catch(Exception e) {
                Log.d("T", "Waiting on sunlight");
            }
        }
        return JSONtoCandidateInfoList(j, this.getApplicationContext());
    }

    public static ArrayList<CandidateInfo> getRepsByLatLong(double lat, double lon, Context c) {

        String apikey = "57cb6d8d4bc94e36bf0a9c4644752dbe"; //url generation from http://stackoverflow.com/questions/30129602/i-am-trying-to-connect-to-the-sunlight-foundation-api-in-an-android-app-but-the
        String baseURL = "https://congress.api.sunlightfoundation.com";
        String coordsAddition = "/legislators/locate?apikey="+apikey +
                "&latitude=" + Double.toString(lat) +
                "&longitude=" + Double.toString(lon);
        String url = baseURL + coordsAddition;


        JSONObject j = null;
        AsyncTask call = new SunlightRequest().execute(url);
        Log.d("SUN2URL", url);
        while( j == null) {
            Log.d("DEBUG", "sunlight call");
            try {
                j = (JSONObject) call.get(5, TimeUnit.SECONDS);
            } catch(TimeoutException e) {
                Log.d("T", "Timeout Error waiting on sunlight 2");
                j = null;
            } catch(Exception e) {
                Log.d("T", "Error waiting on sunlight 2");
                j = null;
            }
        }
        Log.d("DEBUG", "Got json");
        return JSONtoCandidateInfoList(j, c);
    }

    public static ArrayList<CandidateInfo> JSONtoCandidateInfoList(JSONObject j, Context c) {
        try {
            ArrayList<CandidateInfo> res = new ArrayList<>();
            JSONArray rawCands = j.getJSONArray("results");
            for(int i=0; i<rawCands.length(); i++) {
                res.add(new CandidateInfo(rawCands.getJSONObject(i), c));
            }
            return res;
        } catch(JSONException e) {
            Log.e("Malformed JSON", "JSONtoCandidateInfoList: ", e);
            return null;
        }

    }
}

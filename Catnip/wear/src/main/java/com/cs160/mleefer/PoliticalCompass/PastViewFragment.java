package com.cs160.mleefer.PoliticalCompass;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.text.DecimalFormat;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PastViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PastViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PastViewFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String OBAMA = "OBAMA";
    private static final String ROMNEY = "ROMNEY";

    // TODO: Rename and change types of parameters
    private float obama;
    private float romney;
    private String location;

    private OnFragmentInteractionListener mListener;

    public PastViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PastViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PastViewFragment newInstance(float obama, float romney, String loc) {
        PastViewFragment fragment = new PastViewFragment();
        Bundle args = new Bundle();
        args.putFloat(OBAMA, obama);
        args.putFloat(ROMNEY, romney);
        args.putString("LOCATION", loc);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            obama = getArguments().getFloat(OBAMA);
            romney = getArguments().getFloat(ROMNEY);
            location = getArguments().getString("LOCATION");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_past_view, container, false);


        FrameLayout red = (FrameLayout) v.findViewById(R.id.red);
        FrameLayout blue = (FrameLayout) v.findViewById(R.id.blue);
        TextView redtext = (TextView) v.findViewById(R.id.name);
        TextView bluetext = (TextView) v.findViewById(R.id.bluetext);
        TextView state = (TextView) v.findViewById(R.id.state);
        TextView county = (TextView) v.findViewById(R.id.county);
        red.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, (float) 0.3));//romney));
        blue.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, (float) 0.7));//obama));
        DecimalFormat df = new DecimalFormat("#.##");
        redtext.setText(df.format(romney) + "%");
        bluetext.setText(df.format(obama) + "%");
        if (location.equals("66666")) {
            state.setText("Hell");
            county.setText("Seventh Circle");
        } else {
            state.setText("CA");
            county.setText("Alameda");
        }
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

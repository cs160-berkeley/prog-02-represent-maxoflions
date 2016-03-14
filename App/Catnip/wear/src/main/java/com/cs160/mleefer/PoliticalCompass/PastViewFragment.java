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
    private String state;
    private String county;

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
    public static PastViewFragment newInstance(Bundle b) {
        PastViewFragment fragment = new PastViewFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            obama = (float)((double) getArguments().getDouble("OBAMAP"));
            state = getArguments().getString("STATE");
            county = getArguments().getString("COUNTY");
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
        red.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 100 - obama));//romney));
        blue.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, obama));//obama));
        DecimalFormat df = new DecimalFormat("#.##");
        bluetext.setText(df.format(obama) + "%");
        redtext.setText(df.format(100 - obama) + "%");
        state.setText(this.state);
        county.setText(this.county);
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

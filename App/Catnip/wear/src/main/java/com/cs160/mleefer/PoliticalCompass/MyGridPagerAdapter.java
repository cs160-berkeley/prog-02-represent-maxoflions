package com.cs160.mleefer.PoliticalCompass;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.wearable.view.FragmentGridPagerAdapter;

import java.util.List;

/**
 * Created by Max on 3/3/2016.
 */
public class MyGridPagerAdapter extends FragmentGridPagerAdapter {

    private final Context mContext;
    private List mRows;
    private CandidateInfo[] candidates;
    private String loc;

    public MyGridPagerAdapter(Context ctx, FragmentManager fm, CandidateInfo[] cands, String _loc) {
        super(fm);
        mContext = ctx;
        candidates = cands;
        loc = _loc;
    }

    static final int[] BG_IMAGES = new int[] {
    };

    // A simple container for static data in each page
    private static class Page {
        // static resources
        int titleRes;
        int textRes;
        int iconRes;
    }

    // Create a static set of pages in a 2D array
    private final Page[][] PAGES = {};

    public int getColumnCount(int row) {
        return 2;
    }

    public int getRowCount() {
        return candidates.length;
    }


    @Override
    public Fragment getFragment(int row, int col) {
        if(col == 0) {
//            Page page = PAGES[row][col];
            //        String title =
            //                page.titleRes != 0 ? mContext.getString(page.titleRes) : null;
            //        String text =
            //                page.textRes != 0 ? mContext.getString(page.textRes) : null;
            CandidateInfo cand = candidates[row];
            MainFragment fragment = MainFragment.newInstance(cand.getParty(), cand.getPosition(), cand.getName(), cand.getColor());

            // Advanced settings (card gravity, card expansion/scrolling)
//            fragment.setCardGravity(page.cardGravity);
//            fragment.setExpansionEnabled(page.expansionEnabled);
//            fragment.setExpansionDirection(page.expansionDirection);
//            fragment.setExpansionFactor(page.expansionFactor);
            return fragment;
        } else {
            //TODO get actual values from api
            PastViewFragment fragment = PastViewFragment.newInstance((float) 68.3, (float) 31.7, loc);
            return fragment;
        }
    }
}

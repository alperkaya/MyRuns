package com.example.alperkaya.myruns;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int numOfTabs;

    public PagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                StartFragment mStartFragment = new StartFragment();
                return mStartFragment;
            case 1:
                HistoryFragment mHistoryFragment = new HistoryFragment();
                return mHistoryFragment;
            case 2:
                SettingsFragment mSettingsFragment = new SettingsFragment();
                return mSettingsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}

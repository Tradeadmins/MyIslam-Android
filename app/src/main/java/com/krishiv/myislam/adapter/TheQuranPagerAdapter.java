package com.krishiv.myislam.adapter;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.krishiv.myislam.R;
import com.krishiv.myislam.fragment.FragmentSura;

public class TheQuranPagerAdapter extends FragmentPagerAdapter {

    public TheQuranPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new FragmentSura();
            case 1:
                // Games fragment activity
                return new FragmentSura();
            case 2:
                // Movies fragment activity
                return new FragmentSura();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}
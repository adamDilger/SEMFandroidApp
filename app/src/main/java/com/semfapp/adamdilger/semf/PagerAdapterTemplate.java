package com.semfapp.adamdilger.semf;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by adamdilger on 8/01/2016.
 */
public class PagerAdapterTemplate extends FragmentPagerAdapter {

    public PagerAdapterTemplate(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle (int position) {
        return "Page " + String.valueOf(position + 1);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}

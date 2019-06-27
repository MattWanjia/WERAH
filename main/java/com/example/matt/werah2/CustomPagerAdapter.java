package com.example.matt.werah2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by matt on 12/22/2017.
 */

public class CustomPagerAdapter extends FragmentPagerAdapter{
    public CustomPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                JobSeekerTab jobSeekerTab = new JobSeekerTab();
                return jobSeekerTab;
            case 1:
                JobCreatorTab jobCreatorTab = new JobCreatorTab();
                return jobCreatorTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "LOOKING";
            case 1:
                return "POSTING";
        }

        return null;
    }
}

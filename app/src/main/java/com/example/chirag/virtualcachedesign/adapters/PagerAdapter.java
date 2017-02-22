package com.example.chirag.virtualcachedesign.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.chirag.virtualcachedesign.fragments.ProfileFragment;
import com.example.chirag.virtualcachedesign.fragments.SubscriptionFragment;
import com.example.chirag.virtualcachedesign.fragments.TicketFragment;
import com.example.chirag.virtualcachedesign.fragments.TrendingFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public TrendingFragment trendingFragment;
    public SubscriptionFragment subscriptionFragment;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                trendingFragment = new TrendingFragment();
                return trendingFragment;
            case 1:
                subscriptionFragment = new SubscriptionFragment();
                return subscriptionFragment;
            case 2:
                TicketFragment ticketFragment = new TicketFragment();
                return ticketFragment;
            case 3:
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
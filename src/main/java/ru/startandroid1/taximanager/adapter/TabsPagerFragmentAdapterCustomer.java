package ru.startandroid1.taximanager.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ru.startandroid1.taximanager.fragment.customer.CustomerFragmentAllRequests;
import ru.startandroid1.taximanager.fragment.customer.CustomerFragmentCurrentRequest;
import ru.startandroid1.taximanager.fragment.customer.CustomerFragmentFeedbacks;

public class TabsPagerFragmentAdapterCustomer extends FragmentPagerAdapter {

    private String[] tabs;

    public TabsPagerFragmentAdapterCustomer(FragmentManager fm) {
        super(fm);
        tabs = new String[]{
            "Current request",
            "All requests",
            "Send message"
        };
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return CustomerFragmentCurrentRequest.getInstance();
            case 1:
                return CustomerFragmentAllRequests.getInstance();
            case 2:
                return CustomerFragmentFeedbacks.getInstance();
        }

        return null;
    }

    @Override
    public int getCount() {
        return tabs.length;
    }
}

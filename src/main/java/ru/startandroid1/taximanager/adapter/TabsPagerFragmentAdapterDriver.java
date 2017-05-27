package ru.startandroid1.taximanager.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ru.startandroid1.taximanager.fragment.driver.DriverFragmentAllTransportations;
import ru.startandroid1.taximanager.fragment.driver.DriverFragmentCurrentTransportation;
import ru.startandroid1.taximanager.fragment.driver.DriverFragmentMessages;
import ru.startandroid1.taximanager.fragment.driver.DriverFragmentNewTransportations;

public class TabsPagerFragmentAdapterDriver extends FragmentPagerAdapter{

    private String[] tabs;

    public TabsPagerFragmentAdapterDriver(FragmentManager fm) {
        super(fm);
        tabs = new String[]{
                "New transportations",
                "Current transportation",
                "All transportations",
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
                return DriverFragmentNewTransportations.getInstance();
            case 1:
                return DriverFragmentCurrentTransportation.getInstance();
            case 2:
                return DriverFragmentAllTransportations.getInstance();
            case 3:
                return DriverFragmentMessages.getInstance();
        }

        return null;
    }

    @Override
    public int getCount() {
        return tabs.length;
    }
}
